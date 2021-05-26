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
import com.ssb.ssbapp.TransactionModel.PartyModel;
import com.ssb.ssbapp.Utils.Constants;
import com.ssb.ssbapp.Utils.SSBBaseActivity;
import com.ssb.ssbapp.Utils.UtilsMethod;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import static com.ssb.ssbapp.Utils.Constants.SSB_PREF_DATE;

public class PartyEntryActivity extends SSBBaseActivity implements CustomCalculator.CalculatorListner, ImagePickerDailog.ImagePickerListner {
    private EditText entryText, itemName, entryDescription, partDescription;
    private TextView commisionTotal, totalText;
    private EditText commisionText, fairText, labourText, extraText;
    private TextView entries_text, dateTextBtn, imageTextBtn;
    private String descText = "";
    private CustomCalculator customCalculator;
    private double fair = 0.0, commision = 0.0, labour = 0.0, extra = 0.0, t = 0.0;
    private Button saveEntry;
    private LinearLayout entryLayout;
    private List<Double> itemBalanceList;
    private double totalAmount = 0;
    private Bitmap bitmap;
    private ImageView billIMageMoney;
    private Uri picUri;
    boolean isUri = true;
    String detailText="";
    double cm;
    String CurrentDate, type, commTextDesc = "";
    private DatabaseReference moneyTransactionRef;
    //TODO commison fair calculation
    private double balance;
    boolean doubleBackToExitPressedOnce = false;

