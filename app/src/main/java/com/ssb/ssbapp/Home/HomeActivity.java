package com.ssb.ssbapp.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.ssb.ssbapp.DialogHelper.ImagePickerDailog;
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
import java.util.List;

import static com.ssb.ssbapp.Utils.Constants.SSB_PREF_BRANCH;
import static com.ssb.ssbapp.Utils.Constants.SSB_PREF_DATE;
import static java.security.AccessController.getContext;

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

        Dexter.withContext(getApplicationContext())
                .withPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.SEND_SMS)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                    }
                }).check();

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

                String myFormat = "dd-MM-yyyy hh:mm:ss a"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
                String CurrentDate = sdf.format(myCalendar.getTime());
                datetext.setText("Dt." + CurrentDate.substring(0, 10));
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
                            selectedFragment = new MoneyFrag();
                            break;
                        case R.id.navigation_tray:
                            selectedFragment = new TrayFrag();
                            break;

                        case R.id.navigation_staff:
                            selectedFragment = new StaffFrag();
                            break;
                        case R.id.navigation_dashboard:
                            selectedFragment = new DashboardFrag();
                            break;

                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

                    return true;
                }
            };


}