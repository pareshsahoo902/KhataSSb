package com.ssb.ssbapp.DataEntry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ssb.ssbapp.CustomCalculator.CustomCalculator;
import com.ssb.ssbapp.DialogHelper.ImagePickerDailog;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.SuccessScreens.SucessActivity;
import com.ssb.ssbapp.TransactionModel.MoneyTransactionModel;
import com.ssb.ssbapp.Utils.Constants;
import com.ssb.ssbapp.Utils.SSBBaseActivity;
import com.ssb.ssbapp.Utils.UtilsMethod;

import java.util.UUID;

public class CashEntryActivity extends SSBBaseActivity implements ImagePickerDailog.ImagePickerListner {

    private boolean isUri;
    private Bitmap bitmap;
    private ImageView billIMageMoney;
    private EditText cashType, cashEntryText, discount,description;
    private TextView dateTextBtn, imageTextButton, entriesText;
    private Uri picUri;
    private Button saveEntry;
    private double totalCash, cashAmount;
    private String type;
    private DatabaseReference moneyTransactionRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_entry);

        setToolbar(getApplicationContext(), "Cash Entry");
        type=getIntent().getStringExtra(Constants.SSB_TRANSACTION_TYPE);

        billIMageMoney = findViewById(R.id.billIMage);
        dateTextBtn = findViewById(R.id.dateTextBtn);
        imageTextButton = findViewById(R.id.imageTextBtn);
        cashEntryText = findViewById(R.id.calcEntry);
        cashType = findViewById(R.id.cashType);
        entriesText = findViewById(R.id.entriescash_text);
        discount = findViewById(R.id.discount);
        saveEntry = findViewById(R.id.savecashEnrty);
        description = findViewById(R.id.entryDescription);

        moneyTransactionRef = FirebaseDatabase.getInstance().getReference().child("customerTransaction");
        moneyTransactionRef.keepSynced(true);

        saveEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEntryToDB();
            }
        });
        cashEntryText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cashEntryText.selectAll();
            }
        });


        cashEntryText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    calcDis();
                } catch (Exception e) {
                    e.printStackTrace();
                    cashEntryText.setText("0");
                    cashEntryText.selectAll();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                entriesText.setVisibility(View.VISIBLE);
            }
        });

        dateTextBtn.setText(UtilsMethod.getCurrentDate().substring(0, 10));

        imageTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePickerDailog dailog = new ImagePickerDailog();
                dailog.show(getSupportFragmentManager(), "Pick Image");
            }
        });

    }

    private void saveEntryToDB() {
        String ceid = UUID.randomUUID().toString();

        MoneyTransactionModel model = new MoneyTransactionModel(ceid,getLocalSession().getString(Constants.SSB_PREF_CID),getLocalSession().getString(Constants.SSB_PREF_KID)
                ,UtilsMethod.getCurrentDate(),UtilsMethod.getCurrentDate(),"",description.getText().toString(),entriesText.getText().toString(),type,totalCash);

        if (model.getCid()!=null){
            moneyTransactionRef.child(ceid).setValue(model);
            startActivity(new Intent(CashEntryActivity.this, SucessActivity.class).putExtra(Constants.SSB_SUCESS_INTENT,"money"));
            finish();

        }
    }

    private void calcDis() {
        double disText=0.0;
        if (Double.parseDouble(cashEntryText.getText().toString()) != 0 && Double.parseDouble(discount.getText().toString()) != 0)
            disText = Double.parseDouble(cashEntryText.getText().toString()) + Double.parseDouble(discount.getText().toString());

        cashAmount= Double.parseDouble(cashEntryText.getText().toString());
        totalCash = disText;
        entriesText.setText(cashType.getText().toString() + " : " + cashEntryText.getText().toString() + " + " + discount.getText().toString()+ " = "+String.format("%.2f",disText));

    }


    @Override
    public void pickImageIntent(int type) {

        if (type == 0) {
            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePicture, type);
        } else {
            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto, type);
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    isUri = false;
                    bitmap = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    billIMageMoney.setImageBitmap(bitmap);
                    billIMageMoney.setVisibility(View.VISIBLE);

                }

                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    isUri = true;
                    picUri = imageReturnedIntent.getData();
                    billIMageMoney.setImageURI(picUri);
                    billIMageMoney.setVisibility(View.VISIBLE);

                }
                break;
        }
    }
}