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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.Sessions.LocalSession;
import com.ssb.ssbapp.Staff.StaffModel;
import com.ssb.ssbapp.TransactionPage.StaffTransactionPage;
import com.ssb.ssbapp.Utils.UtilsMethod;
import com.ssb.ssbapp.ViewHolder.StaffListItem;
import com.ssb.ssbapp.ViewHolder.StaffViewHolder;

import static com.ssb.ssbapp.Utils.Constants.SSB_EMPLOYEE_DETAILS;
import static com.ssb.ssbapp.Utils.Constants.SSB_PREF_KID;

public class StaffFrag extends Fragment {

    RecyclerView staffRecycler;
    private DatabaseReference staffRef,staffTransaction;
    private double totalGave, totalGot;
    private FirebaseRecyclerOptions<StaffModel> staffoptions;
    private FirebaseRecyclerAdapter<StaffModel, StaffListItem> staffRecycleradapter;
private TextView gavemoney , getmoney;
    public StaffFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_staff, container, false);

        gavemoney = view.findViewById(R.id.gavemoney);
        getmoney = view.findViewById(R.id.getmoney);
        staffRecycler = view.findViewById(R.id.stafffragrecycler);
        staffRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        staffRecycler.hasFixedSize();

        staffRef = FirebaseDatabase.getInstance().getReference().child("staff");
        staffRef.keepSynced(true);

        staffTransaction = FirebaseDatabase.getInstance().getReference().child("staffTransaction");
        staffTransaction.keepSynced(true);

        loadStaffMemebers();
        calculateGotGave();
        return view;
    }



    private void calculateGotGave() {

        staffTransaction.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                totalGot = 0;
                totalGave = 0;
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                        if (snapshot1.child("status").getValue().equals("got")) {
                            long t =  (long)snapshot1.child("amount").getValue();
                            totalGot += t;
                        } else {
                            long tg =  (long)snapshot1.child("amount").getValue();

                            totalGave += tg;
                        }


                }
                calcText();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }





    private void loadStaffMemebers() {
        staffoptions = new FirebaseRecyclerOptions.Builder<StaffModel>()
                .setQuery(staffRef, StaffModel.class).build();

        staffRecycleradapter = new FirebaseRecyclerAdapter<StaffModel, StaffListItem>(staffoptions) {
            @Override
            protected void onBindViewHolder(@NonNull StaffListItem staffViewHolder, int i, @NonNull StaffModel staffModel) {
                staffViewHolder.nameStaff.setText(UtilsMethod.capitalize(staffModel.getName()));
//                staffViewHolder.amount.setText("Salary : "+"₹"+String.valueOf(staffModel.getSalary())+"/Month");

                TextDrawable initial = TextDrawable.builder()
                        .beginConfig().textColor(Color.WHITE)
                        .fontSize(11) /* size in px */
                        .toUpperCase()
                        .width(20)  // width in px
                        .height(20)
                        .endConfig()
                        .buildRound(staffModel.getName().substring(0, 2), R.color.colorPrimaryDark);


                staffViewHolder.stafImage.setImageDrawable(initial);
                staffViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle mBundle = new Bundle();
                        mBundle.putSerializable(SSB_EMPLOYEE_DETAILS,staffModel);
                        startActivity(new Intent(getContext(), StaffTransactionPage.class).putExtras(mBundle));
                    }
                });
            }

            @NonNull
            @Override
            public StaffListItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new StaffListItem(LayoutInflater.from(parent.getContext()).inflate(R.layout.staff_list_item, parent, false));
            }
        };

        staffRecycler.setAdapter(staffRecycleradapter);
        staffRecycleradapter.startListening();

    }


    private void calcText() {

        if (totalGave - totalGot < 0) {
            gavemoney.setText("₹" + String.valueOf(Math.abs(totalGave - totalGot)));
        } else {
            double num = (double) Math.abs(totalGave - totalGot);
            getmoney.setText("₹" + String.valueOf(num));

        }
    }


    @Override
    public void onStart() {
        super.onStart();
        staffRecycleradapter.startListening();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        staffRecycleradapter.stopListening();
    }
}