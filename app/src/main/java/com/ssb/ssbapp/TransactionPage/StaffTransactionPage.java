package com.ssb.ssbapp.TransactionPage;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.squareup.picasso.Picasso;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.Staff.StaffModel;
import com.ssb.ssbapp.Utils.SSBBaseActivity;
import com.ssb.ssbapp.Utils.UtilsMethod;

import static com.ssb.ssbapp.Utils.Constants.SSB_EMPLOYEE_DETAILS;

public class StaffTransactionPage extends SSBBaseActivity {

    StaffModel mStaff;
    ImageView staffImage;
    TextView name, DOJ, salary;
    Button viewDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_transaction_page);
        setToolbar(getApplicationContext(), "Credit Salary");

        mStaff = (StaffModel) getIntent().getSerializableExtra(SSB_EMPLOYEE_DETAILS);
        staffImage = findViewById(R.id.staff_image);
        name = findViewById(R.id.staffNameTitle);
        DOJ = findViewById(R.id.staffDateOfJoining);
        salary = findViewById(R.id.staffSalary);
        viewDetails = findViewById(R.id.viewStaffdetails);

        viewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        loadStaffDetails();

    }

    private void loadStaffDetails() {

        TextDrawable initial = TextDrawable.builder()
                .beginConfig().textColor(Color.WHITE)
                .fontSize(11) /* size in px */
                .toUpperCase()
                .width(20)  // width in px
                .height(20)
                .endConfig()
                .buildRect(mStaff.getName().substring(0, 2), R.color.colorPrimaryDark);

        name.setText("Employee Name : "+ UtilsMethod.capitalize(mStaff.getName()));
        DOJ.setText("Date of joining : "+mStaff.getDate_of_joining());
        salary.setText("Salary : "+"₹"+String.valueOf(mStaff.getSalary())+"/Month");
        Picasso.with(getApplicationContext()).load(mStaff.getProfile_image()).error(initial).into(staffImage);

    }
}