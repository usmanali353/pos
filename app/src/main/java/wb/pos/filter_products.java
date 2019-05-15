package wb.pos;

import android.widget.Filter;

import java.util.ArrayList;

import wb.pos.Adapters.product_list_adapter;
import wb.pos.Model.products;

public class filter_products extends Filter {
    ArrayList<products> tobe_filtered_list;
    product_list_adapter adapter;

    public filter_products(ArrayList<products> tobe_filtered_list, product_list_adapter adapter) {
        this.tobe_filtered_list = tobe_filtered_list;
        this.adapter = adapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();
        if(constraint!=null&&constraint.length()>0){
            ArrayList<products> filtered_list=new ArrayList<>();
            constraint=constraint.toString().toUpperCase();
            for (int i=0;i<tobe_filtered_list.size();i++){
                if(tobe_filtered_list.get(i).getProduct_name().contains(constraint)||String.valueOf(tobe_filtered_list.get(i).getProduct_ID()).contains(constraint)){
                    filtered_list.add(tobe_filtered_list.get(i));
                }
            }
            results.values=filtered_list;
            results.count=filtered_list.size();
        }else{
            results.values=tobe_filtered_list;
            results.count=tobe_filtered_list.size();
        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.productsArrayList= (ArrayList<products>) results.values;
        adapter.notifyDataSetChanged();
    }
}
