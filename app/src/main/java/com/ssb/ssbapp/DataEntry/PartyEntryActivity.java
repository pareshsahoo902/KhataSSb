package com.ssb.ssbapp.DataEntry;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ssb.ssbapp.CustomCalculator.CustomCalculator;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.Utils.SSBBaseActivity;

import java.util.ArrayList;
import java.util.List;

public class PartyEntryActivity extends SSBBaseActivity implements CustomCalculator.CalculatorListner {
    private EditText entryText,itemName;
    private TextView entries_text;
    private String descText="";
    private Button saveEntry;
    private LinearLayout entryLayout;
    private List<Double> itemBalanceList;
    private double totalAmount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_entry);

        entryText = findViewById(R.id.calcEntry);
        itemName = findViewById(R.id.itemName);
        entries_text = findViewById(R.id.entries_text);
        saveEntry = findViewById(R.id.saveEntry);
        entryLayout = findViewById(R.id.detailsLayout);
        itemBalanceList = new ArrayList<>();

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

        setToolbar(getApplicationContext(),"Party Entry");
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
//            itemBalanceList.add(Double.parseDouble(chrSequence));
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


}