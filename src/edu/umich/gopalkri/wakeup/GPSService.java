package edu.umich.gopalkri.wakeup;

import java.io.IOException;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.IBinder;
import edu.umich.gopalkri.wakeup.data.Alarm;
import edu.umich.gopalkri.wakeup.data.Alarms;


public class GPSService extends Service
{
    public static final String ACTIVE_ALARM_FILE = "ActiveAlarm.txt";

    private static final String DESTINATION_PROXIMITY_ALERT = "edu.umich.gopalkri.wakeup.destinationalert";

    /**
     * @see android.os.IBinder#onBind(android.content.Intent)
     */
    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /** (non-Javadoc)
     * @see android.app.Service#onCreate()
     */
    @Override
    public void onCreate()
    {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    /** (non-Javadoc)
     * @see android.app.Service#onStart(android.content.Intent, int)
     */
    @Override
    public void onStart(Intent intent, int startId)
    {
        super.onStart(intent, startId);
        String alarmName;
        try
        {
            alarmName = Utilities.convertInputStreamToString(openFileInput(ACTIVE_ALARM_FILE)).trim();
        }
        catch (IOException e)
        {
            // Do nothing - no alarm.
            return;
        }
        Alarms alarms = new Alarms(this);
        Alarm alarm = alarms.getAlarm(alarmName);
        if (alarm == null)
        {
            // Do nothing - no alarm.
            return;
        }

        Intent i = new Intent(this, ReceiveNotification.class);
        PendingIntent proximityIntent = PendingIntent.getBroadcast(this, -1, i, 0);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Double radius = alarm.getRadiusInMeters();
        locationManager.addProximityAlert(alarm.getLatitude(), alarm.getLongitude(), radius.floatValue(), -1, proximityIntent);
        IntentFilter filter = new IntentFilter(DESTINATION_PROXIMITY_ALERT);
        registerReceiver(new ProximityIntentReceiver(), filter);
    }

    public class ProximityIntentReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            // TODO Auto-generated method stub

        }

    }
}
