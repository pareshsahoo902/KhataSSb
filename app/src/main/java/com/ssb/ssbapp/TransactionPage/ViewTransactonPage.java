package com.ssb.ssbapp.TransactionPage;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.ssb.ssbapp.DialogHelper.ShareablePicker;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.TransactionModel.MoneyTransactionModel;
import com.ssb.ssbapp.Utils.Constants;
import com.ssb.ssbapp.Utils.SSBBaseActivity;
import com.ssb.ssbapp.Utils.UtilsMethod;

public class ViewTransactonPage extends SSBBaseActivity implements ShareablePicker.SharePickerListner {

    private MoneyTransactionModel moneyTransactionModel;
    private TextView deleteEntry , shareEntry;
    private Button editEntry;
    private String shareableText="";
    private ImageView entryImage;
    private TextView amountTotal , desc, entryText , date , balance , gotText,gaveText;
    private FrameLayout gaveLayout, gotLayout;
    private DatabaseReference transactionRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_transacton_page);
        moneyTransactionModel = (MoneyTransactionModel) getIntent().getSerializableExtra(Constants.SSB_MONEYTRANSACTION_INTENT);

        setToolbar(getApplicationContext(), "You "+UtilsMethod.capitalize(moneyTransactionModel.getStatus())+"  "+getCurrencyStr()+moneyTransactionModel.getTotal());

        editEntry =findViewById(R.id.editEntry);
        deleteEntry =findViewById(R.id.deleteEntry);
        shareEntry =findViewById(R.id.shareEntry);

        //details item
        amountTotal = findViewById(R.id.amountText);
        desc = findViewById(R.id.descriptionText);
        entryText = findViewById(R.id.itemText);
        date = findViewById(R.id.dateText);
        balance = findViewById(R.id.balanceText);
        gotText = findViewById(R.id.gotText);
        gaveText = findViewById(R.id.gaveText);
        gaveLayout =findViewById(R.id.giveFrame);
        gotLayout = findViewById(R.id.gotFrame);
        entryImage = findViewById(R.id.entryImage);

        transactionRef= FirebaseDatabase.getInstance().getReference().child("customerTransaction");
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
                shareablePicker.show(getSupportFragmentManager(),"pick share intent");




//
            }
        });
        deleteEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(ViewTransactonPage.this)
                        .setTitle(Html.fromHtml("<font color='#03503E'>Delete Entry</font>"))
                        .setMessage("Are you sure you want to delete this Entry  ?\n"+"₹"+moneyTransactionModel.getTotal())
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                transactionRef.child(moneyTransactionModel.getCeid()).removeValue();
                                finish();
                            }
                        })
                        .setNegativeButton("NO", null)
                        .show();


            }
        });


    }

    private void loadDetails() {


        entryText.setText(moneyTransactionModel.getEntriesText());
        date.setText(moneyTransactionModel.getDate());
        desc.setText(moneyTransactionModel.getDescription());
        amountTotal.setText("Amt:" + getCurrencyStr() + String.valueOf(moneyTransactionModel.getTotal()));
        balance.setText("Bal:" + getCurrencyStr() + String.valueOf(moneyTransactionModel.getTotal()));


        if (moneyTransactionModel.getImageurl()!=null && !moneyTransactionModel.getImageurl().equals("")){

            Picasso.with(getApplicationContext()).load(moneyTransactionModel.getImageurl())
                    .error(R.drawable.backed_up)
                    .into(entryImage);
        }

        if (moneyTransactionModel.getStatus().equals("got")) {
            gaveText.setVisibility(View.INVISIBLE);
            gotText.setText(getCurrencyStr() + String.valueOf(moneyTransactionModel.getTotal()));
        } else {
            gotLayout.setVisibility(View.INVISIBLE);
            gaveText.setVisibility(View.VISIBLE);
            gaveText.setText(getCurrencyStr() + String.valueOf(moneyTransactionModel.getTotal()));
        }

        shareableText = "*Money "+moneyTransactionModel.getStatus()+" "+getCurrencyStr()+moneyTransactionModel.getTotal()+"*\nDetails:\n"
                +moneyTransactionModel.getEntriesText()+"\nDate:\n"+moneyTransactionModel.getDate();
    }

    @Override
    public void pickshareIntent(int type) {

       switch (type){
           case 0:
               Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setData(Uri.parse("smsto:"));
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address"  , new String("8917351934"));
                smsIntent.putExtra("sms_body"  , shareableText);

                try {
                    startActivity(smsIntent);
                    finish();
                    Log.i("Finished sending SMS...", "");
                } catch (android.content.ActivityNotFoundException ex) {
                    showMessageToast(
                            "SMS faild, please try again later.",true);
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
                   showMessageToast("Whatsapp have not been installed.",true);
               }

               break;

       }
    }
}