package com.ssb.ssbapp.salary;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ssb.ssbapp.Admin.CreateEmployee.AddEmploeePayroll;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.Staff.SalaryModel;
import com.ssb.ssbapp.Utils.UtilsMethod;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class CreditSalaryBottomSheet extends BottomSheetDialogFragment {

    private EditText amount, date;
    private Button creditSalary;

    private DatabaseReference slaaryRef;
    private String staffId;
    Calendar myCalendar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.credit_salary_bottom_sheet, container, false);

        Bundle bundle = this.getArguments();
        staffId = bundle.getString("sid");
        amount = view.findViewById(R.id.amount);
        date = view.findViewById(R.id.salaryDate);
        creditSalary = view.findViewById(R.id.creditBtn);

        slaaryRef = FirebaseDatabase.getInstance().getReference().child("staffTransaction");

        date.setText(UtilsMethod.getCurrentDate().substring(0, 10));
        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener dateText = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), dateText, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        creditSalary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (amount.getText().toString().trim() != null && amount.getText().toString().trim().length() > 0) {
                    addSalary();
                } else {
                    amount.setError("Add a amount to credit!");
                }


            }
        });
        return view;
    }


    private void addSalary() {

        String sid = UUID.randomUUID().toString().substring(0, 12);
        SalaryModel model = new SalaryModel(sid, Double.parseDouble(amount.getText().toString()), date.getText().toString(), "gave", staffId);

        slaaryRef.child(sid).setValue(model);
        dismiss();
    }

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        date.setText(sdf.format(myCalendar.getTime()));

    }


}
