package com.ssb.ssbapp.KhataMaster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ssb.ssbapp.R;
import com.ssb.ssbapp.Utils.SSBBaseActivity;

public class KhataManagment extends SSBBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khata_managment);

        setToolbar(getApplicationContext(),"Manage Khata");


    }
}