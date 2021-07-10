package com.ssb.ssbapp.Home.HomeFragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.ssb.ssbapp.BuildConfig;
import com.ssb.ssbapp.CashDetails.CashDetailsActivity;
import com.ssb.ssbapp.DailyReport.DailyReport;
import com.ssb.ssbapp.Home.HomeActivity;
import com.ssb.ssbapp.KhataMaster.KhataManagment;
import com.ssb.ssbapp.Login.LoginActivity;
import com.ssb.ssbapp.ProfileDetails.ProfilePage;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.Sessions.LocalSession;
import com.ssb.ssbapp.SplashScreen.SplashActivity;
import com.ssb.ssbapp.Staff.StaffManagmentActivity;
import com.ssb.ssbapp.TransactionPage.ViewTransactonPage;
import com.ssb.ssbapp.TrayDetails.TrayDetailModel;
import com.ssb.ssbapp.TrayDetails.TrayDetails;
import com.ssb.ssbapp.TrayMaster.TrayManagmentActivity;
import com.ssb.ssbapp.TrayMaster.TrayStock;
import com.ssb.ssbapp.Utils.Constants;
import com.ssb.ssbapp.Utils.FirebaseHelper;


public class DashboardFrag extends Fragment {

    private CardView logout,trayDetaisl,trayStock,dailyReport, khataCard, trayCard, staffCard,moneyDetails;
    private TextView profile_name,versionName;
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
        moneyDetails = v.findViewById(R.id.moneyDetails);
        edit_btn = v.findViewById(R.id.edit_btn);
        trayDetaisl = v.findViewById(R.id.TRAYinfo);
        versionName = v.findViewById(R.id.versionID);
        trayStock = v.findViewById(R.id.trayStock);
        dailyReport = v.findViewById(R.id.dailyReport);

        versionName.setText("v"+ BuildConfig.VERSION_NAME);

//        profile_name.setText(LocalSession.getString(Constants.SSB_PREF_NAME));

        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getContext(), ProfilePage.class));
            }
        });


        trayDetaisl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), TrayDetails.class));

            }
        });
        dailyReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), DailyReport.class));

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

        trayStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), TrayStock.class));

            }
        });

        moneyDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), CashDetailsActivity.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(getContext())
                        .setTitle(Html.fromHtml("<font color='#03503E'>Logout </font>"))
                        .setMessage("Are you sure you want to Logout?")
                        .setPositiveButton("LOGOUT", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                FirebaseAuth.getInstance().signOut();
                                FirebaseHelper.getInstance().clearApplicationData();
                                LocalSession.clear();
                                startActivity(new Intent(getContext(), LoginActivity.class)
                                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                            }
                        })
                        .setNegativeButton("CANCEL", null)
                        .show();



            }
        });
        return v;
    }
}