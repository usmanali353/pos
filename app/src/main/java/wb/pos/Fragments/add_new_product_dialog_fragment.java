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
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import wb.pos.MainActivity;
import wb.pos.R;
import wb.pos.network_operations;

public class add_new_product_dialog_fragment extends DialogFragment {
    EditText name;
    EditText quantity;
    EditText price;
    EditText barcode;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.layout_addproduct,container,false);
          name=v.findViewById(R.id.nameBox);
          quantity=v.findViewById(R.id.quantityBox);
          price=v.findViewById(R.id.priceBox);
          barcode=v.findViewById(R.id.barcodeBox);
        Button scan=v.findViewById(R.id.scanButton);
        Button clear=v.findViewById(R.id.clearButton);
        Button save=v.findViewById(R.id.confirmButton);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(barcode.getText().toString().equals("")) {
                    network_operations.add_product_with_id(getActivity(), Long.parseLong(barcode.getText().toString()), name.getText().toString(), Integer.parseInt(price.getText().toString()), Integer.parseInt(quantity.getText().toString()));
                    startActivity(new Intent(getActivity(),MainActivity.class));
                  // getActivity().finish();
                    Intent i=new Intent("refresh_products");
                    i.putExtra("refresh_products","refresh");
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(i);
                }else{
                    network_operations.add_product_without_id(getActivity(),name.getText().toString(),Integer.parseInt(quantity.getText().toString()),Integer.parseInt(price.getText().toString()));
                    startActivity(new Intent(getActivity(),MainActivity.class));
                   //getActivity().finish();
                    Intent i=new Intent("refresh_products");
                    i.putExtra("refresh_products","refresh");
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(i);
                }
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               add_new_product_dialog_fragment.this.dismiss();
            }
        });
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator.forSupportFragment(add_new_product_dialog_fragment.this).setBeepEnabled(true).initiateScan();
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
               barcode.setText(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
