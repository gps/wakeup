package edu.umich.gopalkri.wakeup;

import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import edu.umich.gopalkri.wakeup.data.Alarm;
import edu.umich.gopalkri.wakeup.data.AlarmAlreadyExistsException;
import edu.umich.gopalkri.wakeup.data.Alarms;
import edu.umich.gopalkri.wakeup.data.InvalidAlarmNameException;

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
            // TODO: start Settings Activity
            return true;

        case MENU_HELP:
            Intent i = new Intent(this, Help.class);
            startActivity(i);
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
     * Sets up Home Screen layout with the ability to select an existing alarm, and create new
     * alarms.
     * @param alarmNames Names of all known alarms.
     */
    private void setupHomeWithAlarms(String[] alarmNames)
    {
        setContentView(R.layout.home_with_alarms);

        // Handle the onClick event for the Done button.
        Button done = (Button) findViewById(R.id.home_done);
        done.setOnClickListener(new View.OnClickListener()
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

        Button createNewAlarm = (Button) findViewById(R.id.home_with_alarms_create_new_alarm);
        createNewAlarm.setOnClickListener(createNewAlarmListener);

        // Setup the Existing Alarms spinner.
        Spinner existingAlarms = (Spinner) findViewById(R.id.home_existing_alarms_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, alarmNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        existingAlarms.setAdapter(adapter);
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
            // TODO remove TESTING CODE and add actual handling code.
            Alarm alarm;
            try
            {
                alarm = new Alarm("Alarm1", 2.0, 3.0, 4.0, Alarm.Units.KM);
                mAlarms.addAlarm(alarm);
            }
            catch (InvalidAlarmNameException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (AlarmAlreadyExistsException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (FileNotFoundException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };

    private Alarms mAlarms;
}
