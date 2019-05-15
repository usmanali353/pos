package wb.pos.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import wb.pos.Model.dbhelper;
import wb.pos.Model.products;
import wb.pos.R;
import wb.pos.network_operations;

public class add_to_cart_dialog_fragment extends DialogFragment {
    EditText percentage;
    Button confirm;
    Button clear;
    dbhelper db;
    SharedPreferences prefs;
    products p;
    TextView title,subtitle;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.dialog_payment,container,false);
         percentage=v.findViewById(R.id.percentage_txt);
         confirm=v.findViewById(R.id.confirmButton);
         clear=v.findViewById(R.id.clearButton);
         prefs= PreferenceManager.getDefaultSharedPreferences(getActivity());
         db=new dbhelper(getActivity());
         title=v.findViewById(R.id.title);
         subtitle=v.findViewById(R.id.subtitle);
         title.setText("Add to Cart");
         subtitle.setText("Quantity");
         percentage.setHint("Quantity");
        Bundle bundle=this.getArguments();
         p=new Gson().fromJson(bundle.getString("selected_product"),products.class);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prefs.getString("user_role", null) != null && prefs.getString("user_role", null).equals("Admin")) {
                    return;
                } else {
                    if (p.getProduct_quantity() > 0) {
                        if (!db.check_if_product_already_exist(String.valueOf(p.getProduct_ID()), prefs.getString("email", null))) {
                            db.insert(p.getProduct_name(), p.getProduct_price() * Integer.valueOf(percentage.getText().toString()), Integer.valueOf(percentage.getText().toString()), String.valueOf(p.getProduct_ID()), prefs.getString("email", null));
                            network_operations.decrement_quantity(String.valueOf(p.getProduct_ID()),getActivity(),percentage.getText().toString());
                            Intent i=new Intent("refresh_products");
                            i.putExtra("refresh_products","refresh");
                            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(i);
                        } else {
                            db.delete(String.valueOf(p.getProduct_ID()), prefs.getString("email", ""));
                            db.insert(p.getProduct_name(), p.getProduct_price() * Integer.valueOf(percentage.getText().toString()), Integer.valueOf(percentage.getText().toString()), String.valueOf(p.getProduct_ID()), prefs.getString("email", null));
                           network_operations.decrement_quantity(String.valueOf(p.getProduct_ID()),getActivity(),percentage.getText().toString());
                            Intent i=new Intent("refresh_products");
                            i.putExtra("refresh_products","refresh");
                            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(i);
                        }
                    }else{
                        Toast.makeText(getActivity(),"This Product is not Available",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

         clear.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 add_to_cart_dialog_fragment.this.dismiss();
             }
         });

        return v;

    }
}
