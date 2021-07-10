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
import com.ssb.ssbapp.Adapters.TrayTransactionAdapter;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.Sessions.LocalSession;
import com.ssb.ssbapp.TransactionModel.MoneyTransactionModel;
import com.ssb.ssbapp.TrayModels.TrayTransactionModel;
import com.ssb.ssbapp.Utils.Constants;
import com.ssb.ssbapp.Utils.UtilsMethod;

import java.util.ArrayList;

public class TrayReport extends Fragment{


    RecyclerView reportRecycler;
    DatabaseReference trayRef ;
    Query trayQuery ;
    TrayTransactionAdapter adapter;
    String date;
    ArrayList<TrayTransactionModel> trayTransactionModel,trayModel;
    private DailyReport activity;



    public TrayReport() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        activity =(DailyReport) getActivity();

        View v =inflater.inflate(R.layout.fragment_tray_report, container, false);
        reportRecycler = v.findViewById(R.id.trayReportRecycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        reportRecycler.setLayoutManager(layoutManager);
        reportRecycler.hasFixedSize();
        trayRef = FirebaseDatabase.getInstance().getReference().child("trayTransaction");
        trayRef.keepSynced(true);

        trayTransactionModel=new ArrayList<>();
        trayModel=new ArrayList<>();

        date= UtilsMethod.getCurrentDate().substring(0,10);
        trayQuery = trayRef.orderByChild("kid").equalTo(LocalSession.getString(Constants.SSB_PREF_KID));


        adapter= new TrayTransactionAdapter(trayModel,getContext(),true);
        reportRecycler.setAdapter(adapter);

        activity.date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (activity.date.getText().toString()!=null && !activity.date.getText().toString().equals("")){
                    date = activity.date.getText().toString();
                    reloadData();
                }
            }
        });

        getTrayDetails();
        return v;
    }

    private void getTrayDetails() {
        trayQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    for (DataSnapshot snapshot1 : snapshot.getChildren()){
                        trayTransactionModel.add(snapshot1.getValue(TrayTransactionModel.class));
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Toast.makeText(getContext(), ""+trayTransactionModel.size(), Toast.LENGTH_SHORT).show();
        reloadData();
    }

    private void reloadData() {
        trayModel.clear();
        for (TrayTransactionModel model : trayTransactionModel){
            if (model.getDate().substring(0,10).equals(date)){
                trayModel.add(model);
            }
        }
        Toast.makeText(getContext(), ""+trayModel.size(), Toast.LENGTH_SHORT).show();

        adapter.updateList(trayModel);
    }


}