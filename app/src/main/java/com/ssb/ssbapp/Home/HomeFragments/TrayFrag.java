package com.ssb.ssbapp.Home.HomeFragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ssb.ssbapp.Customer.CustomerModel;
import com.ssb.ssbapp.DialogHelper.AddCustomerBottomSheet;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.Sessions.LocalSession;
import com.ssb.ssbapp.TransactionPage.TrayTransactionPage;
import com.ssb.ssbapp.Utils.Constants;
import com.ssb.ssbapp.Utils.UtilsMethod;
import com.ssb.ssbapp.ViewHolder.CustomerListViewHolder;

import static com.ssb.ssbapp.Utils.Constants.SSB_PREF_KID;

public class TrayFrag extends Fragment {

    private FloatingActionButton trayFab;
    private RecyclerView custRecycler;
    private DatabaseReference custRef,customeEntryrRef;
    private int totalGave, totalGot;
    private TextView gavemoney , getmoney;

    private FirebaseRecyclerOptions<CustomerModel> custoptions;
    private FirebaseRecyclerAdapter<CustomerModel, CustomerListViewHolder> custRecycleradapter;

    public TrayFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_tray, container, false);

        gavemoney = view.findViewById(R.id.gavetray);
        getmoney = view.findViewById(R.id.gottray);
        custRecycler = view.findViewById(R.id.trayfragrecycler);
        custRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        custRecycler.hasFixedSize();

        custRef = FirebaseDatabase.getInstance().getReference().child("customers");

        trayFab = view.findViewById(R.id.floatingActionButtontray);

        trayFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAddCutmoerDailog();

            }
        });
        customeEntryrRef = FirebaseDatabase.getInstance().getReference().child("trayTransaction");
        customeEntryrRef.keepSynced(true);

        loadCustomers();
        calculateGotGave();

        return view;
    }


    private void loadCustomers() {



        custoptions = new FirebaseRecyclerOptions.Builder<CustomerModel>()
                .setQuery(custRef.orderByChild("kid").equalTo(LocalSession.getString(SSB_PREF_KID)),CustomerModel.class).build();

        custRecycleradapter = new FirebaseRecyclerAdapter<CustomerModel, CustomerListViewHolder>(custoptions) {
            @Override
            protected void onBindViewHolder(@NonNull CustomerListViewHolder viewHolder, int i, @NonNull CustomerModel custModel) {
                viewHolder.nameCust.setText(UtilsMethod.capitalize(custModel.getName()));
//                viewHolder.amount.setText("Salary : "+"₹"+String.valueOf(custModel.get())+"/Month");

                TextDrawable initial = TextDrawable.builder()
                        .beginConfig().textColor(Color.WHITE)
                        .fontSize(11) /* size in px */
                        .toUpperCase()
                        .width(20)  // width in px
                        .height(20)
                        .endConfig()
                        .buildRound(custModel.getName().substring(0, 2), R.color.colorPrimaryDark);


                viewHolder.custImage.setImageDrawable(initial);
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LocalSession.putString(Constants.SSB_PREF_CID,custModel.getUid());
                        startActivity(new Intent(getContext(), TrayTransactionPage.class)
                                .putExtra("name",custModel.getName()));

                    }
                });

                customeEntryrRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int tGO = 0;
                        int tGV = 0;
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            if (snapshot1.child("cid").getValue().equals(custModel.getUid())) {
                                if (snapshot1.child("status").getValue().equals("got")) {
                                    long t = (long) snapshot1.child("total").getValue();
                                    tGO += t;
                                } else {
                                    long tg = (long) snapshot1.child("total").getValue();
                                    tGV += tg;
                                }
                            }

                        }

                        if (tGV - tGO < 0) {
                            viewHolder.amount.setTextColor(Color.parseColor("#FFCC0000"));
                            viewHolder.status.setTextColor(Color.parseColor("#FFCC0000"));
                            viewHolder.status.setText("You'll give");
                            viewHolder.amount.setText(String.valueOf(Math.abs(tGV - tGO))+" Trays");
                        } else {
                            viewHolder.amount.setTextColor(Color.parseColor("#FF669900"));
                            viewHolder.status.setTextColor(Color.parseColor("#FF669900"));
                            int num = (int) Math.abs(tGV - tGO);
                            viewHolder.status.setText("You'll get");
                            viewHolder.amount.setText(String.valueOf(Math.abs(num))+" Trays");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



            }

            @NonNull
            @Override
            public CustomerListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new CustomerListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_list_item, parent, false));
            }
        };

        custRecycler.setAdapter(custRecycleradapter);
        custRecycleradapter.startListening();

    }


    private void getAddCutmoerDailog() {

        AddCustomerBottomSheet subscribeBottomSheetDailog = new AddCustomerBottomSheet();
        subscribeBottomSheetDailog.show(getFragmentManager(), "add customer");
    }


    private void calculateGotGave() {

        customeEntryrRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                totalGot = 0;
                totalGave = 0;
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    if (snapshot1.child("kid").getValue().equals(LocalSession.getString(SSB_PREF_KID))) {
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
            gavemoney.setText("↑ "+String.valueOf(Math.abs(totalGave - totalGot)));
        } else {
            int num = (int) Math.abs(totalGave - totalGot);
            getmoney.setText("↓ "+ String.valueOf(num));

        }
    }




    @Override
    public void onStart() {
        super.onStart();
        custRecycleradapter.startListening();
    }

    @Override
    public void onResume() {
        calculateGotGave();
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        custRecycleradapter.stopListening();
    }
}