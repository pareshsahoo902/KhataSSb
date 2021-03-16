package com.ssb.ssbapp.Utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ssb.ssbapp.Admin.CreateEmployee.AddEmploeePayroll;
import com.ssb.ssbapp.DialogHelper.CustomProgressDailog;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.Sessions.LocalSession;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SSBBaseActivity extends AppCompatActivity {

    private AlertDialog dialog;
    public static Toolbar mToolbar;
    public LocalSession session;
    public Calendar myCalendar;


    /**
     * Fetching the currency according the the phone number while login or sign up
     * @return
     */
    public String getCurrencyStr() {

        String outputStr = "₹";

        return outputStr;
    }

    /**
     * Init the Progress bar to use in the whole application
     *
     * @return
     */
    public AlertDialog getProgressDialog() {
        if (dialog == null) {
            dialog = CustomProgressDailog.getLoadingDialog(this, "Loading..");
        }
        return dialog;
    }

    /**
     *  For showing or displaying the progress bar in the whole application
     */
    public void showProgress() {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!isFinishing() && getProgressDialog() != null && !getProgressDialog().isShowing()) {
                        getProgressDialog().show();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  For hiding or dismissing the progress bar in the whole application
     */
    public void dismissProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing() && getProgressDialog().isShowing()) {
                    try {
                        getProgressDialog().dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    /**
     * Custom Toast layout
     *
     * @param message
     * @param isError
     */
    public void showMessageToast(String message, boolean isError) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast_layout, (ViewGroup) findViewById(R.id.custom_toast_layout));
        TextView tv = (TextView) layout.findViewById(R.id.toast_message_textview);
        ImageView im = (ImageView) layout.findViewById(R.id.errorimage);
        if (isError == true) {
            im.setVisibility(View.VISIBLE);
            tv.setTextColor(Color.WHITE);
            layout.setBackgroundColor(Color.RED);
        }
        tv.setText(message);
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    /**
     * Init the toolbar to be used in the whole application
     *
     * @param context
     * @param toolbarTitleString
     */
    public void setToolbar(final Context context, String toolbarTitleString) {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView toolbarTitle=mToolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setText(toolbarTitleString);
    }


    /**
     * Init the session class
     * @return
     */
    public LocalSession getLocalSession() {
        if (session == null) {
            session = new LocalSession(SSBBaseActivity.this);
        }
        return session;
    }




}
