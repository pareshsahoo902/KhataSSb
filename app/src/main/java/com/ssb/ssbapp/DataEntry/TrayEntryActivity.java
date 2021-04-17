package com.ssb.ssbapp.DataEntry;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ssb.ssbapp.Model.KhataModel;
import com.ssb.ssbapp.Model.TrayMasterModel;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.Utils.SSBBaseActivity;
import com.ssb.ssbapp.Utils.UtilsMethod;
import com.ssb.ssbapp.ViewHolder.KhataListViewHolder;
import com.ssb.ssbapp.ViewHolder.TrayDetailViewHolder;
import com.ssb.ssbapp.ViewHolder.TrayListViewHolder;

public class TrayEntryActivity extends SSBBaseActivity {

    private String type ="";
    private RecyclerView availableRecycler;

    private DatabaseReference trayRef;
    private FirebaseRecyclerOptions<TrayMasterModel> trayFirebaseRecyclerOptions;
    private FirebaseRecyclerAdapter<TrayMasterModel, TrayDetailViewHolder> trayFirebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tray_entry);

        type = getIntent().getStringExtra("type");

        setToolbar(getApplicationContext(),"Tray Entry"+" "+ UtilsMethod.capitalize(type));

        availableRecycler=findViewById(R.id.availableTrayRecycler);
        availableRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        availableRecycler.hasFixedSize();

        trayRef = FirebaseDatabase.getInstance().getReference().child("tray");
        trayRef.keepSynced(true);


        loadAvailanleTrays();



    }

    private void loadAvailanleTrays() {

        trayFirebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<TrayMasterModel>().setQuery(trayRef, TrayMasterModel.class).build();
        trayFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<TrayMasterModel, TrayDetailViewHolder>(trayFirebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull TrayDetailViewHolder trayListViewHolder, int i, @NonNull TrayMasterModel trayMasterModel) {

                trayListViewHolder.trayName.setText(UtilsMethod.capitalize(trayMasterModel.getName()));
                trayListViewHolder.available.setText("0");
                trayListViewHolder.add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        trayListViewHolder.add.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_close_24));
                        trayListViewHolder.add.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                    }
                });


            }

            @NonNull
            @Override
            public TrayDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new TrayDetailViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.tray_detail_item,parent,false));
            }
        };

        availableRecycler.setAdapter(trayFirebaseRecyclerAdapter);
        trayFirebaseRecyclerAdapter.startListening();


    }
}