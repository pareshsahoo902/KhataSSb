package com.ssb.ssbapp.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssb.ssbapp.R;

public class CashDetailsViewHolder extends RecyclerView.ViewHolder {

    public TextView dateName , inCash , outCash,traydet;

    public CashDetailsViewHolder(@NonNull View itemView) {
        super(itemView);
        dateName = itemView.findViewById(R.id.name_dateText);
        inCash = itemView.findViewById(R.id.inText);
        outCash = itemView.findViewById(R.id.outText);
        traydet = itemView.findViewById(R.id.traydet);
        traydet.setVisibility(View.GONE);

    }
}
