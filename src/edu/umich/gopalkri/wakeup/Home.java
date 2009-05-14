/*
Copyright (C) 2009 Gopalkrishna Sharma.
Email: gopalkri@umich.edu / gopalkrishnaps@gmail.com

This file is part of WakeUp!.

Wake Up! is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Wake Up! is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Wake Up!.  If not, see <http://www.gnu.org/licenses/>.
*/

package edu.umich.gopalkri.wakeup;

import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import edu.umich.gopalkri.wakeup.data.Alarms;

/**
 * The Activity that represents the "Home" screen.
 *
 * @author Gopalkrishna Sharma (gopalkri@umich.edu)
 */
public class Home extends Activity
{
    /**
     * Position of the Settings option in the options menu.
     */
    public static final int MENU_SETTINGS = Menu.FIRST;

    /**
     * Position of the Help option in the options menu.
     */
    public static final int MENU_HELP = Menu.FIRST + 1;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mAlarms = new Alarms(this);
        mEditAlarmIntent = new Intent(this, EditAlarm.class);
        mManageAlarmsIntent = new Intent(this, ManageAlarms.class);
        mStartGPSServiceIntent = new Intent(this, GPSService.class);

        setupUI();
    }

    /**
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume()
    {
        super.onResume();
        setupUI();
    }

    /**
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        boolean result = super.onCreateOptionsMenu(menu);

        menu.add(Menu.NONE, MENU_SETTINGS, Menu.NONE, R.string.home_settings);
        menu.add(Menu.NONE, MENU_HELP, Menu.NONE, R.string.home_help);

        return result;
    }

    /**
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
        case MENU_SETTINGS:
            Intent settingsIntent = new Intent(this, EditSettings.class);
            startActivity(settingsIntent);
            return true;

        case MENU_HELP:
            Intent helpIntent = new Intent(this, Help.class);
            startActivity(helpIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Sets up the UI for this activity. Contains all the event handling code
     * required.
     */
    private void setupUI()
    {
        String[] alarmNames = getAllAlarmNames();
        if (alarmNames == null)
        {
            setupHomeNoAlarms();
        }
        else
        {
            setupHomeWithAlarms(alarmNames);
        }
    }

    /**
     * Sets up Home Screen layout with the ability to select an existing alarm,
     * and create new alarms.
     *
     * @param alarmNames
     *            Names of all known alarms.
     */
    private void setupHomeWithAlarms(String[] alarmNames)
    {
        setContentView(R.layout.home_with_alarms);

        // Handle the onClick event for the Done button.
        Button done = (Button) findViewById(R.id.home_done);
        done.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                int pos = mExistingAlarmsSpinner.getSelectedItemPosition();
                if (pos == Spinner.INVALID_POSITION)
                {
                    return; // No alarm selected.
                }
                String alarmName = mAlarms.getAllAlarmNames()[pos];
                try
                {
                    Utilities.writeStringToFile(alarmName, openFileOutput(GPSService.ACTIVE_ALARM_FILE, Context.MODE_PRIVATE));
                }
                catch (FileNotFoundException e)
                {
                    // This should never happen.
                    throw new RuntimeException(e);
                }
                startService(mStartGPSServiceIntent);
                Toast.makeText(Home.this,
                        "Alarm set. You will be alerted when you approach your desination.",
                        Toast.LENGTH_LONG).show();
            }
        });

        // Handle the onClick event for the Manage Existing Alarms button.
        Button manageExistingAlarms = (Button) findViewById(R.id.home_manage_existing_alarms);
        manageExistingAlarms.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                startActivity(mManageAlarmsIntent);
            }
        });

        Button createNewAlarm = (Button) findViewById(R.id.home_with_alarms_create_new_alarm);
        createNewAlarm.setOnClickListener(createNewAlarmListener);

        mExistingAlarmsSpinner = (Spinner) findViewById(R.id.home_existing_alarms_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, alarmNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mExistingAlarmsSpinner.setAdapter(adapter);
    }

    /**
     * Sets up Home Screen layout with only the ability to create a new alarm.
     */
    private void setupHomeNoAlarms()
    {
        setContentView(R.layout.home_no_alarms);

        Button createNewAlarm = (Button) findViewById(R.id.home_no_alarms_create_new_alarm);
        createNewAlarm.setOnClickListener(createNewAlarmListener);
    }

    /**
     * Fetches names of all existing alarms.
     *
     * @return Names of all existing alarms.
     */
    private String[] getAllAlarmNames()
    {
        return mAlarms.getAllAlarmNames();
    }

    private View.OnClickListener createNewAlarmListener = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            startActivity(mEditAlarmIntent);
        }
    };

    private Alarms mAlarms;

    private Intent mEditAlarmIntent;
    private Intent mManageAlarmsIntent;
    private Intent mStartGPSServiceIntent;

    private Spinner mExistingAlarmsSpinner;
}
