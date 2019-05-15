package wb.pos.Fragments;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

import wb.pos.MainActivity;
import wb.pos.Model.dbhelper;
import wb.pos.Model.products;
import wb.pos.R;
import wb.pos.network_operations;

public class products_catalog_fragment extends Fragment {
    ListView product_list;
    Button add_new_product,scanButton;
    EditText searchBox;
    SharedPreferences prefs;
    ArrayList<products> productsArrayList;
    dbhelper db;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.layout_inventory,container,false);
        product_list=v.findViewById(R.id.productListView);
        prefs= PreferenceManager.getDefaultSharedPreferences(getActivity());
        db=new dbhelper(getActivity());

        add_new_product=v.findViewById(R.id.addProductButton);
        if(prefs.getString("user_role",null)!=null&&!prefs.getString("user_role",null).equals("Admin")){
            add_new_product.setVisibility(View.GONE);
        }
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(refresh_products_reciever,new IntentFilter("refresh_products"));
        scanButton=v.findViewById(R.id.scanButton);
        searchBox=v.findViewById(R.id.searchBox);
        network_operations.get_all_products(getActivity(),product_list,searchBox,new dbhelper(getActivity()),prefs);
        add_new_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Add_new_product();
            }
        });
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator.forSupportFragment(products_catalog_fragment.this).setBeepEnabled(true).setCameraId(0).initiateScan();
            }
        });
        return v;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_LONG).show();
            } else {
              searchBox.setText(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    private void Add_new_product(){
       new add_new_product_dialog_fragment().show(getFragmentManager(),"add_product_dialog");
    }
  private BroadcastReceiver refresh_products_reciever =new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getStringExtra("refresh_products")!=null||intent.getStringExtra("refresh_products").equals("refresh")){
                network_operations.get_all_products(getActivity(),product_list,searchBox,new dbhelper(getActivity()),prefs);
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(refresh_products_reciever);
    }

}
