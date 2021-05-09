package com.ssb.ssbapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssb.ssbapp.CashDetails.CashModel;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.TrayDetails.TrayDetailModel;
import com.ssb.ssbapp.TrayModels.TrayModelItem;
import com.ssb.ssbapp.Utils.UtilsMethod;
import com.ssb.ssbapp.ViewHolder.CashDetailsViewHolder;
import com.ssb.ssbapp.ViewHolder.TrayDetailViewHolder;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class TrayDetailAdapter extends RecyclerView.Adapter<CashDetailsViewHolder> {

    private ArrayList<TrayDetailModel> modelArrayList;

    public TrayDetailAdapter(ArrayList<TrayDetailModel> modelArrayList) {
        this.modelArrayList = modelArrayList;
    }

    public void updateList(ArrayList<TrayDetailModel> models){
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
        TrayDetailModel cashModel = modelArrayList.get(position);

        cashDetailsViewHolder.traydet.setText(getDetailText(cashModel.getModelItemArrayList()));
        cashDetailsViewHolder.traydet.setVisibility(View.VISIBLE);
        cashDetailsViewHolder.descText.setText(cashModel.getDescripton());
        cashDetailsViewHolder.descText.setVisibility(View.VISIBLE);
        cashDetailsViewHolder.dateName.setText(UtilsMethod.capitalize(cashModel.getCustomerName()) + "\n" + cashModel.getDate().substring(0,10));
        if (cashModel.getStatus().equalsIgnoreCase("got")) {
            cashDetailsViewHolder.inCash.setText(String.valueOf(cashModel.getTotal()));
        } else {
            cashDetailsViewHolder.outCash.setText( String.valueOf(cashModel.getTotal()));

        }

    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }


    public String getDetailText(ArrayList<TrayModelItem> trayModelItems){
        String detailStr = "";

        for (TrayModelItem item:trayModelItems){
            detailStr+=item.getName()+": "+item.getTotalCount()+"\n";
        }

        return detailStr;
    }
}
