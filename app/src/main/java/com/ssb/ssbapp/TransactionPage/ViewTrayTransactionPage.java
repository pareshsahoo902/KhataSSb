package com.ssb.ssbapp.TransactionPage;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.ssb.ssbapp.DialogHelper.ShareablePicker;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.TransactionModel.MoneyTransactionModel;
import com.ssb.ssbapp.TrayModels.TrayModelItem;
import com.ssb.ssbapp.TrayModels.TrayTransactionModel;
import com.ssb.ssbapp.Utils.Constants;
import com.ssb.ssbapp.Utils.SSBBaseActivity;
import com.ssb.ssbapp.Utils.UtilsMethod;

import java.util.ArrayList;

public class ViewTrayTransactionPage extends SSBBaseActivity implements ShareablePicker.SharePickerListner {

    private TrayTransactionModel transactionModel;
    private TextView deleteEntry, shareEntry;
    private Button editEntry;
    private String shareableText = "";
    private ImageView entryImage;
    private TextView trayCountText, descriptionText, detailText, dateText, balance, gotText, gaveText;
    private FrameLayout gaveLayout, gotLayout;
    private DatabaseReference transactionRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tray_transaction_page);
        transactionModel = (TrayTransactionModel) getIntent().getSerializableExtra(Constants.SSB_TRAYTRANSACTION_INTENT);

        setToolbar(getApplicationContext(), "You " + UtilsMethod.capitalize(transactionModel.getStatus()) + "  " + transactionModel.getTotal() + " Trays");


        editEntry = findViewById(R.id.editEntry);
        deleteEntry = findViewById(R.id.deleteEntry);
        shareEntry = findViewById(R.id.shareEntry);
        entryImage = findViewById(R.id.entryImage);


        dateText = findViewById(R.id.dateText);
        detailText = findViewById(R.id.detailText);
        trayCountText = findViewById(R.id.trayCountText);
        descriptionText = findViewById(R.id.descriptionText);
        gaveText = findViewById(R.id.gaveText);
        gotText = findViewById(R.id.gotText);
        gotLayout = findViewById(R.id.gotFrame);
        gaveLayout = findViewById(R.id.giveFrame);
        balance = findViewById(R.id.balanceText);

        transactionRef = FirebaseDatabase.getInstance().getReference().child("trayTransaction");
        loadDetails();


        editEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        shareEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShareablePicker shareablePicker = new ShareablePicker();
                shareablePicker.show(getSupportFragmentManager(), "pick share intent");


//
            }
        });
        deleteEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(ViewTrayTransactionPage.this)
                        .setTitle(Html.fromHtml("<font color='#03503E'>Delete Entry</font>"))
                        .setMessage("Are you sure you want to delete this Entry  ?\n" + transactionModel.getTotal() + " Trays")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                transactionRef.child(transactionModel.getTeid()).removeValue();
                                finish();
                            }
                        })
                        .setNegativeButton("NO", null)
                        .show();


            }
        });


    }

    private void loadDetails() {
        shareableText = "*TRAY " + transactionModel.getStatus() + " " + transactionModel.getTotal() + "*\nDetails:\n"
                + getDetailText(transactionModel.getModelItemArrayList()) + "\nDate:\n" + transactionModel.getDate();

        if (!transactionModel.getImageUrl().equals("") && transactionModel.getImageUrl() != null) {
            Picasso.with(getApplicationContext())
                    .load(transactionModel.getImageUrl())
                    .error(R.drawable.backed_up)
                    .into(entryImage);

        }

        dateText.setText("Date: " + transactionModel.getDate().substring(0, 10));
        detailText.setText(transactionModel.getDescription());
        descriptionText.setVisibility(View.VISIBLE);
        trayCountText.setText("Total: " + String.valueOf(transactionModel.getTotal()) + " Trays");
        if (transactionModel.getModelItemArrayList().size() > 0) {
            descriptionText.setText("Trays:" + getDetailText(transactionModel.getModelItemArrayList()));

        }


        if (transactionModel.getStatus().equals("got")) {
            gaveText.setVisibility(View.INVISIBLE);
            gotText.setText(String.valueOf(transactionModel.getTotal()));
        } else {
            gotLayout.setVisibility(View.INVISIBLE);
            gaveText.setVisibility(View.VISIBLE);
            gaveText.setText(String.valueOf(transactionModel.getTotal()));
        }
    }


    public String getDetailText(ArrayList<TrayModelItem> trayModelItems) {
        String detailStr = "";

        for (TrayModelItem item : trayModelItems) {
            detailStr += "\n" + item.getName() + ": " + item.getTotalCount();
        }

        return detailStr;
    }

    @Override
    public void pickshareIntent(int type) {
        switch (type) {
            case 0:
                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setData(Uri.parse("smsto:"));
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address", new String("7008071464"));
                smsIntent.putExtra("sms_body", shareableText);

                try {
                    startActivity(smsIntent);
                    finish();
                    Log.i("Finished sending SMS...", "");
                } catch (android.content.ActivityNotFoundException ex) {
                    showMessageToast(
                            "SMS faild, please try again later.", true);
                }
                break;
            case 1:
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, shareableText);
                try {
                    startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    showMessageToast("Whatsapp have not been installed.", true);
                }

                break;

        }
    }
}