package com.ssb.ssbapp.TransactionPage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ssb.ssbapp.DataEntry.TrayEntryActivity;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.Utils.SSBBaseActivity;
import com.ssb.ssbapp.Utils.UtilsMethod;

public class TrayTransactionPage extends SSBBaseActivity {

    private String name;
    private CardView getBtn, gaveBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tray_transaction_page);
        name = getIntent().getStringExtra("name");
        setToolbar(getApplicationContext(), "  " + UtilsMethod.capitalize(name));
        getBtn = findViewById(R.id.getbtn);
        gaveBtn = findViewById(R.id.gavebtn);


        getBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TrayTransactionPage.this, TrayEntryActivity.class)
                .putExtra("type","got"));
            }
        });
        gaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TrayTransactionPage.this, TrayEntryActivity.class)
                .putExtra("type","gave"));
            }
        });


    }
}