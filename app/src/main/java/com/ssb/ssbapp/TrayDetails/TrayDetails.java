package com.ssb.ssbapp.TrayDetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ssb.ssbapp.CashDetails.CashModel;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.TrayModels.TrayModelItem;
import com.ssb.ssbapp.Utils.Constants;
import com.ssb.ssbapp.Utils.SSBBaseActivity;
import com.ssb.ssbapp.Utils.UtilsMethod;
import com.ssb.ssbapp.ViewHolder.CashDetailsViewHolder;

import java.util.ArrayList;

public class TrayDetails extends SSBBaseActivity {

    private RecyclerView cashRecycler;
    private DatabaseReference cashRef;
    private FirebaseRecyclerOptions<TrayDetailModel> entryoptions;
    private FirebaseRecyclerAdapter<TrayDetailModel, CashDetailsViewHolder> entryRecycleradapter;
    private int totalGave, totalGot;
    private Query query;
    private TextView totalIn , totalOut , todaysbalance,cashInHand,entryCount,curentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tray_details);

        setToolbar(getApplicationContext(), "Tray Details");



        totalIn = findViewById(R.id.totalIn);
        totalOut = findViewById(R.id.totalOut);
        todaysbalance = findViewById(R.id.todaysbalance);
        cashInHand = findViewById(R.id.cashInHand);
        entryCount = findViewById(R.id.entryCount);
        curentDate = findViewById(R.id.curentDate);
        cashRecycler = findViewById(R.id.trayDetailsRecycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        cashRecycler.setLayoutManager(layoutManager);
        cashRecycler.hasFixedSize();

        cashRef = FirebaseDatabase.getInstance().getReference().child("trayDetails");
        cashRef.keepSynced(true);


        curentDate.setText(UtilsMethod.getCurrentDate().substring(0,10));

        query = cashRef.orderByChild("kid").equalTo(getLocalSession().getString(Constants.SSB_PREF_KID));
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
        totalIn.setText(String.valueOf(totalGot));
        totalOut.setText(String.valueOf(totalGave));

        if (totalGave - totalGot < 0) {
            todaysbalance.setText(String.valueOf(Math.abs(totalGave - totalGot)));
            cashInHand.setText(String.valueOf(Math.abs(totalGave - totalGot)));
        } else {
            int num = (int) Math.abs(totalGave - totalGot);
            todaysbalance.setText(String.valueOf(num));
            cashInHand.setText(String.valueOf(num));

        }

    }

    public String getDetailText(ArrayList<TrayModelItem> trayModelItems){
        String detailStr = "";

        for (TrayModelItem item:trayModelItems){
            detailStr+=item.getName()+": "+item.getTotalCount()+"\n";
        }

        return detailStr;
    }

    private void loadEntries() {
        entryoptions = new FirebaseRecyclerOptions.Builder<TrayDetailModel>()
                .setQuery(cashRef.orderByChild("kid").equalTo(getLocalSession().getString(Constants.SSB_PREF_KID)), TrayDetailModel.class).build();

        entryRecycleradapter = new FirebaseRecyclerAdapter<TrayDetailModel, CashDetailsViewHolder>(entryoptions) {
            @Override
            protected void onBindViewHolder(@NonNull CashDetailsViewHolder cashDetailsViewHolder, int i, @NonNull TrayDetailModel cashModel) {

                cashDetailsViewHolder.traydet.setText(getDetailText(cashModel.getModelItemArrayList()));
                cashDetailsViewHolder.traydet.setVisibility(View.VISIBLE);
                cashDetailsViewHolder.dateName.setText(UtilsMethod.capitalize(cashModel.getCustomerName()) + "\n" + cashModel.getDate());
                if (cashModel.getStatus().equalsIgnoreCase("got")) {
                    cashDetailsViewHolder.inCash.setText(String.valueOf(cashModel.getTotal()));
                } else {
                    cashDetailsViewHolder.outCash.setText( String.valueOf(cashModel.getTotal()));

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