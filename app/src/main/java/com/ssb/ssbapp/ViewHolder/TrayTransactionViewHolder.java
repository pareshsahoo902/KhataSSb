package com.ssb.ssbapp.ViewHolder;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssb.ssbapp.R;

public class TrayTransactionViewHolder extends RecyclerView.ViewHolder {

    public TextView dateText,detailText,trayCountText,descriptionText,gaveText,gotText,balance,name;
    public ImageView billImage;
    public FrameLayout gotLayout, gaveLayout;

    public TrayTransactionViewHolder(@NonNull View itemView) {
        super(itemView);

        dateText = itemView.findViewById(R.id.dateText);
        detailText = itemView.findViewById(R.id.detailText);
        trayCountText = itemView.findViewById(R.id.trayCountText);
        descriptionText = itemView.findViewById(R.id.descriptionText);
        gaveText = itemView.findViewById(R.id.gaveText);
        gotText = itemView.findViewById(R.id.gotText);
        billImage = itemView.findViewById(R.id.billIMage);
        gotLayout = itemView.findViewById(R.id.gotFrame);
        gaveLayout = itemView.findViewById(R.id.giveFrame);
        balance = itemView.findViewById(R.id.balanceText);
        name = itemView.findViewById(R.id.nameCus);
        name.setVisibility(View.GONE);
    }
}
