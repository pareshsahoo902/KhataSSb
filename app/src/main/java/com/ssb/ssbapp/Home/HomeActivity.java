package com.ssb.ssbapp.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
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
import com.ssb.ssbapp.KhataMaster.AddKhataTopDailog;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.Utils.Constants;
import com.ssb.ssbapp.Utils.SSBBaseActivity;
import com.ssb.ssbapp.Utils.UtilsMethod;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.ssb.ssbapp.Utils.Constants.SSB_PREF_BRANCH;
import static com.ssb.ssbapp.Utils.Constants.SSB_PREF_DATE;

public class HomeActivity extends SSBBaseActivity {

    private RelativeLayout khatabar;
    private ImageView calendarbtn;
    private Calendar myCalendar;
    private TextView khataname, datetext;
    private boolean isAdmin;
    private long type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        isAdmin = getLocalSession().getBoolean(Constants.SSB_PREF_ADMIN);
        type = getLocalSession().getLong(Constants.SSB_PREF_TYPE);

        BottomNavigationView navView = findViewById(R.id.nav_view_bottom);
        navView.setOnNavigationItemSelectedListener(navlistner);
        khatabar = findViewById(R.id.khatabar);
        khataname = findViewById(R.id.titlekhata);
        calendarbtn = findViewById(R.id.calendarbtn);
        datetext = findViewById(R.id.datetext);

        if (getLocalSession().getString(SSB_PREF_BRANCH) != null)
            khataname.setText(UtilsMethod.capitalize(getLocalSession().getString(SSB_PREF_BRANCH)));

        datetext.setText("Dt." + UtilsMethod.getCurrentDate().substring(0, 10));
        getLocalSession().putString(SSB_PREF_DATE, UtilsMethod.getCurrentDate().substring(0, 10));



        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
                String CurrentDate = sdf.format(myCalendar.getTime());
                datetext.setText("Dt." + CurrentDate);
                getLocalSession().putString(SSB_PREF_DATE, CurrentDate);

            }

        };


        calendarbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(HomeActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        khatabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddKhataTopDailog dialog = AddKhataTopDailog.newInstace();
                dialog.show(getSupportFragmentManager(), "show_bottomsheet");
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MoneyFrag()).commit();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navlistner =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()) {
                        case R.id.navigation_money:
                            if (isAdmin || type == 1 || type == 4 || type == 3) {
                                selectedFragment = new MoneyFrag();
                            } else {
                                showMessageToast("Access Denied !", true);
                            }
                            break;
                        case R.id.navigation_tray:
                            if (isAdmin || type == 2 || type == 4 || type == 3) {
                                selectedFragment = new TrayFrag();

                            } else {
                                showMessageToast("Access Denied !", true);
                            }
                            break;
                        case R.id.navigation_staff:
                            if (isAdmin) {
                                selectedFragment = new StaffFrag();

                            } else {
                                showMessageToast("Access Denied !", true);

                            }
                            break;
                        case R.id.navigation_dashboard:
                            if (isAdmin) {
                                selectedFragment = new DashboardFrag();

                            } else {
                                showMessageToast("Access Denied !", true);

                            }
                            break;

                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

                    return true;
                }
            };


}