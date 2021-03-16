package com.ssb.ssbapp.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssb.ssbapp.R;

public class TrayListViewHolder extends RecyclerView.ViewHolder {

    public TextView trayName;
    public ImageView deleteTray;

    public TrayListViewHolder(@NonNull View itemView) {
        super(itemView);

        trayName = itemView.findViewById(R.id.trayname1);
        deleteTray = itemView.findViewById(R.id.traydletebtn);

    }
}
