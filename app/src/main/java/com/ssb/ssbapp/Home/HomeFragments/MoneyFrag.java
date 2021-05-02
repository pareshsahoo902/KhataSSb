package com.ssb.ssbapp.Home.HomeFragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ssb.ssbapp.Customer.CustomerModel;
import com.ssb.ssbapp.DialogHelper.AddCustomerBottomSheet;
import com.ssb.ssbapp.KhataMaster.KhataManagment;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.Sessions.LocalSession;
import com.ssb.ssbapp.Staff.StaffModel;
import com.ssb.ssbapp.TransactionPage.MoneyTransaction;
import com.ssb.ssbapp.Utils.Constants;
import com.ssb.ssbapp.Utils.UtilsMethod;
import com.ssb.ssbapp.ViewHolder.CustomerListViewHolder;
import com.ssb.ssbapp.ViewHolder.StaffListItem;

import static com.ssb.ssbapp.Utils.Constants.SSB_PREF_KID;

public class MoneyFrag extends Fragment {

    private FloatingActionButton moneyFab;
    private RecyclerView custRecycler;
    private DatabaseReference custRef,entryRef , customeEntryrRef,trayTranRef;
    private EditText searchBar;
    private double totalGave, totalGot;
    private String searchText;
    private TextView gavemoney , getmoney ,countMoney;
    private FirebaseRecyclerOptions<CustomerModel> custoptions;
    private FirebaseRecyclerAdapter<CustomerModel, CustomerListViewHolder> custRecycleradapter;

    public MoneyFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_money, container, false);
        custRecycler = view.findViewById(R.id.moneyrecycler);


        custRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        custRecycler.hasFixedSize();
        moneyFab = view.findViewById(R.id.addcustfloatingActionButton);
        searchBar = view.findViewById(R.id.searchhome);
        gavemoney = view.findViewById(R.id.gavemoney);
        getmoney = view.findViewById(R.id.getmoney);
        countMoney = view.findViewById(R.id.countText);
        custRef = FirebaseDatabase.getInstance().getReference().child("customers");
        custRef.keepSynced(true);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                searchText = searchBar.getText().toString().trim();

            }
        });

        entryRef = FirebaseDatabase.getInstance().getReference().child("customerTransaction");
        trayTranRef = FirebaseDatabase.getInstance().getReference().child("trayTransaction");
        entryRef.keepSynced(true);

        moneyFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAddCutmoerDailog();
            }
        });
        loadCustomers();
        calculateGotGave();
        return view;
    }

    private void calculateGotGave() {

        entryRef.addValueEventListener(new ValueEventListener() {
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
            gavemoney.setText("₹" + String.valueOf(Math.abs(totalGave - totalGot)));
        } else {
            int num = (int) Math.abs(totalGave - totalGot);
            getmoney.setText("₹" + String.valueOf(num));

        }
    }


    private void loadCustomers() {

        customeEntryrRef = FirebaseDatabase.getInstance().getReference().child("customerTransaction");
        customeEntryrRef.keepSynced(true);

        custoptions = new FirebaseRecyclerOptions.Builder<CustomerModel>()
//                .setQuery(  custRef.orderByChild("name").startAt(searchText),CustomerModel.class)
                .setQuery(custRef.orderByChild("kid").equalTo(LocalSession.getString(SSB_PREF_KID)), CustomerModel.class).build();

        custRecycleradapter = new FirebaseRecyclerAdapter<CustomerModel, CustomerListViewHolder>(custoptions) {
            @Override
            protected void onBindViewHolder(@NonNull CustomerListViewHolder viewHolder, int i, @NonNull CustomerModel custModel) {
                countMoney.setText(String.valueOf(custRecycleradapter.getItemCount())+" Customers");

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

                viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        new AlertDialog.Builder(getContext())
                                .setTitle(Html.fromHtml("<font color='#03503E'>Delete Customer</font>"))
                                .setMessage("Are you sure you want to delete all entries of this customer ?")
                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Continue with delete operation
                                        deletetransactionInDB(custModel.getUid());
                                        custRef.child(custModel.getUid()).removeValue();
                                        countMoney.setText(String.valueOf(custRecycleradapter.getItemCount()-1)+" Customers");

                                    }
                                })
                                .setNegativeButton("NO", null)
                                .show();

                        return true;
                    }
                });
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        startActivity(new Intent(getContext(), MoneyTransaction.class)
                        .putExtra("name",custModel.getName()));
                        LocalSession.putString(Constants.SSB_PREF_CID,custModel.getUid());
                    }
                });

                customeEntryrRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        double tGO = 0;
                        double tGV = 0;
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
                            viewHolder.amount.setText("₹" + String.valueOf(Math.abs(tGV - tGO)));
                        } else {
                            viewHolder.amount.setTextColor(Color.parseColor("#FF669900"));
                            viewHolder.status.setTextColor(Color.parseColor("#FF669900"));
                            int num = (int) Math.abs(tGV - tGO);
                            viewHolder.status.setText("You'll get");
                            viewHolder.amount.setText("₹" + String.valueOf(Math.abs(num)));

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

    private void deletetransactionInDB(String uid) {

        Query transactionQuery = customeEntryrRef.orderByChild("cid").equalTo(uid);
        Query ttrayQuery = trayTranRef.orderByChild("cid").equalTo(uid);

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

        ttrayQuery.addValueEventListener(new ValueEventListener() {
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


    private void getAddCutmoerDailog() {

        AddCustomerBottomSheet subscribeBottomSheetDailog = new AddCustomerBottomSheet();
        subscribeBottomSheetDailog.show(getFragmentManager(), "add customer");
    }


    @Override
    public void onStart() {
        super.onStart();
        calculateGotGave();
        custRecycleradapter.startListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        calculateGotGave();
    }

    @Override
    public void onStop() {
        super.onStop();
        custRecycleradapter.stopListening();
    }
}