package wb.pos.Fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import wb.pos.R;
import wb.pos.network_operations;

public class Report_fragment extends Fragment {
    ListView reports_listview;
    Button nextButton,previousButton;
    Spinner spinner;
    TextView total;
    TextView current_date;
    Calendar currentTime;
    DatePickerDialog datePicker;
    public static final int DAILY = 0;
    public static final int WEEKLY = 1;
    public static final int MONTHLY = 2;
    public static final int YEARLY = 3;
    String[]items ={"Daily","Weekly","Monthly","Yearly"};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.layout_report,container,false);
        reports_listview=v.findViewById(R.id.saleListView);
        nextButton=v.findViewById(R.id.nextButton);
        previousButton=v.findViewById(R.id.previousButton);
        spinner=v.findViewById(R.id.spinner1);
        total=v.findViewById(R.id.totalBox);
        current_date=v.findViewById(R.id.currentBox);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setSelection(0);
        spinner.setAdapter(adapter);

       // network_operations.get_all_reports(getActivity(),reports_listview,total);
        currentTime = Calendar.getInstance();
        datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int y, int m, int d) {
                currentTime.set(Calendar.YEAR, y);
                currentTime.set(Calendar.MONTH, m);
                currentTime.set(Calendar.DAY_OF_MONTH, d);
                set_date();
            }
        }, currentTime.get(Calendar.YEAR), currentTime.get(Calendar.MONTH), currentTime.get(Calendar.DAY_OF_MONTH));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                spinner.setSelection(pos);
                set_date();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinner.setSelection(0);
            }

        });
        current_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show();
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDate(-1);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDate(1);
            }
        });
        return v;
    }
public void set_date(){
    int period = spinner.getSelectedItemPosition();
    Calendar cTime = (Calendar) currentTime.clone();
    Calendar eTime = (Calendar) currentTime.clone();

    if(period == DAILY){
        current_date.setText(" [" + getSQLDateFormat(currentTime) +  "] ");
        current_date.setTextSize(16);
    } else if (period == WEEKLY){
        while(cTime.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
            cTime.add(Calendar.DATE, -1);
        }

        String toShow = " [" + getSQLDateFormat(cTime) +  "] ~ [";
        eTime = (Calendar) cTime.clone();
        eTime.add(Calendar.DATE, 7);
        toShow += getSQLDateFormat(eTime) +  "] ";
        current_date.setTextSize(16);
        current_date.setText(toShow);
    } else if (period == MONTHLY){
        cTime.set(Calendar.DATE, 1);
        eTime = (Calendar) cTime.clone();
        eTime.add(Calendar.MONTH, 1);
        eTime.add(Calendar.DATE, -1);
        current_date.setTextSize(18);
        current_date.setText(" [" + currentTime.get(Calendar.YEAR) + "-" + (currentTime.get(Calendar.MONTH)+1) + "] ");
    } else if (period == YEARLY){
        cTime.set(Calendar.DATE, 1);
        cTime.set(Calendar.MONTH, 0);
        eTime = (Calendar) cTime.clone();
        eTime.add(Calendar.YEAR, 1);
        eTime.add(Calendar.DATE, -1);
        current_date.setTextSize(20);
        current_date.setText(" [" + currentTime.get(Calendar.YEAR) +  "] ");
    }
    currentTime = cTime;

    network_operations.filter_reports_by_dates(getSQLDateFormat(cTime)+ " 00:00:00",getSQLDateFormat(eTime)+ " 23:59:59",getActivity(),reports_listview,total);

}
    public String getSQLDateFormat(Calendar instance) {
        SimpleDateFormat  dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return dateFormat.format(instance.getTime()).toString().substring(0,10);
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    private void addDate(int increment) {
        int period = spinner.getSelectedItemPosition();
        if (period == DAILY){
            currentTime.add(Calendar.DATE, 1 * increment);
        } else if (period == WEEKLY){
            currentTime.add(Calendar.DATE, 7 * increment);
        } else if (period == MONTHLY){
            currentTime.add(Calendar.MONTH, 1 * increment);
        } else if (period == YEARLY){
            currentTime.add(Calendar.YEAR, 1 * increment);
        }
        set_date();
    }
    /*
    @Override
    public List<Sale> getAllSaleDuring(Calendar start, Calendar end) {
        String startBound = getSQLDateFormat(start);
        String endBound = getSQLDateFormat(end);
        List<Sale> list = getAllSale(" WHERE end_time BETWEEN '" + startBound + " 00:00:00' AND '" + endBound + " 23:59:59' AND status = 'ENDED'");
        return list;
    }*/
}
