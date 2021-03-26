package com.ssb.ssbapp.TransactionPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ssb.ssbapp.Customer.CustomerModel;
import com.ssb.ssbapp.DataEntry.MoneyEntryActivity;
import com.ssb.ssbapp.DialogHelper.EntryPickerDialog;
import com.ssb.ssbapp.DialogHelper.ImagePickerDailog;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.TransactionModel.MoneyTransactionModel;
import com.ssb.ssbapp.Utils.Constants;
import com.ssb.ssbapp.Utils.SSBBaseActivity;
import com.ssb.ssbapp.Utils.UtilsMethod;
import com.ssb.ssbapp.ViewHolder.CustomerListViewHolder;
import com.ssb.ssbapp.ViewHolder.MOneyTransactionviewHolder;

public class MoneyTransaction extends SSBBaseActivity {

    private CardView getBtn;
    private String name;
    private RecyclerView entryRecyclerView;

    private DatabaseReference enrtyRef;

    private FirebaseRecyclerOptions<MoneyTransactionModel> entryoptions;
    private FirebaseRecyclerAdapter<MoneyTransactionModel, MOneyTransactionviewHolder> entryRecycleradapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_transaction);

        name = getIntent().getStringExtra("name");

        setToolbar(getApplicationContext(),"  "+ UtilsMethod.capitalize(name));
        getBtn = findViewById(R.id.getbtn);
        entryRecyclerView = findViewById(R.id.entryRecycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        entryRecyclerView.setLayoutManager(layoutManager);
        entryRecyclerView.hasFixedSize();


        enrtyRef = FirebaseDatabase.getInstance().getReference().child("customerTransaction");
        enrtyRef.keepSynced(true);

        getBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(MoneyTransaction.this, MoneyEntryActivity.class));

                EntryPickerDialog dailog = new EntryPickerDialog();
                dailog.show(getSupportFragmentManager(), "Select Entry Type !");
            }
        });

        loadEntries();
    }

    private void loadEntries() {

        entryoptions = new FirebaseRecyclerOptions.Builder<MoneyTransactionModel>()
                .setQuery(enrtyRef.orderByChild("cid").equalTo(getLocalSession().getString(Constants.SSB_PREF_CID)),MoneyTransactionModel.class).build();

        entryRecycleradapter = new FirebaseRecyclerAdapter<MoneyTransactionModel, MOneyTransactionviewHolder>(entryoptions) {
            @Override
            protected void onBindViewHolder(@NonNull MOneyTransactionviewHolder moneyTransactionviewHolder, int i, @NonNull MoneyTransactionModel moneyTransactionModel) {

                moneyTransactionviewHolder.entryText.setText(moneyTransactionModel.getEntriesText());
                moneyTransactionviewHolder.date.setText(moneyTransactionModel.getDate());
                moneyTransactionviewHolder.desc.setText(moneyTransactionModel.getDescription());
                moneyTransactionviewHolder.amountTotal.setText("Amount: "+getCurrencyStr()+String.valueOf( moneyTransactionModel.getTotal()));
                moneyTransactionviewHolder.balance.setText("Balance: "+getCurrencyStr()+String.valueOf( moneyTransactionModel.getTotal()));

                if (moneyTransactionModel.getStatus().equals("got")){
                    moneyTransactionviewHolder.gaveText.setVisibility(View.INVISIBLE);
                    moneyTransactionviewHolder.gotText.setText(getCurrencyStr()+String.valueOf( moneyTransactionModel.getTotal()));
                }
                else {
                    moneyTransactionviewHolder.gotLayout.setVisibility(View.INVISIBLE);
                    moneyTransactionviewHolder.gaveText.setText(getCurrencyStr()+String.valueOf( moneyTransactionModel.getTotal()));
                }

                moneyTransactionviewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }

            @NonNull
            @Override
            public MOneyTransactionviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new MOneyTransactionviewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_item,parent,false));
            }
        };

        entryRecyclerView.setAdapter(entryRecycleradapter);
        entryRecycleradapter.startListening();

    }


}