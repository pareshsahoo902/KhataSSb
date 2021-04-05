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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.ssb.ssbapp.DialogHelper.ImagePickerDailog;
import com.ssb.ssbapp.Model.EmployeeModel;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.SuccessScreens.SucessActivity;
import com.ssb.ssbapp.Utils.Constants;
import com.ssb.ssbapp.Utils.SSBBaseActivity;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import pub.devrel.easypermissions.EasyPermissions;

import static com.ssb.ssbapp.Utils.Constants.SSB_EMPLOYEE_DETAILS;

public class AddEmploeePayroll extends SSBBaseActivity implements ImagePickerDailog.ImagePickerListner {
    Button btn;
    EditText joiningDateText,salary;
    Calendar myCalendar;
    ImageView editPic, employeeImage;
    EmployeeModel mEmployee;

    private DatabaseReference userReference;

    FirebaseAuth mAuth;
    Bitmap bitmap;
    private TextView employeeDetailsText;
    Uri picUri;
    String emploee_details = "",picDowloadUrl = "";
    boolean isUri = true;

    private StorageTask uploadtask;
    private StorageReference userStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_emploee_payroll);

        setToolbar(getApplicationContext(), "Payroll Details");
        emploee_details = getIntent().getStringExtra(Constants.SSB_EMPLOYEE_DETAILS);

        mEmployee = (EmployeeModel) getIntent().getSerializableExtra(SSB_EMPLOYEE_DETAILS+"model");
        userReference = FirebaseDatabase.getInstance().getReference();


        userStorage= FirebaseStorage.getInstance().getReference().child("SSB").child("Employee Passport Pic").child("employee"+mEmployee.getAadhar()+".jpg");

        btn = findViewById(R.id.createButton);
        joiningDateText = findViewById(R.id.joiningDateText);
        salary = findViewById(R.id.salarytext);
        editPic = findViewById(R.id.editPic);
        employeeImage = findViewById(R.id.employeeimage);
        employeeDetailsText = findViewById(R.id.employeeDetailsText);

        mAuth = FirebaseAuth.getInstance();

        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, perms)) {

            EasyPermissions.requestPermissions(this, "Need some permissions", 101, perms);
        }

        employeeDetailsText.setText(emploee_details + "\n");
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
                validateAndAddToDatabase();

            }
        });
    }

    private void validateAndAddToDatabase() {
        if (TextUtils.isEmpty(salary.getText().toString())){
            showMessageToast("Enter Salary Properly !",true);
        }
        else if (TextUtils.isEmpty(joiningDateText.getText().toString())){
            showMessageToast("Enter Date of Joining",true);
        }
        else {
            showProgress();
            mAuth.createUserWithEmailAndPassword(mEmployee.getLoginID(),mEmployee.getPassword())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                if (isUri){
                                    UploadUserPicUri();
                                }
                                else {
                                    UploadUserPicBitmap();
                                }

                            }
                            else{
                                dismissProgress();
                                Log.v("paresh",task.getException().toString());
                                showMessageToast("Some Error Occured !\nCheck your Internet\n"+mEmployee.getLoginID()+mEmployee.getPassword(),true);
                            }
                        }
                    });
        }

    }


    //upload bitmap image
    private void UploadUserPicBitmap() {
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
                            startAddingToDB(picDowloadUrl);
                        }
                    });


                }
            });


        }else {
            dismissProgress();
            showMessageToast("Enter a Image !",false);
        }


    }

    //puload uri image
    private void UploadUserPicUri() {

        if(picUri!=null){

            final StorageReference filepath = userStorage;
            uploadtask=filepath.putFile(picUri);

            uploadtask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()){
                        dismissProgress();
                        showMessageToast("Error Uploading Pic!",true);
                    }
                    return filepath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    if (task.isSuccessful()){
                        picDowloadUrl = task.getResult().toString();

                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("name",mEmployee.getName());
                        hashMap.put("admin",false);
                        hashMap.put("company","ssb");
                        hashMap.put("branch",mEmployee.getBranchAssinged());
                        hashMap.put("loginID",mEmployee.getLoginID());
                        hashMap.put("password",mEmployee.getPassword());
                        hashMap.put("type",mEmployee.getType());

                        userReference.child("users").child(mEmployee.getAadhar()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()){
                                    startAddingToDB(picDowloadUrl);

                                }
                                else
                                {
                                    dismissProgress();
                                    showMessageToast("SomeError Occurred",true);
                                }

                            }
                        });

                    }

                }
            });

        }else {
            dismissProgress();
            showMessageToast("Enter a Image !",false);
        }

    }



    //save Data to Firebase
    private void startAddingToDB( String imageUrl) {

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("name",mEmployee.getName());
        hashMap.put("admin",false);
        hashMap.put("aadhar",mEmployee.getAadhar());
        hashMap.put("contact",mEmployee.getContact());
        hashMap.put("branch",mEmployee.getBranchAssinged());
        hashMap.put("type",mEmployee.getType());
        hashMap.put("profile_image",imageUrl);
        hashMap.put("date_of_joining",joiningDateText.getText().toString());
        hashMap.put("salary",Integer.parseInt(salary.getText().toString().trim()));

        userReference.child("staff").child(mEmployee.getAadhar()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    dismissProgress();
                    startActivity(new Intent(AddEmploeePayroll.this, SucessActivity.class)
                            .putExtra(Constants.SSB_SUCESS_INTENT,"staff")
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));

                }
                else
                {
                    dismissProgress();
                    showMessageToast(task.getException().getMessage()+"SomeError Occurred",true);
                }

            }
        });



    }


    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        joiningDateText.setText(sdf.format(myCalendar.getTime()));
        showMessageToast("working", false);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
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
                    employeeImage.setImageBitmap(bitmap);
                }

                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    isUri = true;
                    picUri = imageReturnedIntent.getData();
                    employeeImage.setImageURI(picUri);
                }
                break;
        }
    }



}