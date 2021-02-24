package com.ssb.ssbapp.DialogHelper;

import android.app.Activity;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.ssb.ssbapp.R;

public class CustomProgressDailog extends DialogFragment {

    static AlertDialog alertDialog;

    public static AlertDialog getLoadingDialog(Activity activity, String MSGText) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialogloader, null);

        TextView MSG = dialogLayout.findViewById(R.id.text);
        MSG.setText(MSGText);
        builder.setView(dialogLayout);

        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

        return alertDialog;
    }
}
