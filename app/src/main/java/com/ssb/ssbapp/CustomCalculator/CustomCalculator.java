package com.ssb.ssbapp.CustomCalculator;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ssb.ssbapp.R;

public class CustomCalculator extends LinearLayout {

    private LinearLayout body;
    private Button one,two,three,four,five,six,seven,eight,nine,zero,memory_plus,memory_minus,all_clear,delete,add,sub,divide,multiply, decimal;

    public CustomCalculator(Context context) {
        super(context);
        initControl(context);
    }

    private void initControl(Context context) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.custom_calculator, this);

    }
}
