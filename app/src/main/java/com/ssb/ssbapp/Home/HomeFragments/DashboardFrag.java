package com.ssb.ssbapp.Home.HomeFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.ssb.ssbapp.Home.HomeActivity;
import com.ssb.ssbapp.KhataMaster.KhataManagment;
import com.ssb.ssbapp.Login.LoginActivity;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.SplashScreen.SplashActivity;
import com.ssb.ssbapp.Staff.StaffManagmentActivity;
import com.ssb.ssbapp.TrayMaster.TrayManagmentActivity;


public class DashboardFrag extends Fragment {

    private CardView logout, khataCard, trayCard, staffCard;

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
                startActivity(new Intent(getContext(), LoginActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
        return v;
    }
}