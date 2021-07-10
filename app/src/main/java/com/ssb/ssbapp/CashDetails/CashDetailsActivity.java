package com.ssb.ssbapp.CashDetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ssb.ssbapp.Adapters.CashDetailsAdapter;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.TransactionModel.MoneyTransactionModel;
import com.ssb.ssbapp.Utils.Constants;
import com.ssb.ssbapp.Utils.SSBBaseActivity;
import com.ssb.ssbapp.Utils.UtilsMethod;
import com.ssb.ssbapp.ViewHolder.CashDetailsViewHolder;
import com.ssb.ssbapp.ViewHolder.MOneyTransactionviewHolder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CashDetailsActivity extends SSBBaseActivity {

    private RecyclerView cashRecycler;
    private DatabaseReference cashRef;
    private LinearLayout expenseReport;
    private double totalGave, totalGot,yesgot , yesgave;
    private ArrayList<CashModel> modelArrayList;
    private TextView totalIn , totalOut ,totalCash, todaysbalance,cashInHand,entryCount,curentDate;
    private Query query;
    private CashDetailsAdapter adapter;
    private String yesterdayDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_details);

        setToolbar(getApplicationContext(), "CashBook");

        totalIn = findViewById(R.id.totalIn);
        totalOut = findViewById(R.id.totalOut);
        todaysbalance = findViewById(R.id.todaysbalance);
        cashInHand = findViewById(R.id.cashInHand);
        entryCount = findViewById(R.id.entryCount);
        curentDate = findViewById(R.id.curentDate);
        totalCash = findViewById(R.id.totalCash);
        expenseReport = findViewById(R.id.expenseReport);
        cashRecycler = findViewById(R.id.cashDetailsRecycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        cashRecycler.setLayoutManager(layoutManager);
        cashRecycler.hasFixedSize();

        modelArrayList = new ArrayList<>();
        cashRef = FirebaseDatabase.getInstance().getReference().child("cashDetails");
        cashRef.keepSynced(true);

        expenseReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CashDetailsActivity.this,SalesAndExpenseActivity.class));
            }
        });
        query = cashRef.orderByChild("kid").equalTo(getLocalSession().getString(Constants.SSB_PREF_KID));

        curentDate.setText(UtilsMethod.getCurrentDate().substring(0,10));


        adapter = new CashDetailsAdapter(modelArrayList);
        calculateTotalBalance();

        cashRecycler.setAdapter(adapter);

    }


    private void calculateTotalBalance() {
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelArrayList.clear();
                totalGot = 0;
                totalGave = 0;
                yesgave = 0;
                yesgot = 0;
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    if (snapshot1.child("date").getValue().equals(UtilsMethod.getCurrentDate().substring(0,10))){
                        if (snapshot1.child("status").getValue().equals("got")) {
                            double t =  Double.parseDouble((String) snapshot1.child("total").getValue());
                            totalGot += t;
                        } else {
                            double tg = Double.parseDouble((String) snapshot1.child("total").getValue());
                            totalGave += tg;
                        }

                        modelArrayList.add(snapshot1.getValue(CashModel.class));

                    }


                        if (snapshot1.child("status").getValue().equals("got")) {
                            double t =  Double.parseDouble((String) snapshot1.child("total").getValue());
                            yesgot += t;
                        } else {
                            double tg = Double.parseDouble((String) snapshot1.child("total").getValue());
                            yesgave += tg;
                        }



                }
                entryCount.setText(String.valueOf(modelArrayList.size())+" Entry");
                adapter.updateList(modelArrayList);
                calcText();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
    }

    private void calcText() {
        totalIn.setText(getCurrencyStr()+String.valueOf(totalGot));
        totalOut.setText(getCurrencyStr()+String.valueOf(totalGave));

        if (totalGave - totalGot < 0) {
            todaysbalance.setText(getCurrencyStr()+ String.valueOf(Math.abs(totalGave - totalGot)));
        } else {
            double num =  Math.abs(totalGave - totalGot);
            todaysbalance.setText(getCurrencyStr()+ String.valueOf(num));

        }


        if (yesgave - yesgot < 0) {
            totalCash.setText(getCurrencyStr()+ String.valueOf(Math.abs(yesgave - yesgot)));
        } else {
            double num =  Math.abs(yesgave - yesgot);
            totalCash.setText(getCurrencyStr()+ String.valueOf(num));

        }


        cashInHand.setText(getCurrencyStr()+String.valueOf(Double.parseDouble(totalCash.getText().toString().substring(1)) - Double.parseDouble(todaysbalance.getText().toString().substring(1))));

    }



}