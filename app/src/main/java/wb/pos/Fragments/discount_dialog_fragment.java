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

import wb.pos.R;

public class discount_dialog_fragment extends DialogFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View discount_view=getLayoutInflater().inflate(R.layout.dialog_payment,null);
        final EditText percentage=discount_view.findViewById(R.id.percentage_txt);
        Button confirm=discount_view.findViewById(R.id.confirmButton);
        Button clear=discount_view.findViewById(R.id.clearButton);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent("discount");
                i.putExtra("discount",percentage.getText().toString());
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(i);
                discount_dialog_fragment.this.dismiss();
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               discount_dialog_fragment.this.dismiss();
            }
        });
        return discount_view;
    }
}
