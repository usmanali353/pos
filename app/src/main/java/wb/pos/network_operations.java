package wb.pos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import wb.pos.Adapters.product_list_adapter;
import wb.pos.Adapters.report_list_adapter;
import wb.pos.Fragments.add_to_cart_dialog_fragment;
import wb.pos.Model.UsersDB;
import wb.pos.Model.dbhelper;
import wb.pos.Model.products;
import wb.pos.Model.reports;
public class network_operations {
    public static void add_product_with_id(final Context context, long barcode, String name, int quantity, int price){
        pos_interface service=apiclient.getClient().create(pos_interface.class);
        final AlertDialog dialog = new SpotsDialog(context);
        dialog.show();
        dialog.setMessage("Please Wait...");
        Call<String> call =service.insert_products(barcode,name,price,quantity);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                dialog.dismiss();
                if(response.body()!=null){

                    Toast.makeText(context,response.body(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(context,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
    public static void add_product_without_id(final Context context, String name, int quantity, int price){
        pos_interface service=apiclient.getClient().create(pos_interface.class);
        final AlertDialog dialog = new SpotsDialog(context);
        dialog.show();
        dialog.setMessage("Please Wait...");
        Call<String> call =service.insert_products_without_id(name,price,quantity);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                dialog.dismiss();
                if(response.body()!=null){
                    Toast.makeText(context,response.body(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(context,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
    public static void get_all_products(final Context context, final ListView product_list, final EditText search, final dbhelper db, final SharedPreferences prefs){
        pos_interface service=apiclient.getClient().create(pos_interface.class);
        final AlertDialog dialog = new SpotsDialog(context);
        dialog.show();
        dialog.setMessage("Please Wait...");
        Call<ArrayList<products>> call=service.get_all_products();
        call.enqueue(new Callback<ArrayList<products>>() {
            @Override
            public void onResponse(Call<ArrayList<products>> call, Response<ArrayList<products>> response) {
                final ArrayList<products> productsArrayList=response.body();
                dialog.dismiss();
                if(productsArrayList!=null&&productsArrayList.size()>0){
                    final product_list_adapter ad=new product_list_adapter(productsArrayList,context);
                    product_list.setAdapter(ad);
                    product_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if(prefs.getString("user_role", null) != null &&!prefs.getString("user_role", null).equals("Admin")) {
                                Bundle b = new Bundle();
                                b.putString("selected_product", new Gson().toJson(productsArrayList.get(position)));
                                add_to_cart_dialog_fragment f = new add_to_cart_dialog_fragment();
                                f.setArguments(b);
                                f.show(((AppCompatActivity) context).getSupportFragmentManager(), "add_to_cart");
                            }
                        }
                    });
                    search.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                             ad.getFilter().filter(s);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                }else{
                    Toast.makeText(context,"No Products Found",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ArrayList<products>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(context,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }
    public static ArrayList<products> show_products_for_sale(dbhelper mydb, String Username, Context context){
        ArrayList<products> productsincart=new ArrayList<>();
        Cursor res = mydb.get_all_products(Username);
        if (res.getCount() == 0) {
            Toast.makeText(context,"No Products in Cart",Toast.LENGTH_LONG) .show();
        }
        while (res.moveToNext()) {
            products p=new products(Long.parseLong(res.getString(4)),res.getString(1),res.getInt(3),res.getInt(2),res.getInt(0));

            productsincart.add(p);
        }
        return productsincart;
    }
    public static void add_reports(final Context context, dbhelper db, String date,String total){
        pos_interface service=apiclient.getClient().create(pos_interface.class);
        final AlertDialog dialog = new SpotsDialog(context);
        dialog.show();
        dialog.setMessage("Please Wait...");
        SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(context);

        Call<String> call=service.add_report(new Gson().toJson(show_products_for_sale(db,prefs.getString("email",null),context)),date,prefs.getString("email",null),total);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                dialog.dismiss();
                if(response.body()!=null){
                    Toast.makeText(context,response.body(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                dialog.dismiss();
               Toast.makeText(context,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
    public static void sign_in(String email, String password, final Context context){
        pos_interface service=apiclient.getClient().create(pos_interface.class);
        final AlertDialog dialog = new SpotsDialog(context);
        dialog.show();
        dialog.setMessage("Please Wait...");
        Call<ArrayList<UsersDB>> call=service.sign_in(email,password);
        call.enqueue(new Callback<ArrayList<UsersDB>>() {
            @Override
            public void onResponse(Call<ArrayList<UsersDB>> call, Response<ArrayList<UsersDB>> response) {
                dialog.dismiss();
                 ArrayList<UsersDB> user_info=response.body();
                 if(user_info!=null&&user_info.size()>0){
                    Toast.makeText(context,"welcome "+user_info.get(0).getName(),Toast.LENGTH_LONG).show();
                     SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(context);
                     prefs.edit().putString("email",user_info.get(0).getEmail()).apply();
                     prefs.edit().putString("Phone",user_info.get(0).getPhone_No()).apply();
                     prefs.edit().putString("password",user_info.get(0).getPassword()).apply();
                     prefs.edit().putString("Name",user_info.get(0).getName()).apply();
                     prefs.edit().putString("user_role",user_info.get(0).getUser_role()).apply();
                      context.startActivity(new Intent(context,MainActivity.class));
                     ((AppCompatActivity)context).finish();
                 }else{
                     Toast.makeText(context,"Provide Valid Username and Password",Toast.LENGTH_LONG).show();
                 }
            }

            @Override
            public void onFailure(Call<ArrayList<UsersDB>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(context,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }
    public static void update_product_detail(EditText id, EditText name, EditText price, EditText quantity, final Context context){
        pos_interface service=apiclient.getClient().create(pos_interface.class);
        final AlertDialog dialog = new SpotsDialog(context);
        dialog.show();
        dialog.setMessage("Please Wait...");
        Call<String> call=service.edit_product_detail(Long.parseLong(id.getText().toString()),name.getText().toString(),Integer.parseInt(quantity.getText().toString()),Integer.parseInt(price.getText().toString()));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                dialog.dismiss();
                if(response.body()!=null&&!response.body().isEmpty()){
                    Toast.makeText(context,response.body(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                dialog.dismiss();
               Toast.makeText(context,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
    public static void filter_reports_by_dates(String start_date, String end_date, final Context context, final ListView listView,final TextView total){
        pos_interface service=apiclient.getClient().create(pos_interface.class);
        Call<ArrayList<reports>> call=service.get_reports_by_date(start_date,end_date);
        call.enqueue(new Callback<ArrayList<reports>>() {
            @Override
            public void onResponse(Call<ArrayList<reports>> call, Response<ArrayList<reports>> response) {
                ArrayList<reports> reportsArrayList=response.body();
                ArrayList<Float> price_list=new ArrayList<>();
                int sum=0;
                if(reportsArrayList!=null){
                    report_list_adapter adapter=new report_list_adapter(reportsArrayList,context);
                    listView.setAdapter(adapter);
                    for (int i=0;i<reportsArrayList.size();i++){
                        price_list.add(reportsArrayList.get(i).getTotal_amount());
                        sum+=price_list.get(i);
                    }
                    total.setText(String.valueOf(sum));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<reports>> call, Throwable t) {
               Toast.makeText(context,t.getMessage(),Toast.LENGTH_LONG).show();
               ArrayList<reports> reportsArrayList=new ArrayList<>();
                report_list_adapter adapter=new report_list_adapter(reportsArrayList,context);
                listView.setAdapter(adapter);
                total.setText("0");
                adapter.notifyDataSetChanged();
            }
        });
    }
    public static void increment_quantity(String id, final Context context,String quantity){
        pos_interface service=apiclient.getClient().create(pos_interface.class);
        Call<String> call=service.icrement_quantity(id,quantity);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Toast.makeText(context,response.body(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(context,t.getMessage(),Toast.LENGTH_LONG).show();
                Intent i=new Intent("refresh_products");
                i.putExtra("refresh_products","refresh");
                LocalBroadcastManager.getInstance(context).sendBroadcast(i);
            }
        });
    }
    public static void decrement_quantity(String id, final Context context,String quantity){
        pos_interface service=apiclient.getClient().create(pos_interface.class);
        Call<String> call=service.decrement_quantity(id,quantity);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Toast.makeText(context,response.body(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(context,t.getMessage(),Toast.LENGTH_LONG).show();
                Intent i=new Intent("refresh_products");
                i.putExtra("refresh_products","refresh");
                LocalBroadcastManager.getInstance(context).sendBroadcast(i);
            }
        });
    }
}

