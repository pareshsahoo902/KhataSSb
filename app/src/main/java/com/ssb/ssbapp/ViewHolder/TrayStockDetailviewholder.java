package com.ssb.ssbapp.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssb.ssbapp.R;


public class TrayStockDetailviewholder extends RecyclerView.ViewHolder {

    public TextView name , trayPending;


    public TrayStockDetailviewholder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.custnameT);
        trayPending = itemView.findViewById(R.id.pending_tray);
    }
}
