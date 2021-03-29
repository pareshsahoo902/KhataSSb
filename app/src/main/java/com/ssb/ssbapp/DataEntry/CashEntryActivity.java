package com.ssb.ssbapp.DataEntry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ssb.ssbapp.CustomCalculator.CustomCalculator;
import com.ssb.ssbapp.DialogHelper.ImagePickerDailog;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.Utils.SSBBaseActivity;
import com.ssb.ssbapp.Utils.UtilsMethod;

public class CashEntryActivity extends SSBBaseActivity implements CustomCalculator.CalculatorListner, ImagePickerDailog.ImagePickerListner {

    private boolean isUri;
    private Bitmap bitmap;
    private ImageView billIMageMoney;
    private TextView dateTextBtn,imageTextButton;
    private Uri picUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_entry);

        setToolbar(getApplicationContext(),"Cash Entry");

        billIMageMoney = findViewById(R.id.billIMage);
        dateTextBtn = findViewById(R.id.dateTextBtn);
        imageTextButton = findViewById(R.id.imageTextBtn);

        dateTextBtn.setText(UtilsMethod.getCurrentDate().substring(0,10));

        imageTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePickerDailog dailog = new ImagePickerDailog();
                dailog.show(getSupportFragmentManager(),"Pick Image");
            }
        });

    }

    @Override
    public void onOnePressListner(String btn, String chrSequence) {

    }

    @Override
    public void onTwoPressListner(String btn, String chrSequence) {

    }

    @Override
    public void onThreePressListner(String btn, String chrSequence) {

    }

    @Override
    public void onFourPressListner(String btn, String chrSequence) {

    }

    @Override
    public void onFivePressListner(String btn, String chrSequence) {

    }

    @Override
    public void onSixPressListner(String btn, String chrSequence) {

    }

    @Override
    public void onSevenPressListner(String btn, String chrSequence) {

    }

    @Override
    public void onEightPressListener(String btn, String chrSequence) {

    }

    @Override
    public void onNinePressListner(String btn, String chrSequence) {

    }

    @Override
    public void onZeroPressListner(String btn, String chrSequence) {

    }

    @Override
    public void onMemoryPlusPressListner(String btn, String chrSequence) {

    }

    @Override
    public void onMemoryMinusPressListner(String btn, String chrSequence) {

    }

    @Override
    public void onAllClearPressListner(String btn, String chrSequence) {

    }

    @Override
    public void onDeletePressListner(String btn, String chrSequence) {

    }

    @Override
    public void onEqualsPressListner(String btn, String chrSequence) {

    }

    @Override
    public void onAddButtonPressListner(String btn, String chrSequence) {

    }

    @Override
    public void onSubButtonPressListner(String btn, String chrSequence) {

    }

    @Override
    public void onDivideButtonPressListner(String btn, String chrSequence) {

    }

    @Override
    public void onMultiplyButtonPressListner(String btn, String chrSequence) {

    }

    @Override
    public void onDecimalPointPressListner(String btn, String chrSequence) {

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