package com.ssb.ssbapp.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.TransactionModel.MoneyTransactionModel;
import com.ssb.ssbapp.TrayModels.TrayModelItem;
import com.ssb.ssbapp.TrayModels.TrayTransactionModel;
import com.ssb.ssbapp.ViewHolder.TrayTransactionViewHolder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TrayTransactionAdapter extends RecyclerView.Adapter<TrayTransactionViewHolder> {

    private ArrayList<TrayTransactionModel> modelArrayList;
    private Context mContext;

    public TrayTransactionAdapter(ArrayList<TrayTransactionModel> modelArrayList, Context mContext) {
        this.mContext = mContext;
        updateList(modelArrayList);
    }

    @NonNull
    @Override
    public TrayTransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TrayTransactionViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.tray_transaction_item,parent
        ,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TrayTransactionViewHolder holder, int position) {

        TrayTransactionModel model = modelArrayList.get(position);
        holder.dateText.setText("Date: "+model.getDate().substring(0,10));
        holder.detailText.setText(model.getDescription());
        holder.descriptionText.setVisibility(View.VISIBLE);
        holder.trayCountText.setText("Total: "+String.valueOf(model.getTotal())+" Trays");
        if (model.getModelItemArrayList().size()>0){
            holder.descriptionText.setText("Trays:"+getDetailText(model.getModelItemArrayList()));

        }
        if (model.getImageUrl().length()>1){
            Picasso.with(mContext).load(model.getImageUrl())
                    .into(holder.billImage);
            holder.billImage.setVisibility(View.VISIBLE);
        }

//        if (model.getBalance() < 0) {
//            holder.balance.setText("Bal:" + String.valueOf(model.getBalance()));
//            holder.balance.setBackgroundColor(mContext.getResources().getColor(R.color.liteGreen));
//        } else {
//            holder.balance.setText("Bal:" + String.valueOf(model.getBalance()));
//            holder.balance.setBackgroundColor(mContext.getResources().getColor(R.color.litered));
//
//        }

        if (model.getStatus().equals("got")) {
            holder.gaveText.setVisibility(View.INVISIBLE);
            holder.gotText.setText(String.valueOf(model.getTotal()));
        } else {
            holder.gotLayout.setVisibility(View.INVISIBLE);
            holder.gaveText.setVisibility(View.VISIBLE);
            holder.gaveText.setText( String.valueOf(model.getTotal()));
        }



    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public String getDetailText(ArrayList<TrayModelItem> trayModelItems){
        String detailStr = "";

        for (TrayModelItem item:trayModelItems){
            detailStr+= "\n"+item.getName()+": "+item.getTotalCount();
        }

        return detailStr;
    }



    public void updateList(ArrayList<TrayTransactionModel> modelArrayList){
        this.modelArrayList=modelArrayList;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a"); //your own date format
        Collections.sort(this.modelArrayList, new Comparator<TrayTransactionModel>() {
            @Override
            public int compare(TrayTransactionModel o1, TrayTransactionModel o2) {
                try {
                    return simpleDateFormat.parse(o1.getDate()).compareTo(simpleDateFormat.parse(o2.getDate()));
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });
        notifyDataSetChanged();
    }
}
