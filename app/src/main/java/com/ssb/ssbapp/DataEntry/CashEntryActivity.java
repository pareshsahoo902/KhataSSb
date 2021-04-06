package com.ssb.ssbapp.DataEntry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ssb.ssbapp.CashDetails.CashModel;
import com.ssb.ssbapp.CustomCalculator.CustomCalculator;
import com.ssb.ssbapp.DialogHelper.ImagePickerDailog;
import com.ssb.ssbapp.Home.HomeActivity;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.SuccessScreens.SucessActivity;
import com.ssb.ssbapp.TransactionModel.MoneyTransactionModel;
import com.ssb.ssbapp.Utils.Constants;
import com.ssb.ssbapp.Utils.SSBBaseActivity;
import com.ssb.ssbapp.Utils.UtilsMethod;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import static com.ssb.ssbapp.Utils.Constants.SSB_PREF_DATE;

public class CashEntryActivity extends SSBBaseActivity implements ImagePickerDailog.ImagePickerListner {

    private boolean isUri;
    private Bitmap bitmap;
    private ImageView billIMageMoney;
    private EditText cashType, cashEntryText, discount,description;
    private TextView dateTextBtn, imageTextButton, entriesText;
    private Uri picUri;
    private String cutomerName;
    private Button saveEntry;
    private Calendar myCalendar;

    private double totalCash, cashAmount;
    private String type;
    private DatabaseReference moneyTransactionRef,custRef,cashRef;


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
        custRef = FirebaseDatabase.getInstance().getReference().child("customers");
        cashRef = FirebaseDatabase.getInstance().getReference().child("cashDetails");
        cashRef.keepSynced(true);
        moneyTransactionRef.keepSynced(true);

        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
                String CurrentDate = sdf.format(myCalendar.getTime());
                dateTextBtn.setText(CurrentDate);

            }

        };

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

        dateTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CashEntryActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
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

        custRef.child(getLocalSession().getString(Constants.SSB_PREF_CID)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    cutomerName = snapshot.child("name").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void saveEntryToDB() {
        String ceid = UUID.randomUUID().toString();

        MoneyTransactionModel model = new MoneyTransactionModel(ceid,getLocalSession().getString(Constants.SSB_PREF_CID),getLocalSession().getString(Constants.SSB_PREF_KID)
                ,dateTextBtn.getText().toString(),dateTextBtn.getText().toString(),"",description.getText().toString(),entriesText.getText().toString(),type,totalCash);

        if (model.getCid()!=null){
            moneyTransactionRef.child(ceid).setValue(model);
            //TODO load data to cashinOut page
            loadCashDetailsData(ceid);
            startActivity(new Intent(CashEntryActivity.this, SucessActivity.class).putExtra(Constants.SSB_SUCESS_INTENT,"money"));
            finish();

        }
    }

    private void loadCashDetailsData(String ceid) {
        String cdid = UUID.randomUUID().toString().substring(0,14);

        CashModel cashModel = new CashModel(cdid,ceid,getLocalSession().getString(Constants.SSB_PREF_CID),getLocalSession().getString(Constants.SSB_PREF_KID),cutomerName
        ,dateTextBtn.getText().toString(),dateTextBtn.getText().toString(),type,Double.parseDouble(cashEntryText.getText().toString().trim()));

        if (cashModel.getCid()!=null){
            cashRef.child(cdid).setValue(cashModel);
        }
    }

    private void calcDis() {
        double disText=0.0;
        if (Double.parseDouble(cashEntryText.getText().toString()) != 0 && Double.parseDouble(discount.getText().toString()) >= 0)
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