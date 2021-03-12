package com.ssb.ssbapp.Admin.CreateEmployee;


import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.common.collect.Range;
import com.ssb.ssbapp.MainActivity;
import com.ssb.ssbapp.Model.EmployeeModel;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.Utils.Constants;
import com.ssb.ssbapp.Utils.SSBBaseActivity;

import static com.ssb.ssbapp.Utils.Constants.SSB_EMPLOYEE_DETAILS;

public class CreateEmployeeActivity extends SSBBaseActivity {
    private Button btn;
    private EditText firstname, lastname, aadharnumber, contact, loginID, password, confirmPassword;
    private Spinner branchSpinner;
    private RadioGroup ragioGroup;
    private long type;
    private AwesomeValidation awesomeValidation;
    private String branchAssinged = "main", designation, employee_details = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_employee);
        setToolbar(getApplicationContext(), "Create Employee");

        btn = findViewById(R.id.savebtn);
        firstname = findViewById(R.id.firstname_Text);
        lastname = findViewById(R.id.lastname_Text);
        aadharnumber = findViewById(R.id.aadharText);
        contact = findViewById(R.id.phoneNumberText);
        loginID = findViewById(R.id.loginIDText);
        password = findViewById(R.id.passcodeText);
        confirmPassword = findViewById(R.id.confirmpassCodetext);

        branchSpinner = findViewById(R.id.branchSpinner);
        ragioGroup = findViewById(R.id.designationRadioGroup);


        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);


        awesomeValidation.addValidation(this, R.id.loginIDText, Patterns.EMAIL_ADDRESS, R.string.emailerr);
        awesomeValidation.addValidation(this, R.id.phoneNumberText, Patterns.PHONE, R.string.phoneer);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (awesomeValidation.validate() && password.getText().toString().equals(confirmPassword.getText().toString())) {
                    onclick();
                    validateEmployeeDeatils(new EmployeeModel(firstname.getText().toString() + " " + lastname.getText().toString(), aadharnumber.getText().toString(),
                            contact.getText().toString(), loginID.getText().toString(), password.getText().toString(), branchAssinged,type ));

                } else {
                    showMessageToast("Enter Credentials Properly ! Enter password properly", true);
                }


            }
        });
    }

    private void validateEmployeeDeatils(EmployeeModel employeeModel) {
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(SSB_EMPLOYEE_DETAILS+"model",employeeModel);
        startActivity(new Intent(CreateEmployeeActivity.this, AddEmploeePayroll.class)
                .putExtras(mBundle)
                .putExtra(SSB_EMPLOYEE_DETAILS, "Name : " + firstname.getText().toString() + " " + lastname.getText().toString() + "\nAadhar number : " + aadharnumber.getText().toString() + "\nContact : " + contact.getText().toString() + " \nBranch assinged : " + branchAssinged + "\nDesignation : " + onclick()+ String.valueOf(type)));

    }

    private String onclick() {
        int selectedId = ragioGroup.getCheckedRadioButtonId();

        if (selectedId == -1) {
            return "Not Assigned !";
        } else {
            RadioButton radioButton = (RadioButton) findViewById(selectedId);
            if (radioButton.getText().toString().equals("Money Entry(All)")) {
                type = 1;
            }
            if (radioButton.getText().toString().equals("Tray Entry(All)")) {
                type = 2;
            }
            if (radioButton.getText().toString().equals("Money & Tray(All)")) {
                type = 3;
            }
            if (radioButton.getText().toString().equals("Only View All")) {
                type = 4;
            }

            return radioButton.getText().toString();
        }


    }


}