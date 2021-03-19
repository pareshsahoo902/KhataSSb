package com.ssb.ssbapp.Sessions;

import android.content.Context;
import android.content.SharedPreferences;


public class LocalSession {


    static Context context;


    public LocalSession(Context context) {
        this.context = context;

    }

    /**
     * for clearing the complete session of the application
     */
    public static void clear() {
        SharedPreferences.Editor edit = getPreferences().edit();
        edit.clear().apply();
    }

    public static SharedPreferences getPreferences() {
        return context.getSharedPreferences("SSB_KHATA_PREF", Context.MODE_PRIVATE);
    }

    public static String getString(String key) {
        return getString(key, "");
    }

    public static String getString(String key, String fallback) {
        String value = fallback;
        try {
            value = getPreferences().getString(key, fallback);
        } catch (Exception e) {
            value = fallback;
        }
        return value;
    }

    public static boolean putString(String key, String value) {
        return getPreferences().edit().putString(key, value).commit();
    }

    public static int getInt(String key) {
        return getInt(key, 0);
    }

    public static int getInt(String key, int fallback) {
        int value = fallback;
        try {
            value = getPreferences().getInt(key, fallback);
        } catch (Exception e) {
            value = fallback;
        }
        return value;
    }

    public static boolean putInt(String key, int value) {
        return getPreferences().edit().putInt(key, value).commit();
    }

    public long getLong(String key) {
        return getLong(key, 0L);
    }

    public long getLong(String key, long fallback) {
        long value = fallback;
        try {
            value = getPreferences().getLong(key, fallback);
        } catch (Exception e) {
            value = fallback;
        }
        return value;
    }

    public static boolean putLong(String key, long value) {
        return getPreferences().edit().putLong(key, value).commit();
    }

    public float getFloat(String key) {
        return getFloat(key, 0F);
    }

    public float getFloat(String key, float fallback) {
        float value = fallback;
        try {
            value = getPreferences().getFloat(key, fallback);
        } catch (Exception e) {
            value = fallback;
        }
        return value;
    }

    public boolean putFloat(String key, float value) {
        return getPreferences().edit().putFloat(key, value).commit();
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean fallback) {
        boolean value = fallback;

        try {
            value = getPreferences().getBoolean(key, fallback);
        } catch (Exception e) {
            value = fallback;
        }
        return value;
    }

    public boolean putBoolean(String key, boolean value) {
        return getPreferences().edit().putBoolean(key, value).commit();
    }
}
