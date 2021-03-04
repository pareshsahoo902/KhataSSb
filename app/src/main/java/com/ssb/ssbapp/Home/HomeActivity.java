package com.ssb.ssbapp.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.ssb.ssbapp.Home.HomeFragments.DashboardFrag;
import com.ssb.ssbapp.Home.HomeFragments.MoneyFrag;
import com.ssb.ssbapp.Home.HomeFragments.StaffFrag;
import com.ssb.ssbapp.Home.HomeFragments.TrayFrag;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.Utils.SSBBaseActivity;

import java.util.Calendar;

public class HomeActivity extends SSBBaseActivity {

    private RelativeLayout khatabar;
    private ImageView calendarbtn;
    private Calendar myCalendar;
    private TextView khataname,datetext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView navView = findViewById(R.id.nav_view_bottom);
        navView.setOnNavigationItemSelectedListener(navlistner);
        khatabar=findViewById(R.id.khatabar);
        khataname=findViewById(R.id.titlekhata);
        calendarbtn=findViewById(R.id.calendarbtn);
        datetext=findViewById(R.id.datetext);



        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MoneyFrag()).commit();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navlistner=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment =null;


                    switch (menuItem.getItemId()) {
                        case R.id.navigation_money:
                            selectedFragment= new MoneyFrag();
                            break;
                        case R.id.navigation_tray:
                            selectedFragment= new TrayFrag();
                            break;
                        case R.id.navigation_staff:
                            selectedFragment= new StaffFrag();
                            break;
                        case R.id.navigation_dashboard:
                            selectedFragment= new DashboardFrag();
                            break;

                    }


                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

                    return true;
                }
            };


}