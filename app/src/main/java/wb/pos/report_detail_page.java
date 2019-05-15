package wb.pos;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import wb.pos.Adapters.items_in_sale_adapter;
import wb.pos.Model.products;
import wb.pos.Model.reports;

public class report_detail_page extends AppCompatActivity {
TextView dateBox,totalBox;
ListView items_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dateBox=findViewById(R.id.dateBox);
        totalBox=findViewById(R.id.totalBox);
        items_list=findViewById(R.id.lineitemList);
       reports report=new Gson().fromJson(getIntent().getStringExtra("report"),reports.class);
        dateBox.setText(report.getDate());
        totalBox.setText(String.valueOf(report.getTotal_amount()));
        ArrayList<products> productsArrayList=new Gson().fromJson(report.getItems_name(),new TypeToken<ArrayList<products>>(){}.getType());
        if(productsArrayList!=null&&productsArrayList.size()>0)
        items_list.setAdapter(new items_in_sale_adapter(productsArrayList,this));
    }

}
