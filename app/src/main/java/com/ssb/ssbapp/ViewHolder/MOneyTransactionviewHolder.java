package com.ssb.ssbapp.ViewHolder;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssb.ssbapp.R;

public class MOneyTransactionviewHolder extends RecyclerView.ViewHolder {


    public TextView amountTotal , desc, entryText , date , balance , gotText,gaveText;
    public FrameLayout gaveLayout, gotLayout;
    public ImageView billIMage;

    public MOneyTransactionviewHolder(@NonNull View itemView) {
        super(itemView);

        amountTotal = itemView.findViewById(R.id.amountText);
        desc = itemView.findViewById(R.id.descriptionText);
        entryText = itemView.findViewById(R.id.itemText);
        date = itemView.findViewById(R.id.dateText);
        balance = itemView.findViewById(R.id.balanceText);
        gotText = itemView.findViewById(R.id.gotText);
        gaveText = itemView.findViewById(R.id.gaveText);
        gaveLayout = itemView.findViewById(R.id.giveFrame);
        gotLayout = itemView.findViewById(R.id.gotFrame);
        billIMage = itemView.findViewById(R.id.billIMage);

    }
}
