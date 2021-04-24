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

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
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

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import static com.ssb.ssbapp.Utils.Constants.SSB_PREF_DATE;

public class CashEntryActivity extends SSBBaseActivity implements ImagePickerDailog.ImagePickerListner {

    private boolean isUri;
    private Bitmap bitmap = null;
    private ImageView billIMageMoney;
    private EditText cashType, cashEntryText, discount, description;
    private TextView dateTextBtn, imageTextButton, entriesText;
    private Uri picUri = null;
    private String cutomerName;
    private Button saveEntry, clearKhata;
    private Calendar myCalendar;
    private double totalCash, cashAmount;
    private String type;
    String CurrentDate;
    private DatabaseReference moneyTransactionRef, custRef, cashRef;

    private StorageTask uploadtask;
    private StorageReference userStorage;
    private String picDowloadUrl = "";
    private double balance;
    private double cash , discountText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_entry);

        setToolbar(getApplicationContext(), "Cash Entry");
        type = getIntent().getStringExtra(Constants.SSB_TRANSACTION_TYPE);
        balance = Double.parseDouble(getIntent().getStringExtra(Constants.SSB_BALANCE_INTENT));

        billIMageMoney = findViewById(R.id.billIMage);
        dateTextBtn = findViewById(R.id.dateTextBtn);
        imageTextButton = findViewById(R.id.imageTextBtn);
        cashEntryText = findViewById(R.id.calcEntry);
        cashType = findViewById(R.id.cashType);
        entriesText = findViewById(R.id.entriescash_text);
        discount = findViewById(R.id.discount);
        saveEntry = findViewById(R.id.savecashEnrty);
        clearKhata = findViewById(R.id.clearKhata);
        description = findViewById(R.id.entryDescription);
        cashEntryText.setText("");


        moneyTransactionRef = FirebaseDatabase.getInstance().getReference().child("customerTransaction");
        custRef = FirebaseDatabase.getInstance().getReference().child("customers");
        cashRef = FirebaseDatabase.getInstance().getReference().child("cashDetails");
        cashRef.keepSynced(true);
        moneyTransactionRef.keepSynced(true);

        userStorage = FirebaseStorage.getInstance().getReference().child("SSB").child("Transaction Image");


        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd-MM-yyyy hh:mm:ss a"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
                CurrentDate = sdf.format(myCalendar.getTime());
                dateTextBtn.setText(CurrentDate.substring(0, 10));
            }
        };


        //TODO zero code written here!
        clearKhata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double t = Double.parseDouble(cashEntryText.getText().toString());
                balance=Math.abs(balance);
                if (balance>t){
                    double res = balance - t;
                    discount.setText(String.format("%.1f", res));
                    calcDis();
                }else {
                    discount.setText("0");
                    calcDis();
                }
            }
        });


        saveEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalCash>=1.0){
                    saveEntryToDB();

                }else{
                    showMessageToast("Entry Amount can not be 0",true);
                }
            }
        });
        cashEntryText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


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
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                entriesText.setText(cashType.getText().toString() + " : " + cashEntryText.getText().toString());
                entriesText.setVisibility(View.VISIBLE);
            }
        });

        discount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    calcDis();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        CurrentDate = UtilsMethod.getCurrentDate();
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
                if (snapshot.exists()) {
                    cutomerName = snapshot.child("name").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        dateTextBtn.setText(getLocalSession().getString(SSB_PREF_DATE).substring(0,10));


    }

    private double getBalance(double total, double pending) {

        pending = Math.abs(pending);
        if (type.equals("got")) {
            return total + pending;

        } else {
            return total - pending;
        }

    }

    private void saveEntryToDB() {
        String ceid = UUID.randomUUID().toString();
        showProgress();
        if (isUri) {
            if (picUri != null) {
                UploadUserPicUri(ceid);
            } else {
                startAddingToDB("", ceid);
            }
        } else {
            if (bitmap != null) {
                UploadUserPicBitmap(ceid);
            } else {
                startAddingToDB("", ceid);
            }
        }
        //TODO load data to cashinOut page

    }

    private void UploadUserPicBitmap(String ceid) {

        if (bitmap != null) {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = userStorage.child(ceid + ".jpg").putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    dismissProgress();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    userStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            dismissProgress();
                            picDowloadUrl = uri.toString();
                            startAddingToDB(picDowloadUrl, ceid);
                        }
                    });


                }
            });


        } else {
            dismissProgress();
            showMessageToast("Enter a Image !", false);
        }
    }

    private void startAddingToDB(String dowloadUrl, String ceid) {

        MoneyTransactionModel model = new MoneyTransactionModel(ceid, getLocalSession().getString(Constants.SSB_PREF_CID), getLocalSession().getString(Constants.SSB_PREF_KID)
                , CurrentDate, CurrentDate, dowloadUrl, description.getText().toString(), entriesText.getText().toString(), type, totalCash, getBalance(totalCash, balance));

        if (model.getCid() != null) {
            moneyTransactionRef.child(ceid).setValue(model);
            loadCashDetailsData(ceid);
            startActivity(new Intent(CashEntryActivity.this, SucessActivity.class).putExtra(Constants.SSB_SUCESS_INTENT, "money"));
            finish();
        }

    }

    private void UploadUserPicUri(String ceid) {

        if (picUri != null) {

            final StorageReference filepath = userStorage.child(ceid + ".jpg");
            uploadtask = filepath.putFile(picUri);

            uploadtask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        dismissProgress();
                        showMessageToast("Error Uploading Pic!", true);
                    }
                    return filepath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        dismissProgress();
                        picDowloadUrl = task.getResult().toString();
                        startAddingToDB(picDowloadUrl, ceid);

                    }
                }
            });
        }
    }

    private void    loadCashDetailsData(String ceid) {
        String cdid = UUID.randomUUID().toString().substring(0, 14);

        CashModel cashModel = new CashModel(cdid, ceid, getLocalSession().getString(Constants.SSB_PREF_CID), getLocalSession().getString(Constants.SSB_PREF_KID), cutomerName
                , dateTextBtn.getText().toString(), dateTextBtn.getText().toString(), type, Double.parseDouble(cashEntryText.getText().toString().trim()));

        if (cashModel.getCid() != null) {
            cashRef.child(cdid).setValue(cashModel);
        }
    }

    private void calcDis() {
        double disText = 0.0;
        if (Double.parseDouble(cashEntryText.getText().toString()) >= 0 )
            disText = Double.parseDouble(cashEntryText.getText().toString()) + Double.parseDouble(discount.getText().toString());

        cashAmount = Double.parseDouble(cashEntryText.getText().toString());
        totalCash = disText;
        entriesText.setText(cashType.getText().toString() + " : " + cashEntryText.getText().toString() + " + " + discount.getText().toString() + " = " + String.format("%.2f", disText));

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