    private StorageTask uploadtask;
    private StorageReference userStorage;
    private String picDowloadUrl = "";
    private boolean isEqual=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_entry);
        setToolbar(getApplicationContext(), "Party Entry");


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
        commisionTotal = findViewById(R.id.commisionTotal);
        customCalculator.setVisibility(View.GONE);
        itemBalanceList = new ArrayList<>();
        moneyTransactionRef = FirebaseDatabase.getInstance().getReference().child("customerTransaction");
        moneyTransactionRef.keepSynced(true);

        commisionText.setText("8");
        entryText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    entryText.setShowSoftInputOnFocus(false);
                    customCalculator.setVisibility(View.VISIBLE);
                } else {
                    customCalculator.setVisibility(View.GONE);
                }
            }
        });

        entryLayout.setVisibility(View.VISIBLE);
        entries_text.setVisibility(View.VISIBLE);
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

        commisionText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    cm = Double.parseDouble(commisionText.getText().toString().trim());

                } catch (NumberFormatException e) {
                    commisionText.setText("0");
                    commisionText.hasFocus();
                    commisionText.selectAll();
                    cm = 0.0;

                }

                if (!fairText.getText().toString().trim().equals("") && fairText.getText().toString().trim() != null) {
                    fair = Double.parseDouble(fairText.getText().toString().trim());
                }
                if (!labourText.getText().toString().trim().equals("") && labourText.getText().toString().trim() != null) {
                    labour = Double.parseDouble(labourText.getText().toString().trim());
                }
                if (!extraText.getText().toString().trim().equals("") && extraText.getText().toString().trim() != null) {
                    extra = Double.parseDouble(extraText.getText().toString().trim());
                }
                commision = totalAmount * ((cm) / 100);
                commisionTotal.setText("=   " + getCurrencyStr() + String.valueOf(commision));

                t = fair + commision + labour + extra;
                totalText.setText(getCurrencyStr() + String.format("%.1f", t));

                saveEntry.setText("GRAND TOTAL = " + getCurrencyStr() + String.format("%.1f", totalAmount - t));

                commTextDesc = "\nFair(" + String.valueOf(fair) + ")+ Labour(" + String.valueOf(labour) + ")+ Extra(" + String.valueOf(extra) + ")+ Commission("
                        + String.valueOf(cm) + "% =" + String.valueOf(commision) + ")\nTotal:" + String.valueOf(totalAmount) + "-" + String.valueOf(t) +
                        " = " + getCurrencyStr() + String.valueOf(totalAmount - t);
            }
        });

        fairText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                commisionText.setText(commisionText.getText().toString());

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    fair = Double.parseDouble(fairText.getText().toString().trim());
                } catch (NumberFormatException e) {
//                    textTOATAL = textTOATAL - ex;
                    fairText.setText("0");
                    fairText.hasFocus();
                    fairText.selectAll();
                    fair = 0.0;

                }
            }
        });
        labourText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                commisionText.setText(commisionText.getText().toString());

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    labour = Double.parseDouble(labourText.getText().toString().trim());
                } catch (NumberFormatException e) {
//                    textTOATAL = textTOATAL - ex;
                    labourText.setText("0");
                    labourText.hasFocus();
                    labourText.selectAll();
                    labour = 0.0;

                }
            }
        });

        //TODO commsion calc
        extraText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                commisionText.setText(commisionText.getText().toString());
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

            }


        });


        CurrentDate=getLocalSession().getString(SSB_PREF_DATE);
        dateTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(PartyEntryActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        userStorage = FirebaseStorage.getInstance().getReference().child("SSB").child("Transaction Image");

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


        saveEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean save = (boolean) saveEntry.getTag();
                if (save) {
                    if (totalAmount >= 1.0) {
                        saveEntryToDB();

                    } else {
                        showMessageToast("Entry Amount can not be 0", true);
                    }
                } else {
                    entryText.setText((String.valueOf(totalAmount)));
                    totalAmount -= t;
                    saveEntry.setTag(true);
                    saveEntry.setText("Save");
                }
            }
        });
        dateTextBtn.setText(getLocalSession().getString(SSB_PREF_DATE).substring(0, 10));
    }

    private void saveEntryToDB() {
        showProgress();
        String ceid = UUID.randomUUID().toString();

        if (isUri) {
            if (picUri != null) {
                uploadImageUri(ceid);

            } else {
                startAddingToDB("", ceid);
            }
        } else {
            if (bitmap != null) {
                uploadBitmapImage(ceid);

            } else {
                startAddingToDB("", ceid);
            }
        }
    }

    private void startAddingToDB(String picDowloadUrl, String ceid) {

        PartyModel partyModel = new PartyModel(totalText.getText().toString().substring(1),commision,fair,labour,extra,Integer.parseInt(commisionText.getText().toString()));


        MoneyTransactionModel model = new MoneyTransactionModel(ceid, getLocalSession().getString(Constants.SSB_PREF_CID), getLocalSession().getString(Constants.SSB_PREF_KID)
                , CurrentDate, CurrentDate, picDowloadUrl, partDescription.getText().toString() + "\n" + entryDescription.getText().toString(),  descText + commTextDesc, type, String.valueOf(totalAmount),String.valueOf(getTotalAmount()) ,false,true,partyModel,getDetails(itemBalanceList));

        if (model.getCid() != null) {
            moneyTransactionRef.child(ceid).setValue(model);
            startActivity(new Intent(PartyEntryActivity.this, SucessActivity.class).putExtra(Constants.SSB_SUCESS_INTENT, "money"));
            finish();

        }


    }




    private void uploadImageUri(String ceid) {
        if (picUri != null) {

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
                        startAddingToDB(picDowloadUrl, ceid);
                    }
                }
            });

        }
    }

    private void uploadBitmapImage(String ceid) {

        if (bitmap != null) {

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
        if (chrSequence != null) {
            descText = descText + "(" + btn + ")\n"+itemName.getText().toString()+": ";
            entryText.setText(chrSequence + "0");
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
        descText = "";
        entryText.setText("");
        updateLabel(chrSequence);
        itemBalanceList.clear();
        calulateTotal();

    }

    @Override
    public void onDeletePressListner(String btn, String chrSequence) {
        updateLabel(chrSequence);
    }

    @Override
    public void onEqualsPressListner(String btn, String chrSequence) {
        if (chrSequence != null) {
            descText += btn;
            entryText.setText(chrSequence);
            descText += chrSequence;
            updateLabel(chrSequence);
            itemBalanceList.add(Double.parseDouble(chrSequence));
            calulateTotal();

        }
        isEqual=true;
        itemName.setText("");
        entryText.setText("");


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
        if (descText.length()<=1){
            descText = itemName.getText().toString()+": "+descText;
            entries_text.setText(descText);
        }
        else if (isEqual){
            isEqual=false;
            descText=entries_text.getText().toString() +"(M+)\n"+itemName.getText().toString()+ " :"+chrSequence;
            entries_text.setText( descText);
            return;
        }else{
            entries_text.setText(descText);
        }
    }

    private double getTotalAmount(){
        double total =0.0;
        if (itemBalanceList.size() > 0) {
            for (int i = 0; i < itemBalanceList.size(); i++) {
                total += itemBalanceList.get(i);
            }
        }
        return  total;

    }

    private double calulateTotal() {

        double res = 0;
        if (itemBalanceList.size() > 0) {
            for (int i = 0; i < itemBalanceList.size(); i++) {
                res += itemBalanceList.get(i);
            }
            totalAmount = res;
            saveEntry.setText("GRAND TOTAL = " + getCurrencyStr() + String.format("%.1f", res));
            saveEntry.setTag(false);
            double tcom =( Double.parseDouble(commisionText.getText().toString().trim()) *res)/100;
            commisionTotal.setText(String.valueOf(tcom));

        }

        return res;

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

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom()) {
                UtilsMethod.hideKeyboard(this);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        } else {
            this.doubleBackToExitPressedOnce = true;
            customCalculator.setVisibility(View.GONE);
        }
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);


    }

    private String getDetails(List<Double> itemBalanceList){
        String detail ="";

        detail =descText;
        detail= detail+"\nTotal: "+calulateTotal();

        return detail;
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