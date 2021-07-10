package com.ssb.ssbapp.DailyReport;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class DailyReportFragAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;



    public DailyReportFragAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }


    @NonNull

    @Override
    public Fragment getItem(int position) {
        switch (position){

            case 0:
                MoneyReport frag=new MoneyReport();
                return frag;
            case 1:
                TrayReport fragg= new TrayReport();
                return fragg;

            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
