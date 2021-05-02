package com.ssb.ssbapp.SplashScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.ssb.ssbapp.BuildConfig;
import com.ssb.ssbapp.Home.HomeActivity;
import com.ssb.ssbapp.Login.LoginActivity;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.Utils.SSBBaseActivity;

public class SplashActivity extends SSBBaseActivity {

    FirebaseAuth mAuth;
    TextView versionID;
    ImageView img1 , img2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        mAuth=FirebaseAuth.getInstance();
        versionID = findViewById(R.id.versionID);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        versionID.setText("v"+BuildConfig.VERSION_NAME);

        img1.setAlpha((float) 0.9);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                img2.setVisibility(View.VISIBLE);
                img1.setVisibility(View.VISIBLE);

            }
        },500);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mAuth.getCurrentUser()!=null){
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
                }else{
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            }
        },2500);



    }
}