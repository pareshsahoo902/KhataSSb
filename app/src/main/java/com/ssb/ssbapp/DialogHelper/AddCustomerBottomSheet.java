package com.ssb.ssbapp.DialogHelper;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ssb.ssbapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class AddCustomerBottomSheet extends BottomSheetDialogFragment {

    private EditText name, phone;
    private Button addCust;

    private DatabaseReference custRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_customer_bottom_sheet, container, false);

        addCust = view.findViewById(R.id.addCustBtn);
        phone = view.findViewById(R.id.cust_phonetext);
        name = view.findViewById(R.id.cust_nametext);

        addCust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateCust();
            }
        });

        return view;
    }

    private void validateCust() {
        String customer_name = name.getText().toString().trim();
        String customer_contact = phone.getText().toString().trim();
        if (TextUtils.isEmpty(customer_name)) {
            name.setError("Enter a Valid Name");
        } else if (TextUtils.isEmpty(customer_contact) && customer_contact.length() < 10) {
            phone.setError("Enter a Valid Number");
        } else {
            addCusotmerToDB();
        }
    }

    private void addCusotmerToDB() {
        custRef = FirebaseDatabase.getInstance().getReference().child("customers");

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentdate = new SimpleDateFormat("dd/MM/yyyy-hh:mm:ss");
        String CurrentDate = currentdate.format(new Date());
        String custUid = UUID.randomUUID().toString().substring(0,12);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name", name.getText().toString().trim());
        hashMap.put("contact", phone.getText().toString().trim());
        hashMap.put("updated", CurrentDate);
        hashMap.put("uid", custUid);

        dismiss();
        custRef.child(custUid).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    dismiss();
                } else {
                    dismiss();
                    Toast.makeText(getContext(), "Network Error\nTry Again!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
