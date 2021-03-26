package com.ssb.ssbapp.CustomCalculator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ssb.ssbapp.R;

public class CustomCalculator extends LinearLayout implements View.OnClickListener {

    private CalculatorListner mListner;
    LayoutInflater inflater;
    private double opreand1, opreand2;
    private String opreator = "";

    String calctext = "";
    boolean isDecimal = false;
    private LinearLayout body;
    private Button one, two, three,
            four, five, six, seven,
            eight, nine, zero, memory_plus,
            memory_minus, all_clear, delete, equals,
            add, sub, divide, multiply, decimal;

    public interface CalculatorListner {
        void onOnePressListner(String btn, String chrSequence);

        void onTwoPressListner(String btn, String chrSequence);

        void onThreePressListner(String btn, String chrSequence);

        void onFourPressListner(String btn, String chrSequence);

        void onFivePressListner(String btn, String chrSequence);

        void onSixPressListner(String btn, String chrSequence);

        void onSevenPressListner(String btn, String chrSequence);

        void onEightPressListener(String btn, String chrSequence);

        void onNinePressListner(String btn, String chrSequence);

        void onZeroPressListner(String btn, String chrSequence);

        void onMemoryPlusPressListner(String btn, String chrSequence);

        void onMemoryMinusPressListner(String btn, String chrSequence);

        void onAllClearPressListner(String btn, String chrSequence);

        void onDeletePressListner(String btn, String chrSequence);

        void onEqualsPressListner(String btn, String chrSequence);

        void onAddButtonPressListner(String btn, String chrSequence);

        void onSubButtonPressListner(String btn, String chrSequence);

        void onDivideButtonPressListner(String btn, String chrSequence);

        void onMultiplyButtonPressListner(String btn, String chrSequence);

        void onDecimalPointPressListner(String btn, String chrSequence);

    }

    public CustomCalculator(Context context) {
        super(context);

        init();
    }

    public CustomCalculator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomCalculator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    public CustomCalculator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    private void init() {

        inflater = (LayoutInflater)
                getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.custom_calculator, this);

        mListner = (CalculatorListner) getContext();


        initViews();
    }

    private void initViews() {

        body = findViewById(R.id.cust_calc);
        one = findViewById(R.id.btn1);
        two = findViewById(R.id.btn2);
        three = findViewById(R.id.btn3);
        four = findViewById(R.id.btn4);
        five = findViewById(R.id.btn5);
        six = findViewById(R.id.btn6);
        seven = findViewById(R.id.btn7);
        eight = findViewById(R.id.btn8);
        nine = findViewById(R.id.btn9);
        zero = findViewById(R.id.btn0);
        all_clear = findViewById(R.id.btnClear);
        delete = findViewById(R.id.btnDelete);
        decimal = findViewById(R.id.btnDot);
        add = findViewById(R.id.btnAdd);
        sub = findViewById(R.id.btnSubtract);
        divide = findViewById(R.id.btnDivide);
        multiply = findViewById(R.id.btnMultiply);
        memory_minus = findViewById(R.id.btnMminus);
        memory_plus = findViewById(R.id.btnMPlus);
        equals = findViewById(R.id.btnEquals);

        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);
        six.setOnClickListener(this);
        seven.setOnClickListener(this);
        eight.setOnClickListener(this);
        nine.setOnClickListener(this);
        zero.setOnClickListener(this);
        all_clear.setOnClickListener(this);
        decimal.setOnClickListener(this);
        delete.setOnClickListener(this);
        add.setOnClickListener(this);
        sub.setOnClickListener(this);
        divide.setOnClickListener(this);
        multiply.setOnClickListener(this);
        equals.setOnClickListener(this);
        memory_plus.setOnClickListener(this);
        memory_minus.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn0:
                calctext += "0";
                mListner.onZeroPressListner("0", calctext);
                break;
            case R.id.btn1:
                calctext += "1";
                mListner.onOnePressListner("1", calctext);
                break;
            case R.id.btn2:
                calctext += "2";
                mListner.onTwoPressListner("2", calctext);
                break;
            case R.id.btn3:
                calctext += "3";
                mListner.onThreePressListner("3", calctext);
                //TODO
                break;
            case R.id.btn4:
                calctext += "4";
                mListner.onFourPressListner("4", calctext);
                //TODO
                break;
            case R.id.btn5:
                //TODO
                calctext += "5";
                mListner.onFivePressListner("5", calctext);
                break;
            case R.id.btn6:
                calctext += "6";
                mListner.onSixPressListner("6", calctext);
                //TODO
                break;
            case R.id.btn7:
                calctext += "7";
                mListner.onSevenPressListner("7", calctext);
                //TODO
                break;
            case R.id.btn8:
                calctext += "8";
                mListner.onEightPressListener("8", calctext);
                //TODO
                break;
            case R.id.btn9:
                calctext += "9";
                mListner.onNinePressListner("9", calctext);
                //TODO
                break;
            case R.id.btnClear:
                //TODO
                calctext = "";
                isDecimal = false;
                mListner.onAllClearPressListner(" ", calctext);
                break;
            case R.id.btnDelete:
                //TODO
                if (calctext != null && !calctext.equals(""))
                    calctext = calctext.substring(0, calctext.length() - 1);
                mListner.onDeletePressListner(" ", calctext);
                break;
            case R.id.btnDot:
                //TODO
                if (!isDecimal) {
                    calctext += ".";
                    mListner.onNinePressListner(".", calctext);
                    isDecimal = true;
                } else {

                }
                break;
            case R.id.btnMPlus:
                //TODO
                calctext="";
                mListner.onMemoryPlusPressListner("M+", calctext);
                break;
            case R.id.btnMminus:
                //TODO
                calctext = calctext + "M+";
                mListner.onMemoryMinusPressListner("M-", calctext);
                break;
            case R.id.btnAdd:
                //TODO
                opreand1 = Double.parseDouble(calctext);
                calctext += "+";
                mListner.onAddButtonPressListner("+", calctext);
                opreator = "+";
                calctext = "";
                break;
            case R.id.btnSubtract:
                //TODO
                opreand1 = Double.parseDouble(calctext);
                calctext += "-";
                mListner.onSubButtonPressListner("-", calctext);

                opreator = "-";
                calctext = "";
                break;
            case R.id.btnDivide:
                //TODO
                opreand1 = Double.parseDouble(calctext);
                calctext += "/";

                mListner.onDivideButtonPressListner("/", calctext);

                opreator = "/";
                calctext = "";
                break;
            case R.id.btnMultiply:
                //TODO
                opreand1 = Double.parseDouble(calctext);
                calctext+="*";
                mListner.onMultiplyButtonPressListner("*", calctext);
                opreator = "*";
                calctext = "";
                break;
            case R.id.btnEquals:
                //TODO
                if (opreator == "+") {
                    calctext = String.valueOf(opreand1 + Double.parseDouble(calctext));
                } else if (opreator == "-") {
                    calctext = String.valueOf(opreand1 - Double.parseDouble(calctext));
                } else if (opreator == "/") {
                    calctext = String.valueOf(opreand1 / Double.parseDouble(calctext));
                } else if (opreator == "*") {
                    calctext = String.valueOf(opreand1 * Double.parseDouble(calctext));
                }
                mListner.onEqualsPressListner("=", calctext);
                break;

        }

    }


}
