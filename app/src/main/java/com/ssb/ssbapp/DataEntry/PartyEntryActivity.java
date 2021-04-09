package com.ssb.ssbapp.DataEntry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.ssb.ssbapp.CustomCalculator.CustomCalculator;
import com.ssb.ssbapp.DialogHelper.ImagePickerDailog;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.SuccessScreens.SucessActivity;
import com.ssb.ssbapp.TransactionModel.MoneyTransactionModel;
import com.ssb.ssbapp.Utils.Constants;
import com.ssb.ssbapp.Utils.SSBBaseActivity;
import com.ssb.ssbapp.Utils.UtilsMethod;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class PartyEntryActivity extends SSBBaseActivity implements CustomCalculator.CalculatorListner, ImagePickerDailog.ImagePickerListner {
    private EditText entryText,itemName,entryDescription,partDescription;
    private EditText commisionText , fairText, labourText,extraText, totalText;
    private TextView entries_text,dateTextBtn,imageTextBtn;
    private String descText="";
    private CustomCalculator customCalculator;
    private double fair= 0.0, commision =0.0,labour = 0.0 , extra =0.0,t=0.0;
    private Button saveEntry;
    private LinearLayout entryLayout;
    private List<Double> itemBalanceList;
    private double totalAmount=0;
    private Bitmap bitmap;
    private ImageView billIMageMoney;
    private Uri picUri;
    boolean isUri = true;
    String CurrentDate,type;
    private DatabaseReference moneyTransactionRef;
//TODO commison fair calculation
    private double balance;
    boolean doubleBackToExitPressedOnce = false;

    private StorageTask uploadtask;
    private StorageReference userStorage;
    private String picDowloadUrl="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_entry);

        type = getIntent().getStringExtra(Constants.SSB_TRANSACTION_TYPE);
        balance = Double.parseDouble(getIntent().getStringExtra(Constants.SSB_BALANCE_INTENT));

        entryText = findViewById(R.id.calcEntry);
        customCalculator = findViewById(R.id.custom_calc);
        itemName = findViewById(R.id.itemName);
        entries_text = findViewById(R.id.entries_text);
        saveEntry = findViewById(R.id.saveEntry);
        entryLayout = findViewById(R.id.detailsLayout);
        dateTextBtn = findViewById(R.id.dateTextBtn);
        billIMageMoney = findViewById(R.id.billIMageMoney);
        imageTextBtn = findViewById(R.id.imageTextBtn);
        partDescription = findViewById(R.id.partDescription);
        entryDescription = findViewById(R.id.entryDescription);

        fairText = findViewById(R.id.fairText);
        commisionText = findViewById(R.id.commisionText);
        extraText = findViewById(R.id.extraText);
        labourText = findViewById(R.id.labourText);
        totalText = findViewById(R.id.totalText);

        itemBalanceList = new ArrayList<>();
        moneyTransactionRef = FirebaseDatabase.getInstance().getReference().child("customerTransaction");
        moneyTransactionRef.keepSynced(true);


        fairText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customCalculator.setVisibility(View.GONE);
            }
        });commisionText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customCalculator.setVisibility(View.GONE);
            }
        });extraText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customCalculator.setVisibility(View.GONE);
            }
        });labourText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customCalculator.setVisibility(View.GONE);
            }
        });
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
                CurrentDate= sdf.format(myCalendar.getTime());
                dateTextBtn.setText(CurrentDate.substring(0, 10));

            }

        };


        //TODO commsion calc
        extraText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                try {
                    extra = Double.parseDouble(extraText.getText().toString().trim());
                } catch (NumberFormatException e) {
//                    textTOATAL = textTOATAL - ex;
                    extraText.setText("0");
                    extraText.hasFocus();
                    extraText.selectAll();
                    extra = 0.0;

                }
                if (!fairText.getText().toString().trim().equals("") && fairText.getText().toString().trim()!=null){
                    fair = Double.parseDouble(fairText.getText().toString().trim());
                }
                if (!labourText.getText().toString().trim().equals("") && labourText.getText().toString().trim()!=null){
                    labour = Double.parseDouble(labourText.getText().toString().trim());
                }
                if (!commisionText.getText().toString().trim().equals("") && commisionText.getText().toString().trim()!=null){
                    double cm = Double.parseDouble(commisionText.getText().toString().trim());
                    commision = totalAmount*((cm)/100);
                }

               t = fair+commision+labour+extra;
                totalText.setText(getCurrencyStr()+String.format("%.1f",t));

                saveEntry.setText("MRC= "+getCurrencyStr()+String.format("%.1f",totalAmount+t));

            }


        });





        CurrentDate= UtilsMethod.getCurrentDate();
        dateTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(PartyEntryActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        userStorage= FirebaseStorage.getInstance().getReference().child("SSB").child("Transaction Image");

        imageTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePickerDailog dailog = new ImagePickerDailog();
                dailog.show(getSupportFragmentManager(), "Pick Image");
            }
        });
        entryDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customCalculator.setVisibility(View.GONE);
            }
        });

        partDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customCalculator.setVisibility(View.GONE);
            }
        });

        entryText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!entryText.getText().toString().equals("")) {
                    entryLayout.setVisibility(View.VISIBLE);
                    entries_text.setVisibility(View.VISIBLE);
                } else {
                    entryLayout.setVisibility(View.GONE);
                    entries_text.setVisibility(View.GONE);

                }

            }
        });

        saveEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final  boolean save = (boolean) saveEntry.getTag();
                if (save){
                    saveEntryToDB();
                }
                else {
                    entryText.setText((String.valueOf(totalAmount)));
                    totalAmount+=t;
                    saveEntry.setTag(true);
                    saveEntry.setText("Save");
                }
            }
        });
        setToolbar(getApplicationContext(),"Party Entry");
    }

    private void saveEntryToDB() {
        showProgress();
        String ceid = UUID.randomUUID().toString();

        if (isUri){
            if (picUri!=null){
                uploadImageUri(ceid);

            }else{
                startAddingToDB("",ceid);
            }
        }else{
            if (bitmap!=null){
                uploadBitmapImage(ceid);

            }else{
                startAddingToDB("",ceid);
            }
        }
    }

    private void startAddingToDB(String picDowloadUrl, String ceid) {

        MoneyTransactionModel model = new MoneyTransactionModel(ceid,getLocalSession().getString(Constants.SSB_PREF_CID),getLocalSession().getString(Constants.SSB_PREF_KID)
                ,CurrentDate,CurrentDate,picDowloadUrl,partDescription.getText().toString()+"\n"+entryDescription.getText().toString(),itemName.getText().toString()+": "+descText,type,totalAmount,getBalance(totalAmount,balance));

        if (model.getCid()!=null){
            moneyTransactionRef.child(ceid).setValue(model);
            startActivity(new Intent(PartyEntryActivity.this, SucessActivity.class).putExtra(Constants.SSB_SUCESS_INTENT,"money"));
            finish();

        }


    }

    private double getBalance (double total , double pending){

        pending = Math.abs(pending);
        if (type.equals("got")){
            return total+pending;

        }else{
            return total-pending;
        }

    }


    private void uploadImageUri(String ceid) {
        if(picUri!=null) {

            final StorageReference filepath = userStorage;
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
                        picDowloadUrl = task.getResult().toString();
                        startAddingToDB(picDowloadUrl,ceid);
                    }
                }
            });

        }
    }

    private void uploadBitmapImage(String ceid) {

        if(bitmap!=null){

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = userStorage.putBytes(data);
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
                            picDowloadUrl = uri.toString();
                            startAddingToDB(picDowloadUrl,ceid);
                        }
                    });


                }
            });


        }else {
            dismissProgress();
            showMessageToast("Enter a Image !",false);
        }
    }


    @Override
    public void onOnePressListner(String btn, String chrSequence) {
        if (chrSequence != null) {
            entryText.setText(chrSequence);
            descText += btn;
            updateLabel(chrSequence);

        }


    }

    @Override
    public void onTwoPressListner(String btn, String chrSequence) {
        if (chrSequence != null) {
            entryText.setText(chrSequence);
            descText += btn;
            updateLabel(chrSequence);

        }
    }

    @Override
    public void onThreePressListner(String btn, String chrSequence) {
        if (chrSequence != null) {
            entryText.setText(chrSequence);
            descText += btn;
            updateLabel(chrSequence);

        }
    }

    @Override
    public void onFourPressListner(String btn, String chrSequence) {
        if (chrSequence != null) {
            entryText.setText(chrSequence);
            descText += btn;
            updateLabel(chrSequence);

        }
    }

    @Override
    public void onFivePressListner(String btn, String chrSequence) {
        if (chrSequence != null) {
            entryText.setText(chrSequence);
            descText += btn;
            updateLabel(chrSequence);

        }
    }

    @Override
    public void onSixPressListner(String btn, String chrSequence) {
        if (chrSequence != null) {
            entryText.setText(chrSequence);
            descText += btn;
            updateLabel(chrSequence);

        }
    }

    @Override
    public void onSevenPressListner(String btn, String chrSequence) {
        if (chrSequence != null) {
            entryText.setText(chrSequence);
            descText += btn;
            updateLabel(chrSequence);

        }
    }

    @Override
    public void onEightPressListener(String btn, String chrSequence) {
        if (chrSequence != null) {
            entryText.setText(chrSequence);
            descText += btn;
            updateLabel(chrSequence);

        }
    }

    @Override
    public void onNinePressListner(String btn, String chrSequence) {
        if (chrSequence != null) {
            entryText.setText(chrSequence);
            descText += btn;
            updateLabel(chrSequence);

        }
    }

    @Override
    public void onZeroPressListner(String btn, String chrSequence) {
        if (chrSequence != null) {
            entryText.setText(chrSequence);
            descText += btn;
            updateLabel(chrSequence);

        }


    }

    @Override
    public void onMemoryPlusPressListner(String btn, String chrSequence) {
        if (chrSequence != null){
            descText =descText+"("+btn+")\n";
            entryText.setText(chrSequence+"0");
            updateLabel(chrSequence);


        }
    }

    @Override
    public void onMemoryMinusPressListner(String btn, String chrSequence) {
//        if (chrSequence != null){
//            updateLabel(chrSequence);
//            descText +=chrSequence;
//
//        }
    }

    @Override
    public void onAllClearPressListner(String btn, String chrSequence) {
        descText="";
        entryText.setText("");
        updateLabel(chrSequence);

    }

    @Override
    public void onDeletePressListner(String btn, String chrSequence) {
        updateLabel(chrSequence);
    }

    @Override
    public void onEqualsPressListner(String btn, String chrSequence) {
        if (chrSequence != null){
            descText+=btn;
            entryText.setText(chrSequence);
            descText+=chrSequence;
            updateLabel(chrSequence);
            itemBalanceList.add(Double.parseDouble(chrSequence));
            calulateTotal();

        }

    }

    @Override
    public void onAddButtonPressListner(String btn, String chrSequence) {
        if (chrSequence != null) {
            entryText.setText(chrSequence);
            descText += btn;
            updateLabel(chrSequence);
        }
    }

    @Override
    public void onSubButtonPressListner(String btn, String chrSequence) {
        if (chrSequence != null) {
            entryText.setText(chrSequence);
            descText += btn;
            updateLabel(chrSequence);

        }
    }

    @Override
    public void onDivideButtonPressListner(String btn, String chrSequence) {
        if (chrSequence != null) {
            entryText.setText(chrSequence);
            descText += btn;
            updateLabel(chrSequence);

        }
    }

    @Override
    public void onMultiplyButtonPressListner(String btn, String chrSequence) {
        if (chrSequence != null) {
            entryText.setText(chrSequence);
            descText += btn;
            updateLabel(chrSequence);

        }
    }

    @Override
    public void onDecimalPointPressListner(String btn, String chrSequence) {
        if (chrSequence != null) {
            entryText.setText(chrSequence);
            descText += btn;
            updateLabel(chrSequence);

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
                }

                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    isUri = true;
                    picUri = imageReturnedIntent.getData();
                    billIMageMoney.setImageURI(picUri);
                }
                break;
        }
    }

    private void updateLabel(String chrSequence) {
        entries_text.setVisibility(View.VISIBLE);
        entries_text.setText(itemName.getText().toString() + ": " + descText);

    }

    private void calulateTotal() {

        double res = 0;
        if (itemBalanceList.size()>0){
            for (int i =0 ;i<itemBalanceList.size();i++){
                res+=itemBalanceList.get(i);
            }
            totalAmount = res;
            saveEntry.setText("MRC= "+getCurrencyStr()+String.format("%.1f",res));
            saveEntry.setTag(false);
        }

    }

    /**
     * Used for dismissing the hardware keyboard on touch anywhere on the screen except the hardware keyboard
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom()){
                UtilsMethod.hideKeyboard(this);
                customCalculator.setVisibility(View.VISIBLE);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        else {
            this.doubleBackToExitPressedOnce = true;
            customCalculator.setVisibility(View.GONE);
        }
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);



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
}