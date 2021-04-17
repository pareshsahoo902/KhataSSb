package com.ssb.ssbapp.KhataMaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ssb.ssbapp.Home.HomeActivity;
import com.ssb.ssbapp.Login.LoginActivity;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.Utils.SSBBaseActivity;
import com.ssb.ssbapp.Utils.UtilsMethod;

import java.util.HashMap;
import java.util.UUID;

import static com.ssb.ssbapp.Utils.Constants.SSB_PREF_BRANCH;
import static com.ssb.ssbapp.Utils.Constants.SSB_PREF_KID;

public class NewKhata extends SSBBaseActivity {

    private EditText khata_text;
    private Button add_btn;
    private DatabaseReference khataRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_khata);

        setToolbar(getApplicationContext(),"Add Branch");

        khata_text = findViewById(R.id.kahatnew_nametext);
        add_btn = findViewById(R.id.addKBtn);

        khataRef = FirebaseDatabase.getInstance().getReference().child("khata");

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndUpdate();
            }
        });

    }

    private void validateAndUpdate() {
        if (TextUtils.isEmpty(khata_text.getText().toString().trim())){
            khata_text.setError("Khata Name cannot be empty!");
        }
        else {
            showProgress();

            String kid = UUID.randomUUID().toString().substring(0,6);

            HashMap<String,Object> hashMap=new HashMap<>();
            hashMap.put("name",khata_text.getText().toString().trim());
            hashMap.put("isActive",true);
            hashMap.put("company","ssb");
            hashMap.put("kid",kid);
            hashMap.put("created_at", UtilsMethod.getCurrentDate());


            khataRef.child(kid).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    khata_text.setText("");
                    getLocalSession().putString(SSB_PREF_BRANCH,khata_text.getText().toString());
                    getLocalSession().putString(SSB_PREF_KID,kid);
                    dismissProgress();
                    startActivity(new Intent(NewKhata.this, HomeActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));

                }
            });
        }
    }
}