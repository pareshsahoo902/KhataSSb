package com.ssb.ssbapp.Admin.CreateEmployee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.ssb.ssbapp.DialogHelper.ImagePickerDailog;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.SuccessScreens.SucessActivity;
import com.ssb.ssbapp.Utils.SSBBaseActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import pub.devrel.easypermissions.EasyPermissions;

public class AddEmploeePayroll extends SSBBaseActivity implements ImagePickerDailog.ImagePickerListner {
    Button btn;
    EditText joiningDateText;
    Calendar myCalendar;
    ImageView editPic, employeeImage;
    Bitmap bitmap;
    Uri picUri;
    boolean isUri=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_emploee_payroll);

        setToolbar(getApplicationContext(),"Payroll Details");

        btn = findViewById(R.id.createButton);
        joiningDateText = findViewById(R.id.joiningDateText);
        editPic = findViewById(R.id.editPic);
        employeeImage = findViewById(R.id.employeeimage);
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, perms)) {

            EasyPermissions.requestPermissions(this, "Need some permissions", 101, perms);
        }

            myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        editPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    ImagePickerDailog dailog = new ImagePickerDailog();
                    dailog.show(getSupportFragmentManager(), "Select Image !");

            }
        });

        joiningDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(AddEmploeePayroll.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                startActivity(new Intent(AddEmploeePayroll.this, SucessActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

            }
        });
    }


    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        joiningDateText.setText(sdf.format(myCalendar.getTime()));
        showMessageToast("working",false);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void pickImageIntent(int type) {

        if (type==0){
            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePicture, type);        }
        else {
            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto , type);        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    isUri=false;
                    bitmap = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    employeeImage.setImageBitmap(bitmap);
                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    isUri=true;
                    picUri = imageReturnedIntent.getData();
                    employeeImage.setImageURI(picUri);
                }
                break;
        }
    }
}