package com.ssb.ssbapp.Login;


import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.Utils.SSBBaseActivity;

public class LoginActivity extends SSBBaseActivity {
    private EditText email, password;
    private Button login;
    private FirebaseAuth mAuth;
    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        login = findViewById(R.id.loginbtn);

        mAuth = FirebaseAuth.getInstance();
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(this, R.id.login_email, Patterns.EMAIL_ADDRESS, R.string.emailerr);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateUser();
            }
        });

    }

    private void ValidateUser() {

        if (!awesomeValidation.validate()) {
            showMessageToast("Enter Credential Properly", true);
        } else if (TextUtils.isEmpty(password.getText().toString())) {
            showMessageToast("Enter Password", true);

        } else {
            loginUSER();
        }

    }


    private void loginUSER() {
        showProgress();
        mAuth.signInWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            dismissProgress();
                            showMessageToast("Login Failed !", true);
                        } else {

                            checkUserTypeAndUpdateHomeUi(mAuth.getCurrentUser().getUid());
                        }
                    }
                });

    }

    private void checkUserTypeAndUpdateHomeUi(String uid) {

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dismissProgress();
                int type = (int) snapshot.child("type").getValue();
                boolean isAdmin = (boolean)snapshot.child("admin").getValue();
                if (snapshot.exists()){
                    if (isAdmin){


                    }
                    else if (!isAdmin){

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dismissProgress();
            }
        });


    }
}