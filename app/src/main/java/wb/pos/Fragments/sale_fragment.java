package wb.pos.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import net.londatiga.android.bluebamboo.MainActivityPrinter;
import net.londatiga.android.bluebamboo.PrintSellInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import wb.pos.Adapters.sale_list_adapter;
import wb.pos.Model.products;
import wb.pos.R;
import wb.pos.Model.dbhelper;
import wb.pos.network_operations;

public class sale_fragment extends Fragment {
    TextView total_price;
    ListView sale_list;
    dbhelper db;
    int total;
    Button confirm,clear;
    ArrayList<wb.pos.Model.products> productsArrayList=new ArrayList<>();
    SharedPreferences prefs;
    sale_list_adapter adapter;
    String discount="0%";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.layout_sale,container,false);
         total_price=v.findViewById(R.id.totalPrice);
         sale_list=v.findViewById(R.id.sale_List);
         confirm=v.findViewById(R.id.endButton);
         clear=v.findViewById(R.id.discount_btn);
        prefs=PreferenceManager.getDefaultSharedPreferences(getActivity());
         db=new dbhelper(getActivity());
         total=db.getTotalOfAmount(prefs.getString("email",null));
         total_price.setText(String.valueOf(total));
          productsArrayList=network_operations.show_products_for_sale(db,prefs.getString("email",null),getActivity());
          adapter=new sale_list_adapter(productsArrayList,getActivity());
          sale_list.setAdapter(adapter);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter("discount"));
          LocalBroadcastManager.getInstance(getActivity()).registerReceiver(minus_price_reciever,new IntentFilter("minus_price"));
          confirm.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  if (total>0&&!total_price.getText().toString().equals("0")&&!total_price.getText().toString().equals("0.0")) {
                      String name = "", price = "", quantity = "";
                      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                      network_operations.add_reports(getActivity(), db, dateFormat.format(Calendar.getInstance().getTime()), total_price.getText().toString());
                      for (int i = 0; i < productsArrayList.size(); i++) {
                          name += productsArrayList.get(i).getProduct_name() + "\n";
                          quantity += String.valueOf(productsArrayList.get(i).getProduct_quantity()) + "\n";
                          price += String.valueOf(productsArrayList.get(i).getProduct_price()) + "\n";
                      }
                      PrintSellInfo info = new PrintSellInfo(
                              prefs.getString("Name", ""),
                              prefs.getString("Phone", ""),
                              prefs.getString("email", ""),
                              String.valueOf(db.getTotalOfAmount(prefs.getString("email", ""))),
                              total_price.getText().toString(),
                              name,
                              quantity,
                              price,
                              String.valueOf(db.getTotalOfAmount(prefs.getString("email", ""))),
                              discount,
                              String.valueOf(db.getTotalOfAmount(prefs.getString("email", ""))),
                              String.valueOf(0),
                              total_price.getText().toString()
                      );
                      Intent i = new Intent(getActivity(), MainActivityPrinter.class);
                      i.putExtra("printData", info);
                      startActivity(i);

                  }else{
                      Toast.makeText(getActivity(),"No Products Found",Toast.LENGTH_LONG).show();
                  }
              }
          });
          clear.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                 /*
                  products=network_operations.show_products_for_sale(db,prefs.getString("email",null),getActivity());
                  sale_list.setAdapter(adapter);
                  adapter.notifyDataSetChanged();
                  total_price.setText("0");*/
                 if(total>0&&!total_price.getText().equals("0")&&!total_price.getText().toString().equals("0.0")) {
                     discount_dialog_fragment discountDialogFragment = new discount_dialog_fragment();
                     discountDialogFragment.show(((AppCompatActivity) getActivity()).getSupportFragmentManager(), "discount_fragment");
                 }

              }
          });
        return v;
    }
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("discount");
            discount=message+"%";
             total_price.setText(String.valueOf(total-Double.valueOf(message)/100*total));
        }
    };
    private BroadcastReceiver minus_price_reciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            int price = intent.getIntExtra("minus_price",0);
            total_price.setText(String.valueOf(total-price));
        }
    };
    @Override
    public void onResume() {
        super.onResume();
        total=db.getTotalOfAmount(prefs.getString("email",null));
        total_price.setText(String.valueOf(total));
        productsArrayList=network_operations.show_products_for_sale(db,prefs.getString("email",null),getActivity());
        if(productsArrayList!=null&&productsArrayList.size()>0){
            sale_list.setAdapter(new sale_list_adapter(productsArrayList,getActivity()));
        }
    }


}
