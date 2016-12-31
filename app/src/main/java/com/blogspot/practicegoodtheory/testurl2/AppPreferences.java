package com.blogspot.practicegoodtheory.testurl2;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by dcooper on 7/15/2015.
 */
public class AppPreferences
{
    public static boolean isOn(Context context)
    {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getBoolean(SettingsActivity.KEY_PREF_IS_ON, false);
    }

    public static void setOnOff(Context context, boolean isOn)
    {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        Log.d("PREF", "ABOUT TO save");
        editor.putBoolean(SettingsActivity.KEY_PREF_IS_ON, isOn);
        editor.commit();
        Log.d("PREF", "saved "+(isOn?"ON":"OFF"));
    }

    public static String getUrlName(Context context)
    {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String urlName = sharedPref.getString(SettingsActivity.KEY_PREF_URL_NAME, "");
        return urlName;
    }

    public static String getSearchString(Context context)
    {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String searchString = sharedPref.getString(SettingsActivity.KEY_PREF_SEARCH_STRING, "");
        return searchString;
    }

    public static String getPhoneNumber(Context context)
    {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String phoneNumber = sharedPref.getString(SettingsActivity.KEY_PREF_PHONE_NUMBER, "");
        return phoneNumber;
    }

    public static int getCheckFrequency(Context context)
    {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String strCheckFrequency = sharedPref.getString(SettingsActivity.KEY_PREF_CHECK_FREQUENCY, "60");
        int checkFrequency = 60;
        try
        {
            checkFrequency = Integer.parseInt(strCheckFrequency);
            if (checkFrequency < 1)
            {
                checkFrequency = 60;
            }
        }
        catch (Throwable e)
        {
            //Do nothing
        }
        return checkFrequency;
    }

    public static int getNetworkErrors(Context context)
    {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getInt(SettingsActivity.KEY_STORE_NETWORK_ERRORS, 0);
    }

    public static void setNetworkErrors(Context context, int networkErrors)
    {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(SettingsActivity.KEY_STORE_NETWORK_ERRORS, networkErrors);
        editor.commit();
    }


}
