package com.blogspot.practicegoodtheory.testurl2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.blogspot.practicegoodtheory.testurl.R;

public class MainActivity extends ActionBarActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String urlName = AppPreferences.getUrlName(this);
        //Switch switchOnOff = (Switch) findViewById(R.id.switchOnOff);
        Button buttonStartStop = (Button) findViewById(R.id.buttonStartStop);

        Log.d("PREF", "ABOUT TO CHECK");
        if (AppPreferences.isOn(this))
        {
        Log.d("PREF", "IS ON");
        CheckAlarmReceiver.scheduleAlarm(this);
        }
        else
        {
            Log.d("PREF", "IS OFF");
            CheckAlarmReceiver.stopAlarm(this);
        }

        buttonStartStop.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean turnOn = !AppPreferences.isOn(MainActivity.this);
                        AppPreferences.setOnOff(MainActivity.this, turnOn);
                        Button buttonStartStop = (Button) findViewById(R.id.buttonStartStop);
                        if (turnOn) {
                            MainActivity.this.startCheckingService();
                        }
                        else {
                            MainActivity.this.stopCheckingService();
                        }
                        setButtonTextAndsColor();
                    }
                }
        );


        if (urlName==null || urlName.isEmpty())
        {
            Toast.makeText(MainActivity.this, "Use the Settings menu to specify the URL", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        setButtonTextAndsColor();

    }

    private void setButtonTextAndsColor()
    {
        Button theButton = (Button) findViewById(R.id.buttonStartStop);
        boolean onOff = AppPreferences.isOn(this);
        if (onOff)
        {
            theButton.setText(R.string.is_on);
            theButton.setBackgroundResource(R.drawable.button_on);
        }
        else
        {
            theButton.setText(R.string.is_off);
            theButton.setBackgroundResource(R.drawable.button_off);
        }
    }

    private void startCheckingService()
    {
        String urlName =  AppPreferences.getUrlName(getApplicationContext());
        String phoneNumber =  AppPreferences.getPhoneNumber(getApplicationContext());
        if (urlName==null || urlName.isEmpty())
        {
            Toast.makeText(MainActivity.this, "Use the Settings menu to specify URL", Toast.LENGTH_LONG).show();
            return;
        }
        if (phoneNumber==null || phoneNumber.isEmpty())
        {
            Toast.makeText(MainActivity.this, "Phone number is blank. Will use Notifications", Toast.LENGTH_SHORT).show();
        }

        CheckAlarmReceiver.scheduleAlarm(getApplicationContext());

        Toast.makeText(MainActivity.this, "Started checking", Toast.LENGTH_SHORT).show();
    }

    private void stopCheckingService()
    {
        Toast.makeText(MainActivity.this, "Will stop", Toast.LENGTH_SHORT).show();
        CheckAlarmReceiver.stopAlarm(getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
