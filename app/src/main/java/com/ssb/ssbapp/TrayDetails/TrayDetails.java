package com.ssb.ssbapp.TrayDetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ssb.ssbapp.Adapters.TrayDetailAdapter;
import com.ssb.ssbapp.CashDetails.CashModel;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.TrayModels.TrayModelItem;
import com.ssb.ssbapp.Utils.Constants;
import com.ssb.ssbapp.Utils.SSBBaseActivity;
import com.ssb.ssbapp.Utils.UtilsMethod;
import com.ssb.ssbapp.ViewHolder.CashDetailsViewHolder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class TrayDetails extends SSBBaseActivity {

    private RecyclerView cashRecycler;
    private DatabaseReference cashRef;
    private FirebaseRecyclerOptions<TrayDetailModel> entryoptions;
    private FirebaseRecyclerAdapter<TrayDetailModel, CashDetailsViewHolder> entryRecycleradapter;
    private long totalGave, totalGot ,yesgot, yesgave;
    private Query query;
    private LinearLayout salesReport;
    private ArrayList<TrayDetailModel> modelArrayList;
    private TextView totalIn ,totalTray, totalOut , todaysbalance,cashInHand,entryCount,curentDate;
    private String yesterdayDate;
    private TrayDetailAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tray_details);

        setToolbar(getApplicationContext(), "Tray Details");


        totalIn = findViewById(R.id.totalIn);
        totalOut = findViewById(R.id.totalOut);
        todaysbalance = findViewById(R.id.todaysbalance);
        totalTray = findViewById(R.id.totalTray);
        cashInHand = findViewById(R.id.cashInHand);
        entryCount = findViewById(R.id.entryCount);
        salesReport = findViewById(R.id.salesReport);
        curentDate = findViewById(R.id.curentDate);
        cashRecycler = findViewById(R.id.trayDetailsRecycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        cashRecycler.setLayoutManager(layoutManager);
        cashRecycler.hasFixedSize();

        modelArrayList = new ArrayList<>();
        cashRef = FirebaseDatabase.getInstance().getReference().child("trayDetails");
        cashRef.keepSynced(true);
        adapter = new TrayDetailAdapter(modelArrayList);

        getYesterdayDate();


        salesReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TrayDetails.this,TraySalesAndEpense.class));
            }
        });

        curentDate.setText(UtilsMethod.getCurrentDate().substring(0,10));

        query = cashRef.orderByChild("kid").equalTo(getLocalSession().getString(Constants.SSB_PREF_KID));
        loadEntries();
        calculateTotalBalance();

        cashRecycler.setAdapter(adapter);


    }


    private void getYesterdayDate() {
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        cal.add(Calendar.DATE, -1);
        yesterdayDate= dateFormat.format(cal.getTime());

    }

    private void calculateTotalBalance() {


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                totalGot = 0;
                totalGave = 0;
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                    String date = (String)snapshot1.child("date").getValue();

                    if (date.substring(0,10).equals(UtilsMethod.getCurrentDate().substring(0,10))){
                        if (snapshot1.child("status").getValue().equals("got")) {
                            long t =   (long)snapshot1.child("total").getValue();
                            totalGot += t;
                        } else {
                            long tg =  (long)snapshot1.child("total").getValue();
                            totalGave += tg;
                        }

                        modelArrayList.add(snapshot1.getValue(TrayDetailModel.class));

                    }


                        if (snapshot1.child("status").getValue().equals("got")) {
                            long t =   (long)snapshot1.child("total").getValue();
                            yesgot += t;
                        } else {
                            long tg =  (long)snapshot1.child("total").getValue();
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
        totalIn.setText(String.valueOf(totalGot));
        totalOut.setText(String.valueOf(totalGave));

        if (totalGave - totalGot < 0) {
            todaysbalance.setText( String.valueOf(Math.abs(totalGave - totalGot)));
        } else {
            long num =  Math.abs(totalGave - totalGot);
            todaysbalance.setText( String.valueOf(num));

        }


        if (yesgave - yesgot < 0) {
            totalTray.setText(String.valueOf(Math.abs(yesgave - yesgot)));
        } else {
            long num =  Math.abs(yesgave - yesgot);
            totalTray.setText( String.valueOf(num));

        }

        cashInHand.setText(getCurrencyStr()+String.valueOf(Integer.parseInt(totalTray.getText().toString()) - Integer.parseInt(todaysbalance.getText().toString())));




    }



    private void loadEntries() {

    }

}