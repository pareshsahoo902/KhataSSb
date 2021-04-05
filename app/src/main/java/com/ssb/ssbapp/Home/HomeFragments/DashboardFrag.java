package com.ssb.ssbapp.Home.HomeFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.ssb.ssbapp.Home.HomeActivity;
import com.ssb.ssbapp.KhataMaster.KhataManagment;
import com.ssb.ssbapp.Login.LoginActivity;
import com.ssb.ssbapp.ProfileDetails.ProfilePage;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.Sessions.LocalSession;
import com.ssb.ssbapp.SplashScreen.SplashActivity;
import com.ssb.ssbapp.Staff.StaffManagmentActivity;
import com.ssb.ssbapp.TrayMaster.TrayManagmentActivity;
import com.ssb.ssbapp.Utils.Constants;
import com.ssb.ssbapp.Utils.FirebaseHelper;


public class DashboardFrag extends Fragment {

    private CardView logout, khataCard, trayCard, staffCard;
    private TextView profile_name;
    private ImageView profile_pic;
    private FrameLayout edit_btn;

    public DashboardFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);
        logout = v.findViewById(R.id.logout);
        trayCard = v.findViewById(R.id.traymanagement);
        khataCard = v.findViewById(R.id.khatamanage);
        staffCard = v.findViewById(R.id.staffmanage);
        profile_name = v.findViewById(R.id.profile_name);
        profile_pic = v.findViewById(R.id.profile_pic);
        edit_btn = v.findViewById(R.id.edit_btn);


//        profile_name.setText(LocalSession.getString(Constants.SSB_PREF_NAME));

        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getContext(), ProfilePage.class));
            }
        });


        trayCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), TrayManagmentActivity.class));
            }
        });
        khataCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), KhataManagment.class));
            }
        });
        staffCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), StaffManagmentActivity.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                FirebaseHelper.getInstance().clearApplicationData();
                LocalSession.clear();
                startActivity(new Intent(getContext(), LoginActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
        return v;
    }
}