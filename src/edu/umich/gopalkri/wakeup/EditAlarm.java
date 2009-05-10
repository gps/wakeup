package edu.umich.gopalkri.wakeup;

import java.io.FileNotFoundException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import edu.umich.gopalkri.wakeup.data.Alarm;
import edu.umich.gopalkri.wakeup.data.AlarmAlreadyExistsException;
import edu.umich.gopalkri.wakeup.data.Alarms;
import edu.umich.gopalkri.wakeup.data.InvalidAlarmNameException;
import edu.umich.gopalkri.wakeup.data.Alarm.InvalidAlarmStringException;

public class EditAlarm extends Activity
{
    public static final String ALARM_NAME = "ALARM_NAME";

    private static final String ERROR = "Error!";
    private static final String RADIUS_COULD_NOT_BE_PARSED = "The proximity radius you entered could not be parsed as a number. You need to enter a number (can be a decimal). Please fix this and try again.";
    private static final String INVALID_ALARM_NAME = "The alarm name you supplied is invalid. Alarm names cannot contain the character sequence: \""
            + Alarm.FIELD_SEPARATOR
            + "\" (without the \"). Please enter another name and try again.";
    private static final String ALARM_ALREADY_EXISTS = "An alarm with this name already exists. Please pick another name, or delete the existing alarm first and then try again.";
    private static final String LOCATION_NOT_SET = "You have not set a location for this alarm. Please do so and try again.";

    /**
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_alarm);

        mAlarms = new Alarms(this);

        String alarmName = savedInstanceState.getString(ALARM_NAME);
        if (alarmName == null)
        {
            mThisAlarm = new Alarm();
            mNewAlarm = true;
            mLocationSet = false;
        }
        else
        {
            mThisAlarm = mAlarms.getAlarm(alarmName);
            mNewAlarm = false;
            mLocationSet = true;
        }

        setupUI();
    }

    private void setupUI()
    {
        // Setup all EditText fields.
        mETAlarmName = (EditText) findViewById(R.id.edit_alarm_alarm_name);
        mETAlarmRadius = (EditText) findViewById(R.id.edit_alarm_radius);
        mETAlarmSearch = (EditText) findViewById(R.id.edit_alarm_address_search);

        // Setup Units spinner.
        mUnitsSpinner = (Spinner) findViewById(R.id.edit_alarm_units_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.units,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mUnitsSpinner.setAdapter(adapter);

        // Setup Search For Address button.
        Button searchAddress = (Button) findViewById(R.id.edit_alarm_search_btn);
        searchAddress.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {}
        });

        // Setup Save button.
        Button save = (Button) findViewById(R.id.edit_alarm_save);
        save.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if (!mLocationSet)
                {
                    reportError(ERROR, LOCATION_NOT_SET);
                    return;
                }
                updateAlarm();
                try
                {
                    if (mNewAlarm)
                    {
                        mAlarms.addAlarm(mThisAlarm);
                    }
                    else
                    {
                        mAlarms.updateAlarm(mThisAlarm);
                    }
                }
                catch (FileNotFoundException ex)
                {
                    // Unexpected error. This should not happen.
                    throw new RuntimeException("Got a FileNotFoundException when trying to create alarm.");
                }
                catch (AlarmAlreadyExistsException e)
                {
                    // An alarm with this name already exists.
                    reportError(ERROR, ALARM_ALREADY_EXISTS);
                    return;
                }
                setResult(RESULT_OK);
                finish();
            }
        });

        // Setup Pick From Map button.
        Button pickFromMap = (Button) findViewById(R.id.edit_alarm_pick_from_map);
        pickFromMap.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {}
        });
        if (mNewAlarm)
        {
            pickFromMap.setText(R.string.edit_alarm_pick_from_map);
        }
        else
        {
            pickFromMap.setText(R.string.edit_alarm_view_on_map);
            populateFields();
        }
    }

    private void populateFields()
    {
        mETAlarmName.setText(mThisAlarm.getName());
        mETAlarmRadius.setText(((Double) mThisAlarm.getRadius()).toString());
        mUnitsSpinner.setSelection(Alarm.UnitsToInt(mThisAlarm.getUnit()));
        mETAlarmSearch.setText("");
    }

    private void updateAlarm()
    {
        try
        {
            mThisAlarm.setName(mETAlarmName.getText().toString());
        }
        catch (InvalidAlarmNameException ex)
        {
            reportError(ERROR, INVALID_ALARM_NAME);
            return;
        }
        double radius;
        try
        {
            radius = Double.parseDouble(mETAlarmRadius.getText().toString());
        }
        catch (NumberFormatException ex)
        {
            reportError(ERROR, RADIUS_COULD_NOT_BE_PARSED);
            return;
        }
        mThisAlarm.setRadius(radius);
        try
        {
            mThisAlarm.setUnit(Alarm.IntToUnits(mUnitsSpinner.getSelectedItemPosition()));
        }
        catch (InvalidAlarmStringException e)
        {
            // This should not happen.
            throw new RuntimeException("Units spinner and enum Units are out of sync.");
        }
    }

    private void reportError(String title, String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {}
        });
        builder.show();
    }

    private EditText mETAlarmName;
    private EditText mETAlarmRadius;
    private EditText mETAlarmSearch;
    private Spinner mUnitsSpinner;

    private Alarms mAlarms;

    private Alarm mThisAlarm;
    private boolean mNewAlarm;
    private boolean mLocationSet;
}
