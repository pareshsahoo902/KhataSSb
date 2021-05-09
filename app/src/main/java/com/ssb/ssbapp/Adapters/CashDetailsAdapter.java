package com.ssb.ssbapp.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssb.ssbapp.CashDetails.CashModel;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.Utils.UtilsMethod;
import com.ssb.ssbapp.ViewHolder.CashDetailsViewHolder;

import java.util.ArrayList;

public class CashDetailsAdapter extends RecyclerView.Adapter<CashDetailsViewHolder> {

    private ArrayList<CashModel> modelArrayList;
    String getCurrencyStr = "₹";

    public CashDetailsAdapter(ArrayList<CashModel> modelArrayList) {

        this.modelArrayList = modelArrayList;
    }

    public void updateList(ArrayList<CashModel> models){
        this.modelArrayList = models;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CashDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CashDetailsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cash_detail_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CashDetailsViewHolder cashDetailsViewHolder, int position) {

        CashModel cashModel = modelArrayList.get(position);

        cashDetailsViewHolder.dateName.setText(UtilsMethod.capitalize(cashModel.getCustomerName())+ "\n" + cashModel.getDate());
        if (cashModel.getStatus().equalsIgnoreCase("got")) {
            cashDetailsViewHolder.inCash.setText(getCurrencyStr + cashModel.getTotal());
        } else {
            cashDetailsViewHolder.outCash.setText(getCurrencyStr + cashModel.getTotal());
        }

    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }
}
