package com.ssb.ssbapp.Admin.CreateEmployee;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ssb.ssbapp.R;
import com.ssb.ssbapp.Utils.SSBBaseActivity;

public class CreateEmployeeActivity extends SSBBaseActivity {
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_employee);
        setToolbar(getApplicationContext(),"Create Employee");

        btn = findViewById(R.id.savebtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateEmployeeActivity.this,AddEmploeePayroll.class));
            }
        });
    }
}