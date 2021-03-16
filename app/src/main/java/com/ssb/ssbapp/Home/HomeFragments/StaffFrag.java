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

import com.amulyakhare.textdrawable.TextDrawable;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.Staff.StaffModel;
import com.ssb.ssbapp.TransactionPage.StaffTransactionPage;
import com.ssb.ssbapp.Utils.UtilsMethod;
import com.ssb.ssbapp.ViewHolder.StaffListItem;
import com.ssb.ssbapp.ViewHolder.StaffViewHolder;

import static com.ssb.ssbapp.Utils.Constants.SSB_EMPLOYEE_DETAILS;

public class StaffFrag extends Fragment {

    RecyclerView staffRecycler;
    private DatabaseReference staffRef;

    private FirebaseRecyclerOptions<StaffModel> staffoptions;
    private FirebaseRecyclerAdapter<StaffModel, StaffListItem> staffRecycleradapter;

    public StaffFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_staff, container, false);

        staffRecycler = view.findViewById(R.id.stafffragrecycler);
        staffRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        staffRecycler.hasFixedSize();

        staffRef = FirebaseDatabase.getInstance().getReference().child("staff");
        staffRef.keepSynced(true);


        loadStaffMemebers();

        return view;
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


    @Override
    public void onStart() {
        super.onStart();
        staffRecycleradapter.startListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        staffRecycleradapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        staffRecycleradapter.stopListening();
    }
}