package com.ssb.ssbapp.DialogHelper;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ssb.ssbapp.DataEntry.CashEntryActivity;
import com.ssb.ssbapp.DataEntry.MoneyEntryActivity;
import com.ssb.ssbapp.DataEntry.PartyEntryActivity;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.Utils.Constants;

public class EntryPickerDialog extends BottomSheetDialogFragment {

    private ImageView cash ,customer , party;
    private String type;

    private EditText discount;
    private TextView gave , got , pending , result1 , gaveBalance , resultTotal;
    private  double allgave, allGot,balance;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.entry_picker_dialog, container,
                false);

        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        allgave = getArguments().getDouble("allGave");
        allGot = getArguments().getDouble("allGot");

       if (getArguments().getString("transaction_type").equals("gave")){
           type = "gave";
       }
       else {
           type = "got";
       }


       balance = allgave-allGot;
        cash = view.findViewById(R.id.cashPick);
        customer = view.findViewById(R.id.custPick);
        party = view.findViewById(R.id.partyPick);
        got = view.findViewById(R.id.getallText);
        gave = view.findViewById(R.id.giveallText);
        pending = view.findViewById(R.id.pendingText);
        result1 = view.findViewById(R.id.result1);
        resultTotal = view.findViewById(R.id.totalResult);
        gaveBalance = view.findViewById(R.id.allgavedis);
        discount = view.findViewById(R.id.disText);

        discount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!discount.getText().toString().trim().equals("")){
                    calcDiscount();

                }

            }
        });

        got.setText("All Got\n"+"₹"+String.valueOf(allGot));
        gave.setText("All Gave\n₹"+String.valueOf(allgave));
        pending.setText("Pending\n₹"+String.valueOf(allgave-allGot));
        gaveBalance.setText("₹"+String.valueOf(allgave)+" - ");
        calcDiscount();

        cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), CashEntryActivity.class).putExtra(Constants.SSB_TRANSACTION_TYPE,type)
                .putExtra(Constants.SSB_BALANCE_INTENT,String.valueOf(balance)));
                dismiss();
            }
        });
        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MoneyEntryActivity.class).putExtra(Constants.SSB_TRANSACTION_TYPE,type)
                        .putExtra(Constants.SSB_BALANCE_INTENT,String.valueOf(balance)));
                dismiss();
            }
        });
        party.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), PartyEntryActivity.class).putExtra(Constants.SSB_TRANSACTION_TYPE,type)
                        .putExtra(Constants.SSB_BALANCE_INTENT,String.valueOf(balance)));
                dismiss();
            }
        });

    }

    private void calcDiscount() {
        double dis = allgave * (Double.parseDouble(discount.getText().toString().trim())/100);
        result1.setText("= "+String.valueOf(allgave)+" - "+String.format("%.1f",dis));
        resultTotal.setText("= ₹"+String.format("%.2f",allgave - (allgave * (Double.parseDouble(discount.getText().toString().trim())/100))));
    }
}
