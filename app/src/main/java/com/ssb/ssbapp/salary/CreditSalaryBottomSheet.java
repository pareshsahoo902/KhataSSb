package com.ssb.ssbapp.salary;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.Staff.SalaryModel;
import com.ssb.ssbapp.Utils.UtilsMethod;

import java.util.UUID;

public class CreditSalaryBottomSheet extends BottomSheetDialogFragment {

    private EditText amount;
    private Button creditSalary;

    private DatabaseReference slaaryRef;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.credit_salary_bottom_sheet, container, false);

        amount = view.findViewById(R.id.amount);
        creditSalary = view.findViewById(R.id.creditBtn);

        slaaryRef = FirebaseDatabase.getInstance().getReference().child("staffTransaction");

        creditSalary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (amount.getText().toString().trim()!=null && amount.getText().toString().trim().length()>0){
                    addSalary();
                }else{
                    amount.setError("Add a amount to credit!");
                }


            }
        });
        return view;
    }

    private void addSalary() {

        String sid = UUID.randomUUID().toString().substring(0,12);
        SalaryModel model = new SalaryModel( sid,Double.parseDouble(amount.getText().toString()), UtilsMethod.getCurrentDate(),"gave");

        slaaryRef.child(sid).setValue(model);
        dismiss();
    }


}
