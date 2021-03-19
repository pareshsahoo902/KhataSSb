package com.ssb.ssbapp.DataEntry;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ssb.ssbapp.CustomCalculator.CustomCalculator;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.Utils.SSBBaseActivity;

public class PartyEntryActivity extends SSBBaseActivity implements CustomCalculator.CalculatorListner {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_entry);

        setToolbar(getApplicationContext(),"Party Entry");
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
}