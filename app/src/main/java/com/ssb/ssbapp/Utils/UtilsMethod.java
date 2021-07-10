package com.ssb.ssbapp.Utils;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilsMethod {

    public static void deActiveCurrentKahat(){

    }

    public static String getCurrentDate(){
        SimpleDateFormat currentdate = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
        String CurrentDate = currentdate.format(new Date());

        return CurrentDate;
    }



    public static boolean isBetween(Date date, Date dateStart, Date dateEnd) {
        if (date != null && dateStart != null && dateEnd != null) {
            if (date.after(dateStart) && date.before(dateEnd)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * Used for hiding the keyboard on click anywhere in the screen except on the hardware keyboard
     *
     * @param activity
     */
    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }


    /**
     * Used for making the first letter of every word from lower case to upper case
     *
     * @param capString
     * @return
     */
    public static String capitalize(String capString){

        return capString;
    }


    /**
     * Used for detecting the no of days ago, today or yesterday is the date provided.
     * @param time
     * @return
     */
    public static String getPrettierDate(String time){
        String orderDateStr = time;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy-hh:mm:ss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(orderDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long days = (new Date().getTime() - date.getTime()) / 86400000;

        if(days == 0) return "Today";
        else if(days == 1) return "Yesterday";
        else return days + " days ago";
    }


}
