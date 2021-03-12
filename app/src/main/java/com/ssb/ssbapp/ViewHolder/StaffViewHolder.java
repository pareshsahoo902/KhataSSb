package com.ssb.ssbapp.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssb.ssbapp.R;

public class StaffViewHolder extends RecyclerView.ViewHolder {
    public ImageView staffImage;
    public TextView name, DOJ, salary;
    public Button viewDetails;

    public StaffViewHolder(@NonNull View itemView) {
        super(itemView);

        staffImage = itemView.findViewById(R.id.staff_image);
        name = itemView.findViewById(R.id.staffNameTitle);
        DOJ = itemView.findViewById(R.id.staffDateOfJoining);
        salary = itemView.findViewById(R.id.staffSalary);
        viewDetails = itemView.findViewById(R.id.viewStaffdetails);

    }
}
