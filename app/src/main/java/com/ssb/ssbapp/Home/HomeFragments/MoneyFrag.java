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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ssb.ssbapp.Customer.CustomerModel;
import com.ssb.ssbapp.DialogHelper.AddCustomerBottomSheet;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.Staff.StaffModel;
import com.ssb.ssbapp.TransactionPage.MoneyTransaction;
import com.ssb.ssbapp.Utils.UtilsMethod;
import com.ssb.ssbapp.ViewHolder.CustomerListViewHolder;
import com.ssb.ssbapp.ViewHolder.StaffListItem;

public class MoneyFrag extends Fragment {

    private FloatingActionButton moneyFab;
    private RecyclerView custRecycler;
    private DatabaseReference custRef;

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
        custRef = FirebaseDatabase.getInstance().getReference().child("customers");

        moneyFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAddCutmoerDailog();
            }
        });
        loadCustomers();
        return view;
    }

    private void loadCustomers() {
        custoptions = new FirebaseRecyclerOptions.Builder<CustomerModel>()
                .setQuery(custRef, CustomerModel.class).build();

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

                        startActivity(new Intent(getContext(), MoneyTransaction.class)
                        .putExtra("name",custModel.getName()));
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


    @Override
    public void onStart() {
        super.onStart();

        custRecycleradapter.startListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        custRecycleradapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        custRecycleradapter.stopListening();
    }
}