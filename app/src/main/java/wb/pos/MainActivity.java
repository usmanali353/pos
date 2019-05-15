package wb.pos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import wb.pos.Adapters.viewpager_adapter;
import wb.pos.Fragments.Report_fragment;
import wb.pos.Fragments.products_catalog_fragment;
import wb.pos.Fragments.sale_fragment;
import wb.pos.Model.dbhelper;

public class MainActivity extends AppCompatActivity {
PagerTitleStrip tabs;
ViewPager pager;
ArrayList<String> titles;
ArrayList<Fragment> fragments;
viewpager_adapter adapter;
Toolbar toolbar;
SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Inventory");
        prefs= PreferenceManager.getDefaultSharedPreferences(this);
       tabs=findViewById(R.id.title);
       pager=findViewById(R.id.pager);
       titles=new ArrayList<>();
       fragments=new ArrayList<>();
       if(prefs.getString("user_role",null)!=null&&prefs.getString("user_role",null).equals("Admin")){
           titles.add("Inventory");
           titles.add("Reports");
           fragments.add(new products_catalog_fragment());
           fragments.add(new Report_fragment());
           adapter=new viewpager_adapter(getSupportFragmentManager(),titles,fragments);
           pager.setAdapter(adapter);
       }else if(prefs.getString("user_role",null)!=null&&prefs.getString("user_role",null).equals("retailer")){
           titles.add("Inventory");
           titles.add("Sale");
           fragments.add(new products_catalog_fragment());
           fragments.add(new sale_fragment());
           adapter=new viewpager_adapter(getSupportFragmentManager(),titles,fragments);
           pager.setAdapter(adapter);
       }
        Log.e("role", prefs.getString("user_role",null));

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
           @Override
           public void onPageScrolled(int i, float v, int i1) {

           }

           @Override
           public void onPageSelected(int i) {
                Fragment sale_fragment=adapter.getItem(1);
                if(i==1&&sale_fragment!=null){
                    sale_fragment.onResume();
                }
                if(i==0){
                    getSupportActionBar().setTitle("Inventory");
                }else if(i==1){
                    getSupportActionBar().setTitle("Sale");
                }else if(i==2){
                    getSupportActionBar().setTitle("Reports");
                }
           }

           @Override
           public void onPageScrollStateChanged(int i) {

           }
       });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.sign_out){
            prefs.edit().remove("email").apply();
            prefs.edit().remove("Phone").apply();
            prefs.edit().remove("password").apply();
            prefs.edit().remove("Name").apply();
            prefs.edit().remove("user_role").apply();
            startActivity(new Intent(MainActivity.this,login_page.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
