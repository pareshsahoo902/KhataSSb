package com.ssb.ssbapp.TransactionPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ssb.ssbapp.Adapters.MOneyTransactionAdapter;
import com.ssb.ssbapp.Adapters.TrayTransactionAdapter;
import com.ssb.ssbapp.DataEntry.TrayEntryActivity;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.TransactionModel.MoneyTransactionModel;
import com.ssb.ssbapp.TrayModels.TrayTransactionModel;
import com.ssb.ssbapp.Utils.Constants;
import com.ssb.ssbapp.Utils.SSBBaseActivity;
import com.ssb.ssbapp.Utils.UtilsMethod;
import com.ssb.ssbapp.ViewHolder.MOneyTransactionviewHolder;

import java.util.ArrayList;

public class TrayTransactionPage extends SSBBaseActivity {

    private String name;
    private CardView getBtn, gaveBtn;

    private ArrayList<TrayTransactionModel> model;
    private RecyclerView trayRecycler;
    private DatabaseReference enrtyRef;

    private ImageView pdf , deleteTrayTransacton;
    private TrayTransactionAdapter adapter;

    private TextView geta;
    private int totalGave, totalGot;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tray_transaction_page);
        name = getIntent().getStringExtra("name");
        setToolbar(getApplicationContext(), "  " + UtilsMethod.capitalize(name));
        getBtn = findViewById(R.id.getbtn);
        gaveBtn = findViewById(R.id.gavebtn);
        trayRecycler = findViewById(R.id.trayEntryRecycler);
        geta = findViewById(R.id.geta);
        deleteTrayTransacton = findViewById(R.id.deleteTrayTransacton);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        trayRecycler.setLayoutManager(layoutManager);
        trayRecycler.hasFixedSize();



        getBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TrayTransactionPage.this, TrayEntryActivity.class)
                .putExtra("type","got"));
            }
        });
        gaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TrayTransactionPage.this, TrayEntryActivity.class)
                .putExtra("type","gave"));
            }
        });

        deleteTrayTransacton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(TrayTransactionPage.this)
                        .setTitle(Html.fromHtml("<font color='#03503E'>Delete Transactions ?</font>"))
                        .setMessage("Are you sure you want to delete all tray transaction of this customer ?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                deleteTranINdb(getLocalSession().getString(Constants.SSB_PREF_CID));
                            }
                        })
                        .setNegativeButton("NO", null)
                        .show();
            }
        });

        enrtyRef = FirebaseDatabase.getInstance().getReference().child("trayTransaction");
        enrtyRef.keepSynced(true);

        Query query = enrtyRef.orderByChild("cid").equalTo(getLocalSession().getString(Constants.SSB_PREF_CID));

        model = new ArrayList<>();

        adapter = new TrayTransactionAdapter(model,getApplicationContext());
        trayRecycler.setAdapter(adapter);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                model.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    model.add(snapshot1.getValue(TrayTransactionModel.class));
                }
                adapter.updateList(model);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        calculateTotalBalance();




    }

    private void deleteTranINdb(String uid) {

        Query transactionQuery = enrtyRef.orderByChild("cid").equalTo(uid);

        transactionQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot appleSnapshot : snapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void calculateTotalBalance() {


        enrtyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                totalGot = 0;
                totalGave = 0;
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    if (snapshot1.child("cid").getValue().equals(getLocalSession().getString(Constants.SSB_PREF_CID))) {
                        if (snapshot1.child("status").getValue().equals("got")) {
                            long t = (long) snapshot1.child("total").getValue();
                            totalGot += t;
                        } else {
                            long tg = (long) snapshot1.child("total").getValue();
                            totalGave += tg;
                        }
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

        if (totalGave - totalGot < 0) {
            geta.setTextColor(Color.parseColor("#FFCC0000"));
            geta.setText("You will give : "+getGaveArrowStr() + String.valueOf(Math.abs(totalGave - totalGot)));
        } else {
            geta.setTextColor(Color.parseColor("#FF669900"));
            int num = (int) Math.abs(totalGave - totalGot);
            geta.setText("You will get : "+getGotArrowStr() + String.valueOf(num));

        }
    }



}