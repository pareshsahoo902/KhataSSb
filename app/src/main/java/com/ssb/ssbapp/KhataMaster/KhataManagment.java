package com.ssb.ssbapp.KhataMaster;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ssb.ssbapp.Home.HomeActivity;
import com.ssb.ssbapp.Model.KhataModel;
import com.ssb.ssbapp.Model.TrayMasterModel;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.SplashScreen.SplashActivity;
import com.ssb.ssbapp.Utils.SSBBaseActivity;
import com.ssb.ssbapp.Utils.UtilsMethod;
import com.ssb.ssbapp.ViewHolder.KhataListViewHolder;
import com.ssb.ssbapp.ViewHolder.TrayListViewHolder;

import java.util.HashMap;
import java.util.UUID;

import static com.ssb.ssbapp.Utils.Constants.SSB_PREF_BRANCH;
import static com.ssb.ssbapp.Utils.Constants.SSB_PREF_KID;

public class KhataManagment extends SSBBaseActivity {
    private EditText khata_text;
    private Button add_btn;
    private DatabaseReference khataRef, custref, transaxctionref;
    private RecyclerView khataMasterRecyler;
    private TextView countKhata ;


    private FirebaseRecyclerOptions<KhataModel> khataOption;
    private FirebaseRecyclerAdapter<KhataModel, KhataListViewHolder> khataRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khata_managment);

        setToolbar(getApplicationContext(), "Manage Khata");


        khata_text = findViewById(R.id.khata_nametext);
        add_btn = findViewById(R.id.addKhataBtn);
        countKhata = findViewById(R.id.countText);
        khataMasterRecyler = findViewById(R.id.khataMasterRecycler);

        khataMasterRecyler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        khataMasterRecyler.hasFixedSize();

        khataRef = FirebaseDatabase.getInstance().getReference().child("khata");
        custref = FirebaseDatabase.getInstance().getReference().child("customers");
        transaxctionref = FirebaseDatabase.getInstance().getReference().child("customerTransaction");
        khataRef.keepSynced(true);


        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndUpdateKhata();
            }
        });


        loadKhata();

    }

    private void loadKhata() {

        khataOption = new FirebaseRecyclerOptions.Builder<KhataModel>().setQuery(khataRef, KhataModel.class).build();
        khataRecyclerAdapter = new FirebaseRecyclerAdapter<KhataModel, KhataListViewHolder>(khataOption) {
            @Override
            protected void onBindViewHolder(@NonNull KhataListViewHolder trayListViewHolder, int i, @NonNull KhataModel trayMasterModel) {

                countKhata.setText("Total :"+String.valueOf(khataRecyclerAdapter.getItemCount()));

                trayListViewHolder.khataName.setText(UtilsMethod.capitalize(trayMasterModel.getName()));
                if (trayMasterModel.getKid().equals(getLocalSession().getString(SSB_PREF_KID))) {
                    trayListViewHolder.isActiveBtn.setVisibility(View.VISIBLE);
                } else {
                    trayListViewHolder.isActiveBtn.setVisibility(View.INVISIBLE);

                }
                trayListViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        getLocalSession().putString(SSB_PREF_KID, trayMasterModel.getKid());
                        getLocalSession().putString(SSB_PREF_BRANCH, trayMasterModel.getName());
                        startActivity(new Intent(KhataManagment.this, SplashActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                    }
                });

                trayListViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        new AlertDialog.Builder(KhataManagment.this)
                                .setTitle(Html.fromHtml("<font color='#03503E'>Delete Khata</font>"))
                                .setMessage("Are you sure you want to delete all data attached to this khata ?")
                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Continue with delete operation
                                        deletetransactionInDB(trayMasterModel.getKid());
                                        khataRef.child(trayMasterModel.getKid()).removeValue();
                                        deleteCustomerInDB(trayMasterModel.getKid());
                                    }
                                })
                                .setNegativeButton("NO", null)
                                .show();

                        return true;
                    }
                });

            }

            @NonNull
            @Override
            public KhataListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new KhataListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.khata_item, parent, false));
            }
        };


        khataMasterRecyler.setAdapter(khataRecyclerAdapter);
        khataRecyclerAdapter.startListening();

    }

    private void deletetransactionInDB(String kid) {
        Query transactionQuery = transaxctionref.orderByChild("kid").equalTo(kid);

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

    private void deleteCustomerInDB(String kid) {
        Query custQuery = custref.orderByChild("kid").equalTo(kid);

        custQuery.addValueEventListener(new ValueEventListener() {
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


    private void validateAndUpdateKhata() {

        if (TextUtils.isEmpty(khata_text.getText().toString().trim())) {
            khata_text.setError("Tray Name cannot be empty!");
        } else {

            String kid = UUID.randomUUID().toString().substring(0, 6);

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("name", khata_text.getText().toString().trim());
            hashMap.put("active", false);
            hashMap.put("company", "ssb");
            hashMap.put("kid", kid);
            hashMap.put("created_at", UtilsMethod.getCurrentDate());


            khataRef.child(kid).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    khata_text.setText("");
                    khataRecyclerAdapter.notifyDataSetChanged();
                }
            });

            khata_text.setText("");

        }

    }
}