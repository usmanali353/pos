package wb.pos.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import wb.pos.Model.reports;
import wb.pos.R;
import wb.pos.report_detail_page;

public class report_list_adapter extends BaseAdapter {
    ArrayList<reports> reports_list;
    Context context;

    public report_list_adapter(ArrayList<reports> reports_list, Context context) {
        this.reports_list = reports_list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return reports_list.size();
    }

    @Override
    public Object getItem(int position) {
        return reports_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        report_list_viewholder vh;
        if(convertView==null){
            convertView=LayoutInflater.from(context).inflate(R.layout.listview_report,parent,false);
            vh=new report_list_viewholder();
            vh.sid=convertView.findViewById(R.id.sid);
            vh.report_date=convertView.findViewById(R.id.startTime);
            vh.total_bill=convertView.findViewById(R.id.total);
            convertView.setTag(vh);
        }
        vh= (report_list_viewholder) convertView.getTag();
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context,report_detail_page.class);
                i.putExtra("report",new Gson().toJson(reports_list.get(position)));
                context.startActivity(i);
            }
        });
         vh.sid.setText(String.valueOf(reports_list.get(position).getId()));
         vh.total_bill.setText(String.valueOf(reports_list.get(position).getTotal_amount()));
         vh.report_date.setText(reports_list.get(position).getDate());
        return convertView;
    }
    class report_list_viewholder{
        TextView sid,report_date,total_bill;
    }
}
