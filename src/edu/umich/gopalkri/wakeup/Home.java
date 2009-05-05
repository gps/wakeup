package edu.umich.gopalkri.wakeup;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * The Activity that represents the "Home" screen.
 *
 * @author Gopalkrishna Sharma (gopalkri@umich.edu)
 */
public class Home extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        setupUI();
    }

    /**
     * Sets up the UI for this activity. Contains all the event handling code
     * required.
     */
    private void setupUI()
    {
        // Handle the onClick event for the Done button.
        Button done = (Button) findViewById(R.id.home_done);
        done.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {}
        });

        // Handle the onClick event for the Create New Alarm button.
        Button createNewAlarm = (Button) findViewById(R.id.home_create_new_alarm);
        createNewAlarm.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {}
        });

        // Handle the onClick event for the Manage Existing Alarms button.
        Button manageExistingAlarms = (Button) findViewById(R.id.home_manage_existing_alarms);
        manageExistingAlarms.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {}
        });

        // Setup the Existing Alarms spinner.
        Spinner existingAlarms = (Spinner) findViewById(R.id.home_existing_alarms_spinner);

        LinearLayout alarmsExistLayout = (LinearLayout) findViewById(R.id.home_alarms_exist_layout);
        TextView noAlarmsExist = (TextView) findViewById(R.id.home_no_alarms_exist);
        String[] alarmNames = getAllAlarmNames();
        // If there are existing alarms, the layout that has UI elements to set/manage existing
        // alarms must be visible and enabled. If not, they must be disabled.
        if (alarmNames != null)
        {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, alarmNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            existingAlarms.setAdapter(adapter);

            alarmsExistLayout.setEnabled(true);
            alarmsExistLayout.setVisibility(View.VISIBLE);
            noAlarmsExist.setEnabled(false);
            noAlarmsExist.setVisibility(View.INVISIBLE);
        }
        else
        {
            alarmsExistLayout.setEnabled(false);
            alarmsExistLayout.setVisibility(View.INVISIBLE);
            noAlarmsExist.setEnabled(true);
            noAlarmsExist.setVisibility(View.VISIBLE);
        }
    }

    private String[] getAllAlarmNames()
    {
        // TODO Auto-generated method stub
        return null;
    }
}
