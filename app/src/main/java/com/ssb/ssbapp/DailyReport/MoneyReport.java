package com.ssb.ssbapp.DailyReport;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ssb.ssbapp.Adapters.MOneyTransactionAdapter;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.Sessions.LocalSession;
import com.ssb.ssbapp.TransactionModel.MoneyTransactionModel;
import com.ssb.ssbapp.Utils.Constants;
import com.ssb.ssbapp.Utils.UtilsMethod;

import java.util.ArrayList;

public class MoneyReport extends Fragment {

    RecyclerView reportRecycler;
    DatabaseReference moneyRef;
    Query moneyQuery;
    MOneyTransactionAdapter adapter;
    String date;
    private DailyReport activity;

    ArrayList<MoneyTransactionModel> moneyTransactionModel, moneyModel;


    public MoneyReport() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = (DailyReport) getActivity();
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_money_report, container, false);
        reportRecycler = v.findViewById(R.id.moneyReportRecycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        reportRecycler.setLayoutManager(layoutManager);
        reportRecycler.hasFixedSize();
        moneyRef = FirebaseDatabase.getInstance().getReference().child("customerTransaction");
        moneyRef.keepSynced(true);

        moneyTransactionModel = new ArrayList<>();
        moneyModel = new ArrayList<>();

        date = UtilsMethod.getCurrentDate().substring(0, 10);
        moneyQuery = moneyRef.orderByChild("kid").equalTo(LocalSession.getString(Constants.SSB_PREF_KID));

        activity.date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (activity.date.getText().toString() != null && !activity.date.getText().toString().equals("")) {
                    date = activity.date.getText().toString();
                    reloadReport();
                }
            }
        });

        adapter = new MOneyTransactionAdapter(moneyModel, getContext(),true);
        reportRecycler.setAdapter(adapter);

        getMoneyTransactions();


        return v;
    }

    private void getMoneyTransactions() {
        moneyQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        moneyTransactionModel.add(snapshot1.getValue(MoneyTransactionModel.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reloadReport();
    }

    private void reloadReport() {
        moneyModel.clear();
        for (MoneyTransactionModel model : moneyTransactionModel) {
            if (model.getDate().substring(0, 10).equals(date)) {
                moneyModel.add(model);
            }
        }
        Toast.makeText(getContext(), "" + moneyModel.size(), Toast.LENGTH_SHORT).show();
        adapter.updateList(moneyModel);

    }


}