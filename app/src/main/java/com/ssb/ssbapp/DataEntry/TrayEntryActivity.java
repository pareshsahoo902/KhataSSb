package com.ssb.ssbapp.DataEntry;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.ssb.ssbapp.DialogHelper.ImagePickerDailog;
import com.ssb.ssbapp.Model.KhataModel;
import com.ssb.ssbapp.Model.TrayMasterModel;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.SuccessScreens.SucessActivity;
import com.ssb.ssbapp.TrayModels.TrayModelItem;
import com.ssb.ssbapp.TrayModels.TrayTransactionModel;
import com.ssb.ssbapp.Utils.Constants;
import com.ssb.ssbapp.Utils.SSBBaseActivity;
import com.ssb.ssbapp.Utils.UtilsMethod;
import com.ssb.ssbapp.ViewHolder.KhataListViewHolder;
import com.ssb.ssbapp.ViewHolder.TrayDetailViewHolder;
import com.ssb.ssbapp.ViewHolder.TrayListViewHolder;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class TrayEntryActivity extends SSBBaseActivity implements ImagePickerDailog.ImagePickerListner {

    private String type ="";
    private RecyclerView availableRecycler;
    private ImageView billIMageMoney;
    private Uri picUri = null;
    private List<TrayModelItem> trayLists;
    private EditText desctray;
    private Button saveEntry , trayIn , trayOut;
    private TextView dateTextBtn ,imageTextButton;
    private StorageTask uploadtask;
    private StorageReference userStorage;
    private boolean isUri;
    private Bitmap bitmap = null;

    private DatabaseReference trayRef,trayTransactionRef;
    private FirebaseRecyclerOptions<TrayMasterModel> trayFirebaseRecyclerOptions;
    private FirebaseRecyclerAdapter<TrayMasterModel, TrayDetailViewHolder> trayFirebaseRecyclerAdapter;
    private String CurrentDate;
    private String picDowloadUrl = "";
    private int balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tray_entry);

        type = getIntent().getStringExtra("type");

        setToolbar(getApplicationContext(),"Tray Entry"+" "+ UtilsMethod.capitalize(type));

        desctray=findViewById(R.id.desctray);
        saveEntry=findViewById(R.id.savetrayentry);
        trayIn=findViewById(R.id.trayinbtn);
        trayOut=findViewById(R.id.trayoutbtn);
        dateTextBtn = findViewById(R.id.dateTextBtn);
        imageTextButton = findViewById(R.id.imageTextBtn);
        billIMageMoney = findViewById(R.id.billIMageMoney);


        availableRecycler=findViewById(R.id.availableTrayRecycler);
        availableRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        availableRecycler.hasFixedSize();

        userStorage = FirebaseStorage.getInstance().getReference().child("SSB").child("Tray Transaction Image");

        trayRef = FirebaseDatabase.getInstance().getReference().child("tray");
        trayTransactionRef = FirebaseDatabase.getInstance().getReference().child("trayTransaction");
        trayRef.keepSynced(true);
        trayTransactionRef.keepSynced(true);

        dateTextBtn.setText(CurrentDate.substring(0,10));
        CurrentDate = getLocalSession().getString(Constants.SSB_PREF_DATE);

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

        saveEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final  boolean save = (boolean) saveEntry.getTag();
                if (save){
                    if (getTotalTray()>0){
                        saveEntryToDB();
                    }else {
                        showMessageToast("Total Tray cannot be Zero!",true);
                    }
                }else {
                    setToolbar(getApplicationContext(), "You "+type+" " + getTotalTray()+" Trays");
                    saveEntry.setTag(true);
                    saveEntry.setText("Save");
                }

            }
        });


        imageTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePickerDailog dailog = new ImagePickerDailog();
                dailog.show(getSupportFragmentManager(), "Pick Image");
            }
        });

        dateTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(TrayEntryActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        trayLists=new ArrayList<>();
        loadAvailanleTrays();



    }

    private void saveEntryToDB() {
        String teid = UUID.randomUUID().toString();
        showProgress();
        if (isUri){
            if (picUri != null) {
                UploadUserPicUri(teid);
            } else {
                startAddingToDB("", teid);
            }
        }else{
            if (bitmap != null) {
                UploadUserPicBitmap(teid);

            } else {
                startAddingToDB("", teid);
            }
        }

    }

    private void UploadUserPicBitmap(String teid) {

        if (bitmap != null) {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = userStorage.child(teid + ".jpg").putBytes(data);
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
                            startAddingToDB(picDowloadUrl, teid);
                        }
                    });


                }
            });


        } else {
            dismissProgress();
            showMessageToast("Enter a Image !", false);
        }

    }

    private void UploadUserPicUri(String teid) {

        if (picUri != null) {

            final StorageReference filepath = userStorage.child(teid + ".jpg");
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
                        startAddingToDB(picDowloadUrl, teid);

                    }
                }
            });
        }

    }

    private void startAddingToDB(String picUrl, String teid) {

        TrayTransactionModel model = new TrayTransactionModel(teid,getLocalSession().getString(Constants.SSB_PREF_CID), getLocalSession().getString(Constants.SSB_PREF_KID),
                CurrentDate, type,getTotalTray(),picUrl,desctray.getText().toString(),getTotalTray(),(ArrayList<TrayModelItem>) trayLists);

        if (model.getCid()!=null){
            trayTransactionRef.child(teid).setValue(model);
            startActivity(new Intent(TrayEntryActivity.this, SucessActivity.class).putExtra(Constants.SSB_SUCESS_INTENT, "money"));
            finish();

        }


    }

    private void loadAvailanleTrays() {

        trayFirebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<TrayMasterModel>().setQuery(trayRef, TrayMasterModel.class).build();
        trayFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<TrayMasterModel, TrayDetailViewHolder>(trayFirebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull TrayDetailViewHolder trayListViewHolder, int i, @NonNull TrayMasterModel trayMasterModel) {

                final boolean[] isAdded = {false};
                trayListViewHolder.trayName.setText(UtilsMethod.capitalize(trayMasterModel.getName()));
                trayListViewHolder.available.setText("0");
                trayListViewHolder.add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveEntry.setTag(false);
                        if (isAdded[0]){
                            isAdded[0] =false;
                            trayListViewHolder.enterTrayText.setFocusable(true);
                            deleteTrayFromList(trayMasterModel.getTid());
                            trayListViewHolder.enterTrayText.setText("");
                            saveEntry.setText("Total ="+String.valueOf(getTotalTray()));
                            trayListViewHolder.add.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_add_circle_outline_24));
                            trayListViewHolder.add.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDark)));
                        }else {
                            isAdded[0]=true;
                            trayListViewHolder.enterTrayText.setFocusable(false);
                            hideKeybaord(v);
                            trayLists.add(new TrayModelItem(trayMasterModel.getTid(),trayMasterModel.getName(), Integer.parseInt(trayListViewHolder.enterTrayText.getText().toString())));
                            trayListViewHolder.add.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_close_24));
                            trayListViewHolder.add.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                            saveEntry.setText("Total ="+String.valueOf(getTotalTray()));

                        }

                    }
                });


            }

            @NonNull
            @Override
            public TrayDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new TrayDetailViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.tray_detail_item,parent,false));
            }
        };

        availableRecycler.setAdapter(trayFirebaseRecyclerAdapter);
        trayFirebaseRecyclerAdapter.startListening();


    }

    private int getTotalTray(){
        int total =0;

        for (TrayModelItem item: trayLists){
            total+=item.getTotalCount();
        }
        return total;
    }

    private void deleteTrayFromList(String tid) {
        for (TrayModelItem modelItem: trayLists){
            if (modelItem.getId().equals(tid)){
                trayLists.remove(modelItem);
                showMessageToast("Tray Removed",false);
            }
        }
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

    private void hideKeybaord(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
    }
}