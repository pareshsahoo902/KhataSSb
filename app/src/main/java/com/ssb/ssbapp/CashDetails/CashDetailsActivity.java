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
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.TransactionModel.MoneyTransactionModel;
import com.ssb.ssbapp.Utils.Constants;
import com.ssb.ssbapp.Utils.SSBBaseActivity;
import com.ssb.ssbapp.Utils.UtilsMethod;
import com.ssb.ssbapp.ViewHolder.CashDetailsViewHolder;
import com.ssb.ssbapp.ViewHolder.MOneyTransactionviewHolder;

public class CashDetailsActivity extends SSBBaseActivity {

    private RecyclerView cashRecycler;
    private DatabaseReference cashRef;
    private LinearLayout expenseReport;
    private FirebaseRecyclerOptions<CashModel> entryoptions;
    private FirebaseRecyclerAdapter<CashModel, CashDetailsViewHolder> entryRecycleradapter;
    private double totalGave, totalGot;
    private TextView totalIn , totalOut , todaysbalance,cashInHand,entryCount,curentDate;
    private Query query;


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
        expenseReport = findViewById(R.id.expenseReport);
        cashRecycler = findViewById(R.id.cashDetailsRecycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        cashRecycler.setLayoutManager(layoutManager);
        cashRecycler.hasFixedSize();

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
        loadEntries();
        calculateTotalBalance();


    }

    private void calculateTotalBalance() {
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                entryCount.setText(String.valueOf(snapshot.getChildrenCount())+" Entry");
                totalGot = 0;
                totalGave = 0;
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                    if (snapshot1.child("status").getValue().equals("got")) {
                        long t = (long) snapshot1.child("total").getValue();
                        totalGot += t;
                    } else {
                        long tg = (long) snapshot1.child("total").getValue();
                        totalGave += tg;
                    }

                }
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
            cashInHand.setText(getCurrencyStr()+ String.valueOf(Math.abs(totalGave - totalGot)));
        } else {
            int num = (int) Math.abs(totalGave - totalGot);
            todaysbalance.setText(getCurrencyStr()+ String.valueOf(num));
            cashInHand.setText(getCurrencyStr()+ String.valueOf(num));

        }
    }

    private void loadEntries() {
        entryoptions = new FirebaseRecyclerOptions.Builder<CashModel>()
                .setQuery(cashRef.orderByChild("kid").equalTo(getLocalSession().getString(Constants.SSB_PREF_KID)), CashModel.class).build();

        entryRecycleradapter = new FirebaseRecyclerAdapter<CashModel, CashDetailsViewHolder>(entryoptions) {
            @Override
            protected void onBindViewHolder(@NonNull CashDetailsViewHolder cashDetailsViewHolder, int i, @NonNull CashModel cashModel) {

                cashDetailsViewHolder.dateName.setText(UtilsMethod.capitalize(cashModel.getCustomerName()) + "\n" + cashModel.getDate());
                if (cashModel.getStatus().equalsIgnoreCase("got")) {
                    cashDetailsViewHolder.inCash.setText(getCurrencyStr() + cashModel.getTotal());
                } else {
                    cashDetailsViewHolder.outCash.setText(getCurrencyStr() + cashModel.getTotal());

                }

            }

            @NonNull
            @Override
            public CashDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new CashDetailsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cash_detail_item, parent, false));
            }
        };

        entryRecycleradapter.startListening();
        cashRecycler.setAdapter(entryRecycleradapter);
    }


}