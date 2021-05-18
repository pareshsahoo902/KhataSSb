package com.ssb.ssbapp.CashDetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ssb.ssbapp.Adapters.CashDetailsAdapter;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.Utils.Constants;
import com.ssb.ssbapp.Utils.SSBBaseActivity;
import com.ssb.ssbapp.Utils.UtilsMethod;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.ssb.ssbapp.Utils.UtilsMethod.isBetween;

public class SalesAndExpenseActivity extends SSBBaseActivity {

    Button date_minimal, date_maximal;
    RecyclerView dateSaleRecyler;
    ArrayList<CashModel> totalCash;
    ArrayList<CashModel> modelArrayList;
    private DatabaseReference cashRef;
    Date minimalDate, maximalDate;
    Query query;
    TextView totalCashText;
    DateFormat dateFormat;
    Calendar calendar2,calendar1;
    CashDetailsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_and_expense);
        setToolbar(getApplicationContext(), "Cashbook Report");

        date_minimal = findViewById(R.id.fromDate);
        date_maximal = findViewById(R.id.toDate);
        totalCashText = findViewById(R.id.totalCash);
        dateSaleRecyler = findViewById(R.id.dateSaleRecyler);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        dateSaleRecyler.setLayoutManager(layoutManager);
        dateSaleRecyler.hasFixedSize();
        cashRef = FirebaseDatabase.getInstance().getReference().child("cashDetails");
        cashRef.keepSynced(true);
        dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        totalCash = new ArrayList<>();
        modelArrayList = new ArrayList<>();
        query = cashRef.orderByChild("kid").equalTo(getLocalSession().getString(Constants.SSB_PREF_KID));
        getminimalDate();
        date_maximal.setText(UtilsMethod.getCurrentDate().substring(0, 10));
        try {
            maximalDate = dateFormat.parse(UtilsMethod.getCurrentDate().substring(0, 10));
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, ""+e.toString(), Toast.LENGTH_SHORT).show();
        }
        calendar2 = Calendar.getInstance();
        calendar1 = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener dateSetListener2 =  new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar2.set(year,monthOfYear,dayOfMonth);
                maximalDate = calendar2.getTime();

                String myFormat = "dd-MM-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
                date_maximal.setText(sdf.format(calendar2.getTime()));
            }
        };

        final DatePickerDialog.OnDateSetListener dateSetListener1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar1.set(year,monthOfYear,dayOfMonth);
                minimalDate = calendar1.getTime();
                String myFormat = "dd-MM-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
                date_minimal.setText(sdf.format(calendar1.getTime()));
            }
        };
        adapter= new CashDetailsAdapter(modelArrayList);
        dateSaleRecyler.setAdapter(adapter);

        date_minimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(SalesAndExpenseActivity.this,
                        dateSetListener1,calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH),
                        calendar1.get(Calendar.DAY_OF_MONTH)).show();


            }
        });
        date_maximal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(SalesAndExpenseActivity.this,
                        dateSetListener2,calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH),
                        calendar2.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        date_minimal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (minimalDate.before(maximalDate)){
                    loadRecyclerFromDate();
                }
            }
        });

        date_maximal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (maximalDate.after(minimalDate)){
                    loadRecyclerFromDate();
                }
            }
        });

        getAllCashList();

    }

    private void loadRecyclerFromDate(){
        modelArrayList.clear();
        SimpleDateFormat dateFormat =new SimpleDateFormat("dd-MM-yyyy");
        for (CashModel model : totalCash){
            try {
                if (isBetween(dateFormat.parse(model.getDate()),minimalDate,maximalDate)){
                    modelArrayList.add(model);
                }
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(SalesAndExpenseActivity.this, ""+e.toString(), Toast.LENGTH_SHORT).show();

            }


            loadCashDetals(modelArrayList);
            adapter.updateList(modelArrayList);
        }
    }

    private void loadCashDetals(ArrayList<CashModel> modelArrayList) {

        double totalGot = 0;
        double totalGave = 0;

        for (CashModel model : modelArrayList){
            if ( model.getStatus().equals("got")) {
                double t =  Double.parseDouble((String) model.getTotal());
                totalGot += t;
            } else {
                double tg = Double.parseDouble((String) model.getTotal());
                totalGave += tg;
            }
        }

        if (totalGave - totalGot < 0) {
            totalCashText.setText("Total:  "+getCurrencyStr()+ String.valueOf(Math.abs(totalGave - totalGot)));
        } else {
            double num =  Math.abs(totalGave - totalGot);
            totalCashText.setText("Total:  "+getCurrencyStr()+ String.valueOf(num));

        }


    }

    private void getAllCashList() {
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                    totalCash.add(snapshot1.getValue(CashModel.class));
                }

                loadRecyclerFromDate();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                finish();
            }
        });
    }


    private void getminimalDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        minimalDate = cal.getTime();
        date_minimal.setText(dateFormat.format(cal.getTime()));

    }


}