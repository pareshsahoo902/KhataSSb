package com.ssb.ssbapp.ViewHolder;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssb.ssbapp.R;

public class StaffListItem extends RecyclerView.ViewHolder {

    public TextView nameStaff, amount;
    public ImageView stafImage;

    public StaffListItem(@NonNull View itemView) {
        super(itemView);

        nameStaff = itemView.findViewById(R.id.staff_name);
        amount = itemView.findViewById(R.id.staffamount);
        stafImage = itemView.findViewById(R.id.imguserStaff);

    }
}
