package com.ssb.ssbapp.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssb.ssbapp.R;

public class KhataListViewHolder extends RecyclerView.ViewHolder {

    public TextView khataName ;
    public ImageView isActiveBtn;

    public KhataListViewHolder(@NonNull View itemView) {
        super(itemView);

        khataName = itemView.findViewById(R.id.khataname1);
        isActiveBtn = itemView.findViewById(R.id.currentkhatabtn);

    }
}
