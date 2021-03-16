package com.ssb.ssbapp.DataEntry;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ssb.ssbapp.CustomCalculator.CustomCalculator;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.Utils.SSBBaseActivity;
import com.ssb.ssbapp.Utils.UtilsMethod;

public class MoneyEntryActivity extends SSBBaseActivity implements CustomCalculator.CalculatorListner {

    private CustomCalculator customCalculator;
    private EditText entryText,itemName;
    private LinearLayout entryLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_entry);

        setToolbar(getApplicationContext(), "You got " + getCurrencyStr() + " 0 ");
        customCalculator = findViewById(R.id.custom_calc);
        entryText = findViewById(R.id.calcEntry);
        itemName = findViewById(R.id.itemName);
        entryLayout = findViewById(R.id.detailsLayout);

        entryText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customCalculator.setVisibility(View.VISIBLE);

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
                if (!entryText.getText().toString().equals("")){
                    entryLayout.setVisibility(View.VISIBLE);
                }
                else{
                    entryLayout.setVisibility(View.GONE);

                }

            }
        });

        itemName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customCalculator.setVisibility(View.GONE);
            }
        });
    }





    @Override
    public void onOnePressListner(String btn, String chrSequence) {
        if (chrSequence != null)
            updateLabel(chrSequence);


    }

    private void updateLabel(String chrSequence) {
        entryText.setText(chrSequence);

    }

    @Override
    public void onTwoPressListner(String btn, String chrSequence) {
        if (chrSequence != null)
            updateLabel(chrSequence);

    }

    @Override
    public void onThreePressListner(String btn, String chrSequence) {
        if (chrSequence != null)
            updateLabel(chrSequence);
    }

    @Override
    public void onFourPressListner(String btn, String chrSequence) {
        if (chrSequence != null)
            updateLabel(chrSequence);
    }

    @Override
    public void onFivePressListner(String btn, String chrSequence) {
        if (chrSequence != null)
            updateLabel(chrSequence);
    }

    @Override
    public void onSixPressListner(String btn, String chrSequence) {
        if (chrSequence != null)
            updateLabel(chrSequence);
    }

    @Override
    public void onSevenPressListner(String btn, String chrSequence) {
        if (chrSequence != null)
            updateLabel(chrSequence);
    }

    @Override
    public void onEightPressListener(String btn, String chrSequence) {
        if (chrSequence != null)
            updateLabel(chrSequence);
    }

    @Override
    public void onNinePressListner(String btn, String chrSequence) {
        if (chrSequence != null)
            updateLabel(chrSequence);
    }

    @Override
    public void onZeroPressListner(String btn, String chrSequence) {
        if (chrSequence != null)
            updateLabel(chrSequence);


    }

    @Override
    public void onMemoryPlusPressListner(String btn, String chrSequence) {
        if (chrSequence != null)
            updateLabel(chrSequence);
    }

    @Override
    public void onMemoryMinusPressListner(String btn, String chrSequence) {
        if (chrSequence != null)
            updateLabel(chrSequence);
    }

    @Override
    public void onAllClearPressListner(String btn, String chrSequence) {
            updateLabel(chrSequence);
    }

    @Override
    public void onDeletePressListner(String btn, String chrSequence) {
            updateLabel(chrSequence);
    }

    @Override
    public void onEqualsPressListner(String btn, String chrSequence) {
        if (chrSequence != null)
            updateLabel(chrSequence);
    }

    @Override
    public void onAddButtonPressListner(String btn, String chrSequence) {
        if (chrSequence != null)
            updateLabel(chrSequence);
    }

    @Override
    public void onSubButtonPressListner(String btn, String chrSequence) {
        if (chrSequence != null)
            updateLabel(chrSequence);
    }

    @Override
    public void onDivideButtonPressListner(String btn, String chrSequence) {
        if (chrSequence != null)
            updateLabel(chrSequence);
    }

    @Override
    public void onMultiplyButtonPressListner(String btn, String chrSequence) {
        if (chrSequence != null)
            updateLabel(chrSequence);
    }

    @Override
    public void onDecimalPointPressListner(String btn, String chrSequence) {
        if (chrSequence != null)
            updateLabel(chrSequence);
    }





    /**
     * Used for dismissing the hardware keyboard on touch anywhere on the screen except the hardware keyboard
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
            int scrcoords[] = new int[2] ;
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                UtilsMethod.hideKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }

}