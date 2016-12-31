package com.blogspot.practicegoodtheory.testurl2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by dcooper on 7/11/2015.
 */
public class CheckAlarmReceiver extends BroadcastReceiver
{

    public static void scheduleAlarm(Context context)
    {
        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, CheckAlarmReceiver.class);

        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        Log.i(CheckAlarmReceiver.class.getName(), "******************* About to set alarm **** ");
        int checkInterval = AppPreferences.getCheckFrequency(context);
        switch (checkInterval)
        {
            case 15:
                alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                        AlarmManager.INTERVAL_FIFTEEN_MINUTES, alarmIntent);
                break;
            case 30:
                alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        AlarmManager.INTERVAL_HALF_HOUR,
                        AlarmManager.INTERVAL_HALF_HOUR, alarmIntent);
                break;
            case 60:
                alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        AlarmManager.INTERVAL_HOUR,
                        AlarmManager.INTERVAL_HOUR, alarmIntent);
                break;
            default:
                alarmMgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        checkInterval * 60000, checkInterval * 60000, alarmIntent);
        }

    }

    public static void  stopAlarm(Context context)
    {
        Log.i(CheckAlarmReceiver.class.getName(), "******************* Cancelling alarm ***  * ");
        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, CheckAlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmMgr.cancel(alarmIntent);
        AppPreferences.setOnOff(context, false);
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.i(this.getClass().getName(), "******************* Alarm Received *********************  "+intent.getClass().getName());

        //Cannot do network ops in this main thread
        //Cannot start a separate thread without needing gosyncy etc. to synch the response
        //Therefore, starting a service
        Intent urlCheckerIntent = new Intent(context, UrlCheckerService.class);
        context.startService(urlCheckerIntent);
    }

}
