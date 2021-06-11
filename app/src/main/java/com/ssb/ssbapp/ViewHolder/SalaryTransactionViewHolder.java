package com.ssb.ssbapp.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssb.ssbapp.R;

public class SalaryTransactionViewHolder extends RecyclerView.ViewHolder {

    public TextView date , gave , got;

    public SalaryTransactionViewHolder(@NonNull View itemView) {
        super(itemView);
        gave= itemView.findViewById(R.id.amountGave);
        got= itemView.findViewById(R.id.amountGot);
        date= itemView.findViewById(R.id.date);

    }


}
