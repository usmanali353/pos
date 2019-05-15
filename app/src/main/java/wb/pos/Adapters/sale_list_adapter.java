package wb.pos.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import wb.pos.R;
import wb.pos.Model.dbhelper;
import wb.pos.Model.products;
import wb.pos.network_operations;

public class sale_list_adapter extends BaseAdapter {
    dbhelper db;
    public sale_list_adapter(ArrayList<products> productsArrayList, Context context) {
        this.productsArrayList = productsArrayList;
        this.context = context;
        db=new dbhelper(context);
    }

    ArrayList<products> productsArrayList;
    Context context;

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
        sale_list_viewholder vh;
        if(convertView==null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_lineitem, parent, false);
            vh=new sale_list_viewholder();
            vh.name=convertView.findViewById(R.id.name);
            vh.price=convertView.findViewById(R.id.price);
            vh.quantity=convertView.findViewById(R.id.quantity);
            convertView.setTag(vh);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder remove=new AlertDialog.Builder(context);
                remove.setTitle("Remove Product");
                remove.setMessage("Are you sure you want to delete this Product");
                remove.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i=new Intent("minus_price");
                        i.putExtra("minus_price",productsArrayList.get(position).getProduct_price());
                        LocalBroadcastManager.getInstance(context).sendBroadcast(i);
                        network_operations.increment_quantity(String.valueOf(productsArrayList.get(position).getProduct_ID()),context,String.valueOf(productsArrayList.get(position).getProduct_quantity()));
                        Integer rows=db.delete(String.valueOf(productsArrayList.get(position).getProduct_ID()));
                        if(rows>0){
                            productsArrayList.remove(productsArrayList.get(position));
                            notifyDataSetChanged();
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        });
        vh= (sale_list_viewholder) convertView.getTag();
        vh.name.setText(productsArrayList.get(position).getProduct_name());
        vh.price.setText(String.valueOf(productsArrayList.get(position).getProduct_price()));
        vh.quantity.setText(String.valueOf(productsArrayList.get(position).getProduct_quantity()));
        return convertView;
    }
    class sale_list_viewholder{
        TextView name,quantity,price;
    }
}
