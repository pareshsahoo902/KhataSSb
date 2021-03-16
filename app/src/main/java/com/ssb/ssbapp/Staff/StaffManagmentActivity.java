package com.ssb.ssbapp.Staff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amulyakhare.textdrawable.TextDrawable;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.ssb.ssbapp.Admin.CreateEmployee.CreateEmployeeActivity;
import com.ssb.ssbapp.Home.HomeActivity;
import com.ssb.ssbapp.KhataMaster.NewKhata;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.Utils.SSBBaseActivity;
import com.ssb.ssbapp.ViewHolder.StaffViewHolder;

public class StaffManagmentActivity extends SSBBaseActivity {

    private FloatingActionButton addStaffFab;
    private RecyclerView staffRecycler;
    private DatabaseReference staffRef;

    private FirebaseRecyclerOptions<StaffModel> staffoptions;
    private FirebaseRecyclerAdapter<StaffModel, StaffViewHolder> staffRecycleradapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_managment);

        setToolbar(getApplicationContext(),"Manage Staff");

        addStaffFab=findViewById(R.id.staffmanegmentfab);
        staffRecycler=findViewById(R.id.staffmanagementrecycler);
        staffRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        staffRecycler.hasFixedSize();


        staffRef = FirebaseDatabase.getInstance().getReference().child("staff");
        staffRef.keepSynced(true);
        addStaffFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StaffManagmentActivity.this, CreateEmployeeActivity.class));
            }
        });


        showProgress();
        loadStaffMemebers();

    }

    private void loadStaffMemebers() {
        staffoptions = new FirebaseRecyclerOptions.Builder<StaffModel>()
                .setQuery(staffRef,StaffModel.class).build();

        staffRecycleradapter = new FirebaseRecyclerAdapter<StaffModel, StaffViewHolder>(staffoptions) {
            @Override
            protected void onBindViewHolder(@NonNull StaffViewHolder staffViewHolder, int i, @NonNull StaffModel staffModel) {
                TextDrawable initial = TextDrawable.builder()
                        .beginConfig().textColor(Color.WHITE)
                        .fontSize(11) /* size in px */
                        .toUpperCase()
                        .width(20)  // width in px
                        .height(20)
                        .endConfig()
                        .buildRect(staffModel.getName().substring(0, 2), R.color.colorPrimaryDark);

                staffViewHolder.name.setText("Name : "+staffModel.getName());
                staffViewHolder.DOJ.setText("Date of Joining : "+staffModel.getDate_of_joining());
                staffViewHolder.salary.setText("Salary : "+"₹"+String.valueOf(staffModel.getSalary())+"/Month");
                Picasso.with(getApplicationContext()).load(staffModel.getProfile_image()).error(initial)
                        .into(staffViewHolder.staffImage);

                staffViewHolder.viewDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        
                    }
                });
            }

            @NonNull
            @Override
            public StaffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new StaffViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.staff_item,parent,false));
            }
        };

        dismissProgress();

        staffRecycler.setAdapter(staffRecycleradapter);
        staffRecycleradapter.startListening();

    }

    @Override
    protected void onStart() {
        super.onStart();
        staffRecycleradapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        staffRecycleradapter.stopListening();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(StaffManagmentActivity.this, HomeActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}