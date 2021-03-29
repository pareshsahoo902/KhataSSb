package com.ssb.ssbapp.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssb.ssbapp.R;

public class CustomerListViewHolder extends RecyclerView.ViewHolder {

    public TextView nameCust, amount,status;
    public ImageView custImage;

    public CustomerListViewHolder(@NonNull View itemView) {
        super(itemView);


        nameCust = itemView.findViewById(R.id.cust_name);
        amount = itemView.findViewById(R.id.custamount);
        status = itemView.findViewById(R.id.custstatus);
        custImage = itemView.findViewById(R.id.imguserCust);

    }
}
