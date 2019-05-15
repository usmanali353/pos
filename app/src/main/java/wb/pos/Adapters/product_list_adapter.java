package wb.pos.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import wb.pos.Fragments.update_product_fragment;
import wb.pos.R;
import wb.pos.Model.dbhelper;
import wb.pos.Model.products;
import wb.pos.filter_products;
import wb.pos.network_operations;

public class product_list_adapter extends BaseAdapter implements Filterable {
   public ArrayList<products> productsArrayList;
    ArrayList<products> filtered_list;
    Context context;
     dbhelper db;
     SharedPreferences prefs;
     FragmentManager manager;
    public product_list_adapter(ArrayList<products> productsArrayList, Context context) {
        this.productsArrayList = productsArrayList;
        this.context = context;
        db=new dbhelper(context);
        this.filtered_list=productsArrayList;
        prefs=PreferenceManager.getDefaultSharedPreferences(context);
        manager=((AppCompatActivity)context).getSupportFragmentManager();
    }

    @Override
    public int getCount() {
        return productsArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return productsArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
         product_list_viewholder vh;
        if(convertView==null){
            convertView=LayoutInflater.from(context).inflate(R.layout.listview_inventory,parent,false);
            vh=new product_list_viewholder();
            vh.name=convertView.findViewById(R.id.name);
            vh.options=convertView.findViewById(R.id.optionView);
            convertView.setTag(vh);
        }
        vh=(product_list_viewholder) convertView.getTag();
        vh.name.setText(productsArrayList.get(position).getProduct_name());
        if(prefs.getString("user_role",null)!=null&&!prefs.getString("user_role",null).equals("Admin")){
            vh.options.setVisibility(View.GONE);
        }

        vh.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_product_fragment f=new update_product_fragment();
                Bundle b=new Bundle();
                b.putString("selected_product",new Gson().toJson(productsArrayList.get(position)));
                f.setArguments(b);
                f.show(manager,"update_product_fragment");
                /*AlertDialog.Builder edit_product_dialog=new AlertDialog.Builder(context);
                edit_product_dialog.setTitle("Edit Product");
                View edit_product_view=LayoutInflater.from(context).inflate(R.layout.layout_productdetail_detail,null);
                EditText name=edit_product_view.findViewById(R.id.nameBox);
                EditText quantity=edit_product_view.findViewById(R.id.quantityBox);
                EditText price=edit_product_view.findViewById(R.id.priceBox);
                EditText barcode=edit_product_view.findViewById(R.id.barcodeBox);
                name.setText(productsArrayList.get(position).getProduct_name());
                quantity.setText(String.valueOf(productsArrayList.get(position).getProduct_quantity()));
                price.setText(String.valueOf(productsArrayList.get(position).getProduct_price()));
                barcode.setText(String.valueOf(productsArrayList.get(position).getProduct_ID()));
                edit_product_dialog.setView(edit_product_view);
                edit_product_dialog.show();*/
            }
        });
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new filter_products(filtered_list,this);
    }

    class product_list_viewholder{
        TextView name;
        View options;
    }
}
