package com.ssb.ssbapp.Adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssb.ssbapp.R;
import com.ssb.ssbapp.ViewHolder.CashDetailsViewHolder;

public class MoneyReportAdapter extends RecyclerView.Adapter<CashDetailsViewHolder> {
    @NonNull
    @Override
    public CashDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CashDetailsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cash_detail_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CashDetailsViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
