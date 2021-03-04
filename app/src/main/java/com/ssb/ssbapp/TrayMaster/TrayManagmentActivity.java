package com.ssb.ssbapp.TrayMaster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ssb.ssbapp.R;
import com.ssb.ssbapp.Utils.SSBBaseActivity;

public class TrayManagmentActivity extends SSBBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tray_managment);

        setToolbar(getApplicationContext(),"Add Tray");
    }
}