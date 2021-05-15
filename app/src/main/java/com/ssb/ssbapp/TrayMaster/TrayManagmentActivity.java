package com.ssb.ssbapp.TrayMaster;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ssb.ssbapp.Home.HomeActivity;
import com.ssb.ssbapp.KhataMaster.NewKhata;
import com.ssb.ssbapp.Model.TrayMasterModel;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.Staff.StaffModel;
import com.ssb.ssbapp.Utils.SSBBaseActivity;
import com.ssb.ssbapp.Utils.UtilsMethod;
import com.ssb.ssbapp.ViewHolder.StaffViewHolder;
import com.ssb.ssbapp.ViewHolder.TrayListViewHolder;

import java.util.HashMap;
import java.util.UUID;

public class TrayManagmentActivity extends SSBBaseActivity {

    private EditText tray_text;
    private Button add_btn;
    private DatabaseReference trayRef;
    private RecyclerView trayMasterRecyler;
    private TextView countTrayText;

    private FirebaseRecyclerOptions<TrayMasterModel> trayOption;
    private FirebaseRecyclerAdapter<TrayMasterModel, TrayListViewHolder> trayRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tray_managment);

        setToolbar(getApplicationContext(), "Manage Tray");

        tray_text = findViewById(R.id.tray_nametext);
        add_btn = findViewById(R.id.addTrayBtn);
        countTrayText = findViewById(R.id.countText);
        trayMasterRecyler = findViewById(R.id.traymasterrecyler);

        trayMasterRecyler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        trayMasterRecyler.hasFixedSize();

        trayRef = FirebaseDatabase.getInstance().getReference().child("tray");
        trayRef.keepSynced(true);

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndUpdateTray();
            }
        });


        loadTrays();

    }

    private void loadTrays() {

        trayOption = new FirebaseRecyclerOptions.Builder<TrayMasterModel>().setQuery(trayRef, TrayMasterModel.class).build();
        trayRecyclerAdapter = new FirebaseRecyclerAdapter<TrayMasterModel, TrayListViewHolder>(trayOption) {
            @Override
            protected void onBindViewHolder(@NonNull TrayListViewHolder trayListViewHolder, int i, @NonNull TrayMasterModel trayMasterModel) {
                countTrayText.setText("Total :"+String.valueOf(trayRecyclerAdapter.getItemCount()));

                trayListViewHolder.trayName.setText(UtilsMethod.capitalize(trayMasterModel.getName()));

                trayListViewHolder.deleteTray.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        trayRef.child(trayMasterModel.getTid()).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                trayRecyclerAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                });

            }

            @NonNull
            @Override
            public TrayListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new TrayListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.tray_item,parent,false));
            }
        };


        trayMasterRecyler.setAdapter(trayRecyclerAdapter);
        trayRecyclerAdapter.startListening();

    }

    private void validateAndUpdateTray() {
        if (TextUtils.isEmpty(tray_text.getText().toString().trim())) {
            tray_text.setError("Tray Name cannot be empty!");
        } else {

            String tid = UUID.randomUUID().toString().substring(0, 4);

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("name", tray_text.getText().toString().trim());
            hashMap.put("isActive", true);
            hashMap.put("company", "ssb");
            hashMap.put("tid", tid);
            hashMap.put("created_at", UtilsMethod.getCurrentDate());


            trayRef.child(tid).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    tray_text.setText("");
                    trayRecyclerAdapter.notifyDataSetChanged();
                }
            });

            tray_text.setText("");

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        trayRecyclerAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        trayRecyclerAdapter.stopListening();
    }

}