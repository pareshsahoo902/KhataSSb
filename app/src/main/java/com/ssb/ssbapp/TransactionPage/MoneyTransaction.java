package com.ssb.ssbapp.TransactionPage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ssb.ssbapp.DataEntry.MoneyEntryActivity;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.Utils.SSBBaseActivity;
import com.ssb.ssbapp.Utils.UtilsMethod;

public class MoneyTransaction extends SSBBaseActivity {

    private CardView getBtn;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_transaction);

        name = getIntent().getStringExtra("name");

        setToolbar(getApplicationContext(),"  "+ UtilsMethod.capitalize(name));
        getBtn = findViewById(R.id.getbtn);

        getBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MoneyTransaction.this, MoneyEntryActivity.class));
            }
        });
    }
}