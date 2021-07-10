package com.ssb.ssbapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.TransactionModel.MoneyTransactionModel;
import com.ssb.ssbapp.TrayModels.TrayTransactionModel;
import com.ssb.ssbapp.ViewHolder.DailyReportViewHolder;

import java.util.ArrayList;

public class DailyReportAdapter extends RecyclerView.Adapter<DailyReportViewHolder> {

    private ArrayList<MoneyTransactionModel> moneyModel;
    private ArrayList<TrayTransactionModel> trayModel;
    private Context mContext;
    private DatabaseReference ref;

    public DailyReportAdapter(ArrayList<MoneyTransactionModel> moneyModel, ArrayList<TrayTransactionModel> trayModel, Context mContext) {
        this.moneyModel = moneyModel;
        this.trayModel = trayModel;
        this.mContext = mContext;
        ref = FirebaseDatabase.getInstance().getReference().child("customers");
    }


    public void updateList(ArrayList<MoneyTransactionModel> moneyModelList, ArrayList<TrayTransactionModel> trayModelList) {
        this.moneyModel.clear();
        this.trayModel.clear();
        this.moneyModel = moneyModelList;
        this.trayModel = trayModelList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DailyReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DailyReportViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.daily_report_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DailyReportViewHolder holder, int position) {


//
//
//
//        }else{
        if (position<moneyModel.size()){
            MoneyTransactionModel model1 = moneyModel.get(position);
            String cid = model1.getCid();
            holder.name.setText(getCustomerName(cid));
            if (model1.getStatus().equals("got")) {
                holder.got.setText(model1.getDescription() + "\nTotal:" + String.valueOf(model1.getTotal()));
            } else {
                holder.gave.setText(model1.getDescription() + "\nTotal:" + String.valueOf(model1.getTotal()));

            }



        }else {

            TrayTransactionModel model =  trayModel.get(position - moneyModel.size());
            String cid = model.getCid();
            holder.name.setText( getCustomerName(cid));
            if (model.getStatus().equals("got")){
                holder.traygot.setText(model.getDescription()+"\nTray:"+String.valueOf(model.getTotal()));
            }else{
                holder.traygave.setText(model.getDescription()+"\nTray:"+String.valueOf(model.getTotal()));

            }


            final String[] name = {""};

            ref.child(cid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {

                        name[0] = (String) snapshot.child("name").getValue();
                        holder.name.setText(name[0]);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }





    }


    @Override
    public int getItemCount() {

        return moneyModel.size()+trayModel.size();

    }


    private String getCustomerName(String cid) {

        final String[] name = {""};


        return name[0];

    }
}
