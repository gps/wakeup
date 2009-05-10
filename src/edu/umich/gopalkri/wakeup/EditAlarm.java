package edu.umich.gopalkri.wakeup;

import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;

import edu.umich.gopalkri.wakeup.data.Alarm;
import edu.umich.gopalkri.wakeup.data.AlarmAlreadyExistsException;
import edu.umich.gopalkri.wakeup.data.Alarms;
import edu.umich.gopalkri.wakeup.data.InvalidAlarmNameException;
import edu.umich.gopalkri.wakeup.data.Alarm.InvalidAlarmStringException;

public class EditAlarm extends Activity
{
    public static final String ALARM_NAME = "ALARM_NAME";

    private static final int SELECT_DESTINATION = 1;

    private static final String RADIUS_COULD_NOT_BE_PARSED = "The proximity radius you entered could not be parsed as a number. You need to enter a number (can be a decimal). Please fix this and try again.";
    private static final String INVALID_ALARM_NAME = "The alarm name you supplied is invalid. Alarm names cannot contain the character sequence: \""
            + Alarm.FIELD_SEPARATOR
            + "\" (without the \"). Please enter another name and try again.";
    private static final String ALARM_ALREADY_EXISTS = "An alarm with this name already exists. Please pick another name, or delete the existing alarm first and then try again.";
    private static final String LOCATION_NOT_SET = "You have not set a location for this alarm. Please do so and try again.";
    private static final String LOCATION_SET = "The destination you picked is: ";

    /**
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_alarm);

        mAlarms = new Alarms(this);
        mSelectDestinationIntent = new Intent(this, SelectDestination.class);

        if (savedInstanceState != null)
        {
            String alarmName = savedInstanceState.getString(ALARM_NAME);
            if (alarmName != null)
            {
                mThisAlarm = mAlarms.getAlarm(alarmName);
                mNewAlarm = false;
                mLocationSet = true;
            }
            else
            {
                mThisAlarm = new Alarm();
                mNewAlarm = true;
                mLocationSet = false;
            }
        }
        else
        {
            mThisAlarm = new Alarm();
            mNewAlarm = true;
            mLocationSet = false;
        }

        setupUI();
    }

    /**
     * @see android.app.Activity#onActivityResult(int, int,
     *      android.content.Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle bundle = data.getExtras();

        switch (requestCode)
        {
        case SELECT_DESTINATION:
            String locStr = bundle.getString(SelectDestination.LOCATION_STRING);
            if (locStr == null)
            {
                // This should not happen.
                throw new RuntimeException("SelectDestination returned a null location string.");
            }
            GeoPoint selectedLocation = Utilities.decodeLocationString(locStr);
            Double latitude = Utilities.getLatitudeFromGeoPoint(selectedLocation);
            Double longitude = Utilities.getLongitudeFromGeoPoint(selectedLocation);
            mThisAlarm.setLatitude(latitude);
            mThisAlarm.setLongitude(longitude);
            mLocationSet = true;
            Toast toast = Toast.makeText(this, LOCATION_SET + latitude.toString() + ", "
                    + longitude.toString() + ".", Toast.LENGTH_LONG);
            toast.show();
        }
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
                    // FIXME Check that EditAlarm.this actually works.
                    Utilities.reportError(EditAlarm.this, Utilities.ERROR, LOCATION_NOT_SET);
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
                    throw new RuntimeException(
                            "Got a FileNotFoundException when trying to create alarm.");
                }
                catch (AlarmAlreadyExistsException e)
                {
                    // An alarm with this name already exists.
                    // FIXME Check that EditAlarm.this actually works.
                    Utilities.reportError(EditAlarm.this, Utilities.ERROR, ALARM_ALREADY_EXISTS);
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
            {
                startActivityForResult(mSelectDestinationIntent, SELECT_DESTINATION);
            }
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
            Utilities.reportError(this, Utilities.ERROR, INVALID_ALARM_NAME);
            return;
        }
        double radius;
        try
        {
            radius = Double.parseDouble(mETAlarmRadius.getText().toString());
        }
        catch (NumberFormatException ex)
        {
            Utilities.reportError(this, Utilities.ERROR, RADIUS_COULD_NOT_BE_PARSED);
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

    private EditText mETAlarmName;
    private EditText mETAlarmRadius;
    private EditText mETAlarmSearch;
    private Spinner mUnitsSpinner;

    private Alarms mAlarms;

    private Intent mSelectDestinationIntent;

    private Alarm mThisAlarm;
    private boolean mNewAlarm;
    private boolean mLocationSet;
}
