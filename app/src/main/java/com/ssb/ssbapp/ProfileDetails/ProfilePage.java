package com.ssb.ssbapp.ProfileDetails;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ssb.ssbapp.R;
import com.ssb.ssbapp.Utils.SSBBaseActivity;

public class ProfilePage extends SSBBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        setToolbar(getApplicationContext(),"Profile");
    }
}