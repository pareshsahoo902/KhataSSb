package com.ssb.ssbapp.report;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ssb.ssbapp.Adapters.MOneyTransactionAdapter;
import com.ssb.ssbapp.Adapters.TrayTransactionAdapter;
import com.ssb.ssbapp.Customer.CustomerModel;
import com.ssb.ssbapp.Model.TrayMasterModel;
import com.ssb.ssbapp.PdfGenerator.PDFGenerator;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.TransactionModel.MoneyTransactionModel;
import com.ssb.ssbapp.TrayModels.TrayTransactionModel;
import com.ssb.ssbapp.TrayModels.TrayTransactionModel;
import com.ssb.ssbapp.Utils.Constants;
import com.ssb.ssbapp.Utils.SSBBaseActivity;
import com.ssb.ssbapp.Utils.UtilsMethod;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.ssb.ssbapp.Utils.UtilsMethod.isBetween;

public class TrayReport extends SSBBaseActivity {

    private String cid, fileName = "";
    private RecyclerView reportMoneyRecycelr;
    private Button fromDate, toDate;
    Spinner filterSpinner, typeSpinner;

    TextView netBalance, totalIn, totalOut, entriesText, generatePDF, sharePDF;
    TrayTransactionAdapter adapter;
    ArrayList<TrayTransactionModel> totalEntry;
    ArrayList<TrayTransactionModel> modelArrayList;
    private DatabaseReference entryRef;
    Date minimalDate, maximalDate;
    Query query;
    CustomerModel currentModel = null;
    ArrayList<TrayMasterModel> trayList;
    SimpleDateFormat dateFormat;
    Calendar calendar2, calendar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tray_report);
        cid = getIntent().getStringExtra("cid");
        setToolbar(getApplicationContext(), "TRAY REPORT");

        filterSpinner = findViewById(R.id.filterSpinner);
        typeSpinner = findViewById(R.id.typeSpinner);
        fromDate = findViewById(R.id.fromDate);
        toDate = findViewById(R.id.toDate);
        reportMoneyRecycelr = findViewById(R.id.reportTrayRecycelr);
        netBalance = findViewById(R.id.netBalance);
        generatePDF = findViewById(R.id.pdfDownload);
        sharePDF = findViewById(R.id.shareEntry);
        totalIn = findViewById(R.id.totalIn);
        totalOut = findViewById(R.id.totalOut);
        entriesText = findViewById(R.id.entryCount);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        reportMoneyRecycelr.setLayoutManager(layoutManager);
        reportMoneyRecycelr.hasFixedSize();
        entryRef = FirebaseDatabase.getInstance().getReference().child("trayTransaction");
        entryRef.keepSynced(true);
        dateFormat = new SimpleDateFormat("dd-MM-yyyy");


        calendar2 = Calendar.getInstance();
        calendar1 = Calendar.getInstance();

        getTrayList();

        if (cid.equals("")) {
            query = entryRef.orderByChild("kid").equalTo(getLocalSession().getString(Constants.SSB_PREF_KID));
            fileName = "Ssb Khata Tray Report";
        } else {
            query = entryRef.orderByChild("cid").equalTo(cid);
            getUserDetails();

        }

        totalEntry = new ArrayList<>();
        modelArrayList = new ArrayList<>();

        ArrayAdapter<CharSequence> filterAdapter = ArrayAdapter.createFromResource(this,
                R.array.filter, android.R.layout.simple_spinner_item);
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(filterAdapter);

        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,
                R.array.type, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);


        final DatePickerDialog.OnDateSetListener dateSetListener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar2.set(year, monthOfYear, dayOfMonth);
                maximalDate = calendar2.getTime();

                String myFormat = "dd-MM-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
                toDate.setText(sdf.format(calendar2.getTime()));
            }
        };

        final DatePickerDialog.OnDateSetListener dateSetListener1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar1.set(year, monthOfYear, dayOfMonth);
                minimalDate = calendar1.getTime();
                String myFormat = "dd-MM-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
                fromDate.setText(sdf.format(calendar1.getTime()));
            }
        };

        adapter = new TrayTransactionAdapter(modelArrayList, TrayReport.this);

        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(TrayReport.this,
                        dateSetListener1, calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH),
                        calendar1.get(Calendar.DAY_OF_MONTH)).show();


            }
        });

        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(TrayReport.this,
                        dateSetListener2, calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH),
                        calendar2.get(Calendar.DAY_OF_MONTH)).show();

            }
        });


        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                applyFilter(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        fromDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int md, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int md, int before, int count) {
                if (minimalDate != null && maximalDate != null)
                    if (minimalDate.before(maximalDate)) {
                        loadRecyclerFromDate();
                    }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        toDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int md, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int md, int before, int count) {
                if (minimalDate != null && maximalDate != null)
                    if (maximalDate.after(minimalDate)) {
                        loadRecyclerFromDate();
                    }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        generatePDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PDFGenerator pdf = new PDFGenerator(getApplicationContext());

                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                String dates = fromDate.getText().toString() + " - " + toDate.getText().toString();

                boolean isGenerated = false;

                switch (typeSpinner.getSelectedItemPosition()) {
                    case 0:
                        isGenerated = pdf.createTrayStatement(fileName, modelArrayList, trayList, currentModel, dates);
                        break;
                    case 1:
                        isGenerated = pdf.createTrayDuePdf(fileName, modelArrayList, trayList, currentModel, dates);
                        break;
                    case 2:
                        isGenerated = pdf.createTrayDataPdf(fileName, modelArrayList, trayList, currentModel,"got", dates);
                        break;
                    case 3:
                        isGenerated = pdf.createTrayDataPdf(fileName, modelArrayList, trayList, currentModel,"gave", dates);

                        break;
                    case 4:
//                        isGenerated = pdf.createTrayDuePdf(fileName, modelArrayList, trayList, currentModel, dates);

                        break;
                    default:
                        break;
                }

                if (isGenerated) {

                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString(), "SSB/" + fileName + ".pdf");
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(file), "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);

                } else {

                    showMessageToast("Report Failed!\nTry Later", true);
                }

            }
        });

        sharePDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PDFGenerator pdf = new PDFGenerator(getApplicationContext());

                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                String dates = fromDate.getText().toString() + " - " + toDate.getText().toString();


                boolean isGenerated = false;

                switch (typeSpinner.getSelectedItemPosition()) {
                    case 0:
                        isGenerated = pdf.createTrayStatement(fileName, modelArrayList, trayList, currentModel, dates);
                        break;
                    case 1:
                        isGenerated = pdf.createTrayDuePdf(fileName, modelArrayList, trayList, currentModel, dates);
                        break;
                    case 2:
                        isGenerated = pdf.createTrayDataPdf(fileName, modelArrayList, trayList, currentModel,"got", dates);
                        break;
                    case 3:
                        isGenerated = pdf.createTrayDataPdf(fileName, modelArrayList, trayList, currentModel,"gave", dates);

                        break;
                    case 4:
//                        isGenerated = pdf.createTrayDuePdf(fileName, modelArrayList, trayList, currentModel, dates);

                        break;
                    default:
                        break;
                }



                if (isGenerated) {

                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString(), "SSB/" + fileName + ".pdf");
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setDataAndType(Uri.fromFile(file), "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);

                } else {

                    showMessageToast("Report Failed!\nTry Later", true);
                }

            }
        });


        getAllEntryList();

        reportMoneyRecycelr.setAdapter(adapter);


    }

    private void getTrayList() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("tray");
        trayList = new ArrayList<>();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        trayList.add(snapshot1.getValue(TrayMasterModel.class));
                    }
                }

                Toast.makeText(TrayReport.this, "" + trayList.get(0).getName(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                finish();
            }
        });

    }


    private void getAllEntryList() {


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                    totalEntry.add(snapshot1.getValue(TrayTransactionModel.class));
                }

                loadRecyclerFromDate();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                finish();
            }
        });

    }


    private void loadRecyclerFromDate() {

        modelArrayList.clear();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        for (TrayTransactionModel model : totalEntry) {

            try {
                if (isBetween(dateFormat.parse(model.getDate().substring(0, 10)), minimalDate, maximalDate)) {
                    Log.v("paresh", model.getTeid());
                    modelArrayList.add(model);
                }
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(TrayReport.this, "" + e.toString(), Toast.LENGTH_SHORT).show();

            }

        }


        loadCashDetals(modelArrayList);
        adapter.updateList(modelArrayList);
    }


    private void applyFilter(int i) {


        switch (i) {
            case 0:
                //TODO write logic to set calendar for ALL .
                modelArrayList.clear();
                modelArrayList.addAll(totalEntry);
                loadCashDetals(modelArrayList);
                adapter.updateList(modelArrayList);
                break;
            case 1:
                //TODO write logic to set calendar for LAST WEEK.
                Date date = new Date();
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                int day = c.get(Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
                c.add(Calendar.DATE, -day - 7);
                minimalDate = c.getTime();
                fromDate.setText(dateFormat.format(c.getTime()));
                c.add(Calendar.DATE, 6);
                maximalDate = c.getTime();
                toDate.setText(dateFormat.format(c.getTime()));

                break;
            case 2:
                //TODO write logic to set calendar for LAST MONTH.
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.MONTH, -1);
                cal.set(Calendar.DAY_OF_MONTH, 1);
                minimalDate = cal.getTime();
                fromDate.setText(dateFormat.format(cal.getTime()));

                cal.set(Calendar.DAY_OF_MONTH, 31);
                toDate.setText(dateFormat.format(cal.getTime()));

                maximalDate = cal.getTime();

                break;
            case 3:
                //TODO write logic to set calendar for THIS MONTH.
                Calendar cl = Calendar.getInstance();
                cl.set(Calendar.DAY_OF_MONTH, 1);
                minimalDate = cl.getTime();
                fromDate.setText(dateFormat.format(cl.getTime()));

                cl.set(Calendar.DAY_OF_MONTH, 31);
                maximalDate = cl.getTime();
                toDate.setText(dateFormat.format(cl.getTime()));

                break;
            case 4:
                //TODO write logic to set calendar for ZERO.
                Toast.makeText(this, "Choose other options!", Toast.LENGTH_SHORT).show();
                break;

            default:
                getminimalDate();
                break;

        }

    }


    private void getminimalDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        minimalDate = cal.getTime();
        fromDate.setText(dateFormat.format(cal.getTime()));
        toDate.setText(dateFormat.format(cal.getTime()));

        toDate.setText(UtilsMethod.getCurrentDate().substring(0, 10));
        try {
            maximalDate = dateFormat.parse(UtilsMethod.getCurrentDate().substring(0, 10));
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "" + e.toString(), Toast.LENGTH_SHORT).show();
        }

    }


    private void loadCashDetals(ArrayList<TrayTransactionModel> modelArrayList) {

        double totalGot = 0;
        double totalGave = 0;

        for (TrayTransactionModel model : modelArrayList) {
            if (model.getStatus().equals("got")) {
                double t = model.getTotal();
                totalGot += t;
            } else {
                double tg = model.getTotal();
                totalGave += tg;
            }
        }

        totalIn.setText(String.valueOf(totalGot));
        totalOut.setText(String.valueOf(totalGave));
        entriesText.setText(String.valueOf(modelArrayList.size()) + "Entries");
        if (totalGave - totalGot < 0) {
            netBalance.setText("Total:  " + String.valueOf(Math.abs(totalGave - totalGot)));
        } else {
            double num = Math.abs(totalGave - totalGot);
            netBalance.setText("Total:  " + String.valueOf(num));

        }


    }


    private void getUserDetails() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("customers").child(cid);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                currentModel = snapshot.getValue(CustomerModel.class);
                fileName = currentModel.getName() + "Tray" + UtilsMethod.getCurrentDate().substring(18);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


}