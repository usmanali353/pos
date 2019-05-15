package wb.pos.Adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

public class viewpager_adapter extends FragmentPagerAdapter {
    ArrayList<String> titles;
    ArrayList<Fragment> fragmentArrayList;
    public viewpager_adapter(FragmentManager fm,ArrayList<String> titles,ArrayList<Fragment> fragments) {
        super(fm);
        this.titles=titles;
        this.fragmentArrayList=fragments;
    }


    @Override
    public Fragment getItem(int i) {
        return fragmentArrayList.get(i);
    }

    @Override
    public int getCount() {
        return fragmentArrayList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
