package com.ssb.ssbapp.TrayMaster;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ssb.ssbapp.Customer.CustomerModel;
import com.ssb.ssbapp.Model.TrayMasterModel;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.TrayModels.TrayModelItem;
import com.ssb.ssbapp.TrayModels.TrayTransactionModel;
import com.ssb.ssbapp.Utils.Constants;
import com.ssb.ssbapp.Utils.SSBBaseActivity;
import com.ssb.ssbapp.Utils.UtilsMethod;
import com.ssb.ssbapp.ViewHolder.TrayStockDetailviewholder;

import java.util.ArrayList;
import java.util.List;

public class TrayStockDetails extends SSBBaseActivity {

    private RecyclerView trayMasterRecyler;
    private DatabaseReference trayRef , custRef;
    private FirebaseRecyclerOptions<CustomerModel> trayOption;
    private FirebaseRecyclerAdapter<CustomerModel, TrayStockDetailviewholder> trayRecyclerAdapter;
    private List<TrayTransactionModel> modelItemList;

    TrayMasterModel trayMasterModel;
    String tid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tray_stock_details);

        trayMasterModel = (TrayMasterModel) getIntent().getSerializableExtra("Tray_Intent");
        tid = trayMasterModel.getTid();
        setToolbar(getApplicationContext(),trayMasterModel.getName()+" : Pending Trays");
        trayMasterRecyler = findViewById(R.id.traystockCurstomerRecycelr);

        trayMasterRecyler.setLayoutManager(new LinearLayoutManager(TrayStockDetails.this,LinearLayoutManager.VERTICAL,false));
        trayMasterRecyler.hasFixedSize();
        trayRef = FirebaseDatabase.getInstance().getReference().child("trayTransaction");
        custRef = FirebaseDatabase.getInstance().getReference().child("customers");
        trayRef.keepSynced(true);
        custRef.keepSynced(true);

        Query query = trayRef.orderByChild("kid").equalTo(getLocalSession().getString(Constants.SSB_PREF_KID));

        modelItemList = new ArrayList<>();

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                modelItemList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    modelItemList.add(snapshot1.getValue(TrayTransactionModel.class));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                finish();
            }

        });

        loadRecycler();

    }

    private void loadRecycler() {

        trayOption = new FirebaseRecyclerOptions.Builder<CustomerModel>().setQuery(custRef.orderByChild("kid").equalTo(getLocalSession().getString(Constants.SSB_PREF_KID)), CustomerModel.class).build();
        trayRecyclerAdapter = new FirebaseRecyclerAdapter<CustomerModel, TrayStockDetailviewholder>(trayOption) {
            @Override
            protected void onBindViewHolder(@NonNull TrayStockDetailviewholder customerHolder, int i, @NonNull CustomerModel customerModel) {
                int pending = getPendingTray(customerModel.getUid());
                    customerHolder.name.setText(UtilsMethod.capitalize(customerModel.getName()));
                    customerHolder.trayPending.setText(String.valueOf(pending)+" Trays");


            }

            @NonNull
            @Override
            public TrayStockDetailviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new TrayStockDetailviewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_detail_item,parent,false));
            }
        };


        trayMasterRecyler.setAdapter(trayRecyclerAdapter);
        trayRecyclerAdapter.startListening();

    }

    private int getPendingTray(String uid) {

        int trays =0;

        for (TrayTransactionModel model : modelItemList){
            if (model.getCid().equals(uid) && model.getStatus().equals("gave")){
                for (TrayModelItem item : model.getModelItemArrayList()){
                    if (item.getId().equals(tid)){
                        trays+=item.getTotalCount();
                    }
                }

            }
        }

        return trays;
    }


}