package com.ssb.ssbapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.TransactionModel.MoneyTransactionModel;
import com.ssb.ssbapp.TransactionPage.ViewTransactonPage;
import com.ssb.ssbapp.TransactionPage.ViewTrayTransactionPage;
import com.ssb.ssbapp.TrayModels.TrayModelItem;
import com.ssb.ssbapp.TrayModels.TrayTransactionModel;
import com.ssb.ssbapp.Utils.Constants;
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

        if (model.getStatus().equals("got")) {
            holder.gaveText.setVisibility(View.GONE);
            holder.gotText.setVisibility(View.VISIBLE);
            holder.gotText.setText(String.valueOf(model.getTotal()));
        } else {
            holder.gaveText.setVisibility(View.VISIBLE);
            holder.gaveText.setText( String.valueOf(model.getTotal()));
        }

        int balance = calcBalance(position);

        if (balance < 0) {
            holder.balance.setText("Bal:  " + String.valueOf(Math.abs(balance)));
            holder.balance.setBackgroundColor(mContext.getResources().getColor(R.color.liteGreen));
        } else {
            holder.balance.setText("Bal:  " + String.valueOf(balance));
            holder.balance.setBackgroundColor(mContext.getResources().getColor(R.color.litered));

        }




        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.SSB_TRAYTRANSACTION_INTENT, model);

                holder.itemView.getContext().startActivity(new Intent(mContext, ViewTrayTransactionPage.class)
                        .putExtras(bundle)
                );
            }
        });


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


    private int calcBalance(int position){
        double balance =0.0;

        if (position==0){
            balance = modelArrayList.get(position).getTotal();
        }else {
            for (int i =0 ;i<=position;i++){
                if (modelArrayList.get(i).getStatus().equals("gave")){
                    balance-=modelArrayList.get(i).getTotal();
                }else {
                    balance+=modelArrayList.get(i).getTotal();
                }
            }
        }
        return (int) balance;
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
