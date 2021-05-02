package com.ssb.ssbapp.TrayMaster;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ssb.ssbapp.Model.TrayMasterModel;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.TrayDetails.TrayDetailModel;
import com.ssb.ssbapp.TrayModels.TrayModelItem;
import com.ssb.ssbapp.TrayModels.TrayTransactionModel;
import com.ssb.ssbapp.Utils.Constants;
import com.ssb.ssbapp.Utils.SSBBaseActivity;
import com.ssb.ssbapp.Utils.UtilsMethod;
import com.ssb.ssbapp.ViewHolder.TrayListViewHolder;

import java.util.ArrayList;
import java.util.List;

public class TrayStock extends SSBBaseActivity {


    private RecyclerView trayMasterRecyler;
    private DatabaseReference trayRef;
    private FirebaseRecyclerOptions<TrayMasterModel> trayOption;
    private FirebaseRecyclerAdapter<TrayMasterModel, TrayStockViewHolder> trayRecyclerAdapter;
    private List<TrayDetailModel> modelItemList;
    //TODO if frommtraydetails change to tray details
    private DatabaseReference trayTransactionRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tray_stock);
        setToolbar(getApplicationContext(),"Tray Stock");


        trayMasterRecyler = findViewById(R.id.traystockRecycelr);

        trayMasterRecyler.setLayoutManager(new LinearLayoutManager(TrayStock.this,LinearLayoutManager.VERTICAL,false));
        trayMasterRecyler.hasFixedSize();
        trayRef = FirebaseDatabase.getInstance().getReference().child("tray");
        trayRef.keepSynced(true);


        //TODO if frommtraydetails change to tray details
        trayTransactionRef = FirebaseDatabase.getInstance().getReference().child("trayDetails");
        trayTransactionRef.keepSynced(true);

        Query query = trayTransactionRef.orderByChild("kid").equalTo(getLocalSession().getString(Constants.SSB_PREF_KID));

        modelItemList = new ArrayList<>();

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                modelItemList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    modelItemList.add(snapshot1.getValue(TrayDetailModel.class));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        loadTrays();




    }


    private List<Integer> getAvailableTray(String trayID) {
        List<Integer> trays = new ArrayList<>();
        int got=0;
        int gave=0;
        int available = 0;

        for (TrayDetailModel model:modelItemList){

            if (model.getStatus().equals("gave")){
                for (TrayModelItem item:model.getModelItemArrayList()){

                    if (item.getId().equals(trayID)){
                        gave+=item.getTotalCount();
                    }
                }
            }else{
                for (TrayModelItem item:model.getModelItemArrayList()){

                    if (item.getId().equals(trayID)){
                        got+=item.getTotalCount();
                    }
                }
            }

        }

        trays.add(got);
        trays.add(gave);
        trays.add(got-gave);

        return trays;
    }

    private void loadTrays() {

        trayOption = new FirebaseRecyclerOptions.Builder<TrayMasterModel>().setQuery(trayRef, TrayMasterModel.class).build();
        trayRecyclerAdapter = new FirebaseRecyclerAdapter<TrayMasterModel, TrayStockViewHolder>(trayOption) {
            @Override
            protected void onBindViewHolder(@NonNull TrayStockViewHolder trayListViewHolder, int i, @NonNull TrayMasterModel trayMasterModel) {

                List<Integer> trayDetails = getAvailableTray(trayMasterModel.getTid());
                trayListViewHolder.trayName.setText(UtilsMethod.capitalize(trayMasterModel.getName()));

                if (trayDetails!=null){
                    trayListViewHolder.got.setText(String.valueOf(trayDetails.get(0)));
                    trayListViewHolder.gave.setText(String.valueOf(trayDetails.get(1)));
                    trayListViewHolder.avaiable.setText(String.valueOf(trayDetails.get(2)));
                }


            }

            @NonNull
            @Override
            public TrayStockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new TrayStockViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.tray_stock_item,parent,false));
            }
        };


        trayMasterRecyler.setAdapter(trayRecyclerAdapter);
        trayRecyclerAdapter.startListening();
    }
}