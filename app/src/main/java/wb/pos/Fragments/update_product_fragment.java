package wb.pos.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import java.util.ArrayList;

import wb.pos.Model.products;
import wb.pos.R;
import wb.pos.network_operations;

public class update_product_fragment extends DialogFragment {
     EditText name;
     EditText quantity;
     EditText price;
     EditText barcode;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View edit_product_view= inflater.inflate(R.layout.layout_productdetail_detail,container,false);
      Bundle bundle=this.getArguments();
         products p=new Gson().fromJson(bundle.getString("selected_product"),products.class);
         name=edit_product_view.findViewById(R.id.nameBox);
        quantity=edit_product_view.findViewById(R.id.quantityBox);
        price=edit_product_view.findViewById(R.id.priceBox);
        barcode=edit_product_view.findViewById(R.id.barcodeBox);
        Button clear=edit_product_view.findViewById(R.id.cancelEditButton);
        Button submit=edit_product_view.findViewById(R.id.submitEditButton);
        name.setText(p.getProduct_name());
        quantity.setText(String.valueOf(p.getProduct_quantity()));
        price.setText(String.valueOf(p.getProduct_price()));
        barcode.setText(String.valueOf(p.getProduct_ID()));
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_product_fragment.this.dismiss();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                network_operations.update_product_detail(barcode,name,price,quantity,getActivity());
                Intent i=new Intent("refresh_products");
                i.putExtra("refresh_products","refresh");
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(i);
            }
        });
        return edit_product_view;
    }
}
