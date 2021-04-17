package com.ssb.ssbapp.ViewHolder;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssb.ssbapp.R;

public class TrayDetailViewHolder extends RecyclerView.ViewHolder {

    public ImageButton add;
    public TextView trayName;
    public EditText available , enterTrayText;

    public TrayDetailViewHolder(@NonNull View itemView) {
        super(itemView);
        add = itemView.findViewById(R.id.addTrayImagBtn);
        trayName = itemView.findViewById(R.id.traynametext);
        available = itemView.findViewById(R.id.availableText);
        enterTrayText = itemView.findViewById(R.id.enterTrayText);

    }
}
