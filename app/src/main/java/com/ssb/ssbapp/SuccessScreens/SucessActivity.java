package com.ssb.ssbapp.SuccessScreens;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;

import com.airbnb.lottie.LottieAnimationView;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.Staff.StaffManagmentActivity;
import com.ssb.ssbapp.Utils.Constants;

public class SucessActivity extends AppCompatActivity {

    LottieAnimationView lottieanim;
    String intentType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sucess);

        intentType = getIntent().getStringExtra(Constants.SSB_SUCESS_INTENT);

        lottieanim = findViewById(R.id.lottieanim);

        lottieanim.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {


            }

            @Override
            public void onAnimationEnd(Animator animation) {

                if (intentType.equals("staff")){
                    startActivity(new Intent(SucessActivity.this, StaffManagmentActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                }
                else{
                    finish();

                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}