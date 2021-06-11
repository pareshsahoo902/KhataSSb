package com.ssb.ssbapp.TransactionPage;

import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.ssb.ssbapp.CashDetails.CashDetailsActivity;
import com.ssb.ssbapp.Model.KhataModel;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.Staff.SalaryModel;
import com.ssb.ssbapp.Staff.StaffModel;
import com.ssb.ssbapp.Utils.SSBBaseActivity;
import com.ssb.ssbapp.Utils.UtilsMethod;
import com.ssb.ssbapp.ViewHolder.SalaryTransactionViewHolder;
import com.ssb.ssbapp.ViewHolder.StaffViewHolder;
import com.ssb.ssbapp.report.ReportActivity;
import com.ssb.ssbapp.salary.CreditSalaryBottomSheet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.ssb.ssbapp.Utils.Constants.SSB_EMPLOYEE_DETAILS;

public class StaffTransactionPage extends SSBBaseActivity {

    StaffModel mStaff;
    ImageView staffImage;
    TextView name, DOJ, salary;
    Button viewDetails;
    CardView creditBtn;
    boolean isPayDay=false;
    String payDate = "";
    private RecyclerView staffTransactionRecycler;
    DatabaseReference staffRef;
    private FirebaseRecyclerOptions<SalaryModel> staffoptions;
    private FirebaseRecyclerAdapter<SalaryModel, SalaryTransactionViewHolder> staffRecycleradapter;


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
        creditBtn = findViewById(R.id.creditBtn);

        staffTransactionRecycler = findViewById(R.id.entryRecycler);

        staffTransactionRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        staffTransactionRecycler.hasFixedSize();

        staffRef = FirebaseDatabase.getInstance().getReference().child("staffTransaction");


        viewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        creditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreditSalaryBottomSheet bottomSheet = new CreditSalaryBottomSheet();
                bottomSheet.show(getSupportFragmentManager(), "Credit Salary");
            }
        });

        loadStaffDetails();
        checkCreditSalaryStatus();
        loadTransaction();
    }

    private void checkCreditSalaryStatus() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
       

        try {
            Date doj = dateFormat.parse(mStaff.getLast_salary());
            payDate=dateFormat.format( addOneMonth(doj));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        

       
        staffRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot snapshot1:snapshot.getChildren()){
                        if (snapshot1.getKey().equals(payDate+"Salary")){
                            isPayDay=false;
                            
                        }else {
                            isPayDay=true;
                        }
                    }
                }
                
                if (isPayDay){
                    try {
                        if (UtilsMethod.getCurrentDate().substring(0,10).equals(payDate) || dateFormat.parse(payDate).before(dateFormat.parse(UtilsMethod.getCurrentDate().substring(0,10)))){
                            giveSalary();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                        Toast.makeText(StaffTransactionPage.this, "Some Error Occur!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }

           
        });
        
    }

    private void giveSalary() {

        SalaryModel model = new SalaryModel(payDate+"Salary",(double) (mStaff.getSalary()),UtilsMethod.getCurrentDate(),"got");

        staffRef.child(payDate+"Salary").setValue(model);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("staff").child(mStaff.getAadhar()).child("last_salary").setValue(payDate);

    }

    private void loadTransaction() {

        staffoptions = new FirebaseRecyclerOptions.Builder<SalaryModel>().setQuery(staffRef, SalaryModel.class).build();
        staffRecycleradapter = new FirebaseRecyclerAdapter<SalaryModel, SalaryTransactionViewHolder>(staffoptions) {
            @Override
            protected void onBindViewHolder(@NonNull SalaryTransactionViewHolder salaryTransactionViewHolder, int i, @NonNull SalaryModel salaryModel) {

                salaryTransactionViewHolder.date.setText(salaryModel.getDate().substring(0, 10));
                if (salaryModel.getStatus().equals("gave")) {
                    salaryTransactionViewHolder.got.setVisibility(View.INVISIBLE);
                    salaryTransactionViewHolder.gave.setText(getCurrencyStr() + String.valueOf(salaryModel.getAmount()));
                } else {
                    salaryTransactionViewHolder.gave.setVisibility(View.INVISIBLE);

                    salaryTransactionViewHolder.got.setText(getCurrencyStr() + String.valueOf(salaryModel.getAmount()));

                }
            }

            @NonNull

            @Override
            public SalaryTransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new SalaryTransactionViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.salary_item, parent, false));
            }
        };

        staffTransactionRecycler.setAdapter(staffRecycleradapter);
        staffRecycleradapter.startListening();


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

        name.setText("Employee Name : " + UtilsMethod.capitalize(mStaff.getName()));
        DOJ.setText("Date of joining : " + mStaff.getDate_of_joining());
        salary.setText("Salary : " + "₹" + String.valueOf(mStaff.getSalary()) + "/Month");
        Picasso.with(getApplicationContext()).load(mStaff.getProfile_image()).error(initial).into(staffImage);

    }


    public static Date addOneMonth(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 1);
        return cal.getTime();
    }
}


