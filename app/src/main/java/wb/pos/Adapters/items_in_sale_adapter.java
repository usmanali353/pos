package wb.pos.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import wb.pos.Model.products;
import wb.pos.R;
import java.util.ArrayList;

import wb.pos.Model.reports;

public class items_in_sale_adapter extends BaseAdapter {
    ArrayList<products> productsArrayList;
    Context context;

    public items_in_sale_adapter(ArrayList<products> reportsArrayList, Context context) {
        this.productsArrayList = reportsArrayList;
        this.context = context;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        sold_item_viewholder vh;
        if(convertView==null){
            convertView=LayoutInflater.from(context).inflate(R.layout.listview_lineitem,parent,false);
            vh=new sold_item_viewholder();
            vh.name=convertView.findViewById(R.id.name);
            vh.price=convertView.findViewById(R.id.price);
            vh.quantity=convertView.findViewById(R.id.quantity);
            convertView.setTag(vh);
        }
        vh= (sold_item_viewholder) convertView.getTag();
        vh.name.setText(productsArrayList.get(position).getProduct_name());
        vh.price.setText(String.valueOf(productsArrayList.get(position).getProduct_price()));
        vh.quantity.setText(String.valueOf(productsArrayList.get(position).getProduct_quantity()));
        return convertView;
    }
    class sold_item_viewholder{
        TextView name,quantity,price;
    }
}
