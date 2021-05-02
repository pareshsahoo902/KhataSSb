package com.ssb.ssbapp.TransactionPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.squareup.picasso.Picasso;
import com.ssb.ssbapp.Adapters.MOneyTransactionAdapter;
import com.ssb.ssbapp.Customer.CustomerModel;
import com.ssb.ssbapp.DataEntry.MoneyEntryActivity;
import com.ssb.ssbapp.DialogHelper.EntryPickerDialog;
import com.ssb.ssbapp.DialogHelper.ImagePickerDailog;
import com.ssb.ssbapp.Home.HomeActivity;
import com.ssb.ssbapp.Login.LoginActivity;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.SplashScreen.SplashActivity;
import com.ssb.ssbapp.TransactionModel.MoneyTransactionModel;
import com.ssb.ssbapp.Utils.Constants;
import com.ssb.ssbapp.Utils.SSBBaseActivity;
import com.ssb.ssbapp.Utils.UtilsMethod;
import com.ssb.ssbapp.ViewHolder.CustomerListViewHolder;
import com.ssb.ssbapp.ViewHolder.MOneyTransactionviewHolder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class MoneyTransaction extends SSBBaseActivity {

    private CardView getBtn, gaveBtn;
    private String name;
    private ImageView pdfGenerate , deleteTransaction;
    private RecyclerView entryRecyclerView;
    private DatabaseReference enrtyRef;
    private boolean isGave;
    private FirebaseRecyclerOptions<MoneyTransactionModel> entryoptions;
    private FirebaseRecyclerAdapter<MoneyTransactionModel, MOneyTransactionviewHolder> entryRecycleradapter;
    private TextView geta;
    private double totalGave, totalGot;
    MOneyTransactionAdapter adapter;
    private ArrayList<MoneyTransactionModel> model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_transaction);

        name = getIntent().getStringExtra("name");

        setToolbar(getApplicationContext(), "  " + UtilsMethod.capitalize(name));
        getBtn = findViewById(R.id.getbtn);
        gaveBtn = findViewById(R.id.gavebtn);
        geta = findViewById(R.id.geta);
        deleteTransaction = findViewById(R.id.deleteTransacton);
        pdfGenerate = findViewById(R.id.getPdf);
        entryRecyclerView = findViewById(R.id.entryRecycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        entryRecyclerView.setLayoutManager(layoutManager);
        entryRecyclerView.hasFixedSize();

        enrtyRef = FirebaseDatabase.getInstance().getReference().child("customerTransaction");
        enrtyRef.keepSynced(true);


        Query query = enrtyRef.orderByChild("cid").equalTo(getLocalSession().getString(Constants.SSB_PREF_CID));

        model = new ArrayList<>();


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                model.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    model.add(snapshot1.getValue(MoneyTransactionModel.class));
                }
                adapter.updateList(model);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        deleteTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(MoneyTransaction.this)
                        .setTitle(Html.fromHtml("<font color='#03503E'>Delete Transactions ?</font>"))
                        .setMessage("Are you sure you want to delete all transaction of this customer ?")
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


        getBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(MoneyTransaction.this, MoneyEntryActivity.class));

                EntryPickerDialog dailog = new EntryPickerDialog();
                Bundle bundle = new Bundle();

                bundle.putString("transaction_type", "got");
                bundle.putDouble("allGave", totalGave);
                bundle.putDouble("allGot", totalGot);
                dailog.setArguments(bundle);
                dailog.show(getSupportFragmentManager(), "Select Entry Type !");
            }
        });


        gaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(MoneyTransaction.this, MoneyEntryActivity.class));

                EntryPickerDialog dailog = new EntryPickerDialog();
                Bundle bundle = new Bundle();

                bundle.putString("transaction_type", "gave");
                bundle.putDouble("allGave", totalGave);
                bundle.putDouble("allGot", totalGot);
                dailog.setArguments(bundle);
                dailog.show(getSupportFragmentManager(), "Select Entry Type !");
            }
        });


        calculateTotalBalance();

        loadEntryInRecycler();
