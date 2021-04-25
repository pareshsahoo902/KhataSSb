package com.ssb.ssbapp.TrayMaster;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssb.ssbapp.R;

public class TrayStockViewHolder extends RecyclerView.ViewHolder {

    public TextView got,gave, trayName , avaiable;

    public TrayStockViewHolder(@NonNull View itemView) {
        super(itemView);

        got = itemView.findViewById(R.id.trayGot);
        gave = itemView.findViewById(R.id.trayGave);
        avaiable = itemView.findViewById(R.id.trayAvailbe);
        trayName = itemView.findViewById(R.id.trayNameT);
    }
}
