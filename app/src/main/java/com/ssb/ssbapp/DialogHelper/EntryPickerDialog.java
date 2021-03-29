package com.ssb.ssbapp.DialogHelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
       if (getArguments().getString("transaction_type").equals("gave")){
           type = "gave";
       }
       else {
           type = "got";
       }


        cash = view.findViewById(R.id.cashPick);
        customer = view.findViewById(R.id.custPick);
        party = view.findViewById(R.id.partyPick);

        cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), CashEntryActivity.class).putExtra(Constants.SSB_TRANSACTION_TYPE,type));
                dismiss();
            }
        });
        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MoneyEntryActivity.class).putExtra(Constants.SSB_TRANSACTION_TYPE,type));
                dismiss();
            }
        });
        party.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), PartyEntryActivity.class).putExtra(Constants.SSB_TRANSACTION_TYPE,type));
                dismiss();
            }
        });

    }
}