//        loadEntries();
    }

    private void deleteTranINdb(String uid) {


        Query transactionQuery = enrtyRef.orderByChild("cid").equalTo(uid);

        transactionQuery.addValueEventListener(

        new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot appleSnapshot : snapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();

                }
                transactionQuery.removeEventListener(this);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void loadEntryInRecycler() {

        adapter = new MOneyTransactionAdapter(model, getApplicationContext());
        entryRecyclerView.setAdapter(adapter);


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
            geta.setText("You will give : ₹" + String.valueOf(Math.abs(totalGave - totalGot)));
        } else {
            geta.setTextColor(Color.parseColor("#FF669900"));
            int num = (int) Math.abs(totalGave - totalGot);
            geta.setText("You will get : ₹" + String.valueOf(num));

        }
    }

    private void loadEntries() {

        entryoptions = new FirebaseRecyclerOptions.Builder<MoneyTransactionModel>()
                .setQuery(enrtyRef.orderByChild("cid").equalTo(getLocalSession().getString(Constants.SSB_PREF_CID)), MoneyTransactionModel.class).build();

        entryRecycleradapter = new FirebaseRecyclerAdapter<MoneyTransactionModel, MOneyTransactionviewHolder>(entryoptions) {
            @Override
            protected void onBindViewHolder(@NonNull MOneyTransactionviewHolder moneyTransactionviewHolder, int i, @NonNull MoneyTransactionModel moneyTransactionModel) {

                if (!moneyTransactionModel.description.equals("")) {
                    moneyTransactionviewHolder.desc.setText(moneyTransactionModel.getDescription());
                    moneyTransactionviewHolder.desc.setVisibility(View.VISIBLE);
                }

                moneyTransactionviewHolder.entryText.setText(moneyTransactionModel.getEntriesText());
                moneyTransactionviewHolder.date.setText(moneyTransactionModel.getDate());
                moneyTransactionviewHolder.amountTotal.setText("Amt:" + getCurrencyStr() + String.valueOf(moneyTransactionModel.getTotal()));
                if (moneyTransactionModel.getBalance() < 0) {
                    moneyTransactionviewHolder.balance.setText("Bal:" + getCurrencyStr() + String.valueOf(moneyTransactionModel.getBalance()));
                    moneyTransactionviewHolder.balance.setBackgroundColor(getResources().getColor(R.color.liteGreen));
                } else {
                    moneyTransactionviewHolder.balance.setText("Bal:" + getCurrencyStr() + String.valueOf(moneyTransactionModel.getBalance()));
                    moneyTransactionviewHolder.balance.setBackgroundColor(getResources().getColor(R.color.litered));

                }

                if (moneyTransactionModel.getImageurl().length() > 0) {

                    Picasso.with(getApplicationContext()).load(moneyTransactionModel.getImageurl()).into(moneyTransactionviewHolder.billIMage);
                    moneyTransactionviewHolder.billIMage.setVisibility(View.VISIBLE);
                }

                if (moneyTransactionModel.getStatus().equals("got")) {
                    moneyTransactionviewHolder.gaveText.setVisibility(View.INVISIBLE);
                    moneyTransactionviewHolder.gotText.setText(getCurrencyStr() + String.valueOf(moneyTransactionModel.getTotal()));
                } else {
                    moneyTransactionviewHolder.gotLayout.setVisibility(View.INVISIBLE);
                    moneyTransactionviewHolder.gaveText.setVisibility(View.VISIBLE);
                    moneyTransactionviewHolder.gaveText.setText(getCurrencyStr() + String.valueOf(moneyTransactionModel.getTotal()));
                }

                moneyTransactionviewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constants.SSB_MONEYTRANSACTION_INTENT, moneyTransactionModel);

                        startActivity(new Intent(MoneyTransaction.this, ViewTransactonPage.class)
                                .putExtras(bundle)
                        );
                    }
                });
            }

            @NonNull
            @Override
            public MOneyTransactionviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new MOneyTransactionviewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_item, parent, false));
            }
        };

        entryRecyclerView.setAdapter(entryRecycleradapter);
        entryRecycleradapter.startListening();

    }

    private void getSortedArray(ArrayList<MoneyTransactionModel> arraylist) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy"); //your own date format
        Collections.sort(model, new Comparator<MoneyTransactionModel>() {
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

    }

}