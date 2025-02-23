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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.TransactionModel.MoneyTransactionModel;
import com.ssb.ssbapp.TransactionPage.ViewTransactonPage;
import com.ssb.ssbapp.Utils.Constants;
import com.ssb.ssbapp.ViewHolder.MOneyTransactionviewHolder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MOneyTransactionAdapter extends RecyclerView.Adapter<MOneyTransactionviewHolder> {

    private ArrayList<MoneyTransactionModel> modelArrayList;
    private Context mCOntext;
    private boolean isName;

    public MOneyTransactionAdapter(ArrayList<MoneyTransactionModel> modelArrayList, Context mCOntext, boolean isName) {
        this.mCOntext = mCOntext;
        this.isName=isName;
       updateList(modelArrayList);
    }

    @NonNull
    @Override
    public MOneyTransactionviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MOneyTransactionviewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MOneyTransactionviewHolder moneyTransactionviewHolder, int position) {
        MoneyTransactionModel moneyTransactionModel = (MoneyTransactionModel)modelArrayList.get(position);

        if (isName){
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            ref.child("customers").child(moneyTransactionModel.getCid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        moneyTransactionviewHolder.name.setText((String)snapshot.child("name").getValue());
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            moneyTransactionviewHolder.name.setVisibility(View.VISIBLE);
        }

        if (moneyTransactionModel.getStatus().equals("got")) {
            moneyTransactionviewHolder.gotText.setText(getCurrencyStr() + String.valueOf(moneyTransactionModel.getTotal()));
            moneyTransactionviewHolder.gotText.setVisibility(View.VISIBLE);
        }else {
            moneyTransactionviewHolder.gotText.setVisibility(View.GONE);
            moneyTransactionviewHolder.gaveText.setText(getCurrencyStr() + String.valueOf(moneyTransactionModel.getTotal()));
            moneyTransactionviewHolder.gaveText.setVisibility(View.VISIBLE);
        }


        if (!moneyTransactionModel.description.equals("")) {
            moneyTransactionviewHolder.desc.setText(moneyTransactionModel.getDescription());
            moneyTransactionviewHolder.desc.setVisibility(View.VISIBLE);
        }

        moneyTransactionviewHolder.entryText.setText(moneyTransactionModel.getEntriesText());
        moneyTransactionviewHolder.date.setText("Date: "+moneyTransactionModel.getDate().substring(0,10));
        moneyTransactionviewHolder.amountTotal.setText("Amt:" + getCurrencyStr() + String.valueOf(moneyTransactionModel.getTotal()));


        double balance = calcBalance(position);
        if (balance < 0) {
            moneyTransactionviewHolder.balance.setText("Bal:" + getCurrencyStr() + String.valueOf(Math.abs(balance)));
            moneyTransactionviewHolder.balance.setBackgroundColor(mCOntext.getResources().getColor(R.color.liteGreen));
        } else {
            moneyTransactionviewHolder.balance.setText("Bal:" + getCurrencyStr() + String.valueOf(balance));
            moneyTransactionviewHolder.balance.setBackgroundColor(mCOntext.getResources().getColor(R.color.litered));

        }
        if (moneyTransactionModel.getImageurl().length() > 0) {

            Picasso.with(mCOntext).load(moneyTransactionModel.getImageurl()).into(moneyTransactionviewHolder.billIMage);
            moneyTransactionviewHolder.billIMage.setVisibility(View.VISIBLE);
        }

        if (isName){

            moneyTransactionviewHolder.amountTotal.setVisibility(View.GONE);
            moneyTransactionviewHolder.balance.setVisibility(View.GONE);
            moneyTransactionviewHolder.billIMage.setVisibility(View.GONE);
        }


        moneyTransactionviewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.SSB_MONEYTRANSACTION_INTENT, moneyTransactionModel);

                moneyTransactionviewHolder.itemView.getContext().startActivity(new Intent(mCOntext, ViewTransactonPage.class)
                        .putExtras(bundle)
                );
            }
        });



    }

    public void updateList(ArrayList<MoneyTransactionModel> modelArrayList){
        this.modelArrayList=modelArrayList;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a"); //your own date format
        Collections.sort(this.modelArrayList, new Comparator<MoneyTransactionModel>() {
            @Override
            public int compare(MoneyTransactionModel o1, MoneyTransactionModel o2) {
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


    private double calcBalance(int position){
        double balance =0.0;

        if (position==0){
            balance = Double.parseDouble(modelArrayList.get(position).getTotal());
            return balance;
        }else {
            for (int i =0 ;i<=position;i++){
                if (modelArrayList.get(i).getStatus().equals("got")){
                    balance+=Double.parseDouble(modelArrayList.get(i).getTotal());
                }else {
                    balance-=Double.parseDouble(modelArrayList.get(i).getTotal());
                }
                Log.v("paresh",String.valueOf(balance));
            }
        }
        return balance;
    }


    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }


    public String getCurrencyStr() {

        String outputStr = "₹";

        return outputStr;
    }
}
