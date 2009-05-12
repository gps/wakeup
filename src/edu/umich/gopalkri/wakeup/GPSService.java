package edu.umich.gopalkri.wakeup;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.IBinder;
import edu.umich.gopalkri.wakeup.data.Alarm;
import edu.umich.gopalkri.wakeup.data.Settings;

public class GPSService extends Service
{
    public static final String ACTIVE_ALARM_FILE = "ActiveAlarm.txt";

    private static final String DESTINATION_PROXIMITY_ALERT = "edu.umich.gopalkri.wakeup.destinationalert";

    private PendingIntent mProximityIntent;
    private boolean mProximityAlertActive = false;

    /**
     * @see android.os.IBinder#onBind(android.content.Intent)
     */
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    /**
     * (non-Javadoc)
     *
     * @see android.app.Service#onCreate()
     */
    @Override
    public void onCreate()
    {
        super.onCreate();
        Intent i = new Intent(DESTINATION_PROXIMITY_ALERT);
        mProximityIntent = PendingIntent.getBroadcast(this, -1, i, 0);
    }

    /**
     * (non-Javadoc)
     *
     * @see android.app.Service#onStart(android.content.Intent, int)
     */
    @Override
    public void onStart(Intent intent, int startId)
    {
        super.onStart(intent, startId);

        Alarm alarm = Utilities.getActiveAlarm(this);
        if (alarm == null)
        {
            return;
        }

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Double radius = alarm.getRadiusInMeters();
        locationManager.addProximityAlert(alarm.getLatitude(), alarm.getLongitude(), radius
                .floatValue(), -1, mProximityIntent);
        IntentFilter filter = new IntentFilter(DESTINATION_PROXIMITY_ALERT);
        registerReceiver(new ProximityIntentReceiver(), filter);
        mProximityAlertActive = true;
    }

    /**
     * @see android.app.Service#onDestroy()
     */
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (!mProximityAlertActive)
        {
            return;
        }
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.removeProximityAlert(mProximityIntent);
    }

    public class ProximityIntentReceiver extends BroadcastReceiver
    {
        private Settings mSettings;

        @Override
        public void onReceive(Context context, Intent intent)
        {
            mSettings = new Settings(context);
            boolean entering = intent
                    .getBooleanExtra(LocationManager.KEY_PROXIMITY_ENTERING, false);
            if (entering)
            {
                stopService(new Intent(context, GPSService.class));
                launchNotification(context);
            }
            else
            {
                stopService(new Intent(context, GPSService.class));
                ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
                        .cancel(ReceiveNotification.NOTIFICATION_ID);
            }
        }

        private void launchNotification(Context ctx)
        {
            Alarm alarm = Utilities.getActiveAlarm(ctx);
            if (alarm == null)
            {
                // This should never happen.
                return;
            }

            Intent intent = new Intent(ctx, ReceiveNotification.class);
            PendingIntent launchIntent = PendingIntent.getActivity(ctx, 0, intent, 0);

            String appName = getString(R.string.app_name);
            Notification notification = new Notification(R.drawable.icon, appName, System
                    .currentTimeMillis());
            String expandedTitle = appName + " Alarm: " + alarm.getName();
            String expandedText = "You are now within " + ((Double) alarm.getRadius()) + " "
                    + Alarm.UnitsToString(alarm.getUnit()) + " of your destination.";
            notification.setLatestEventInfo(ctx, expandedTitle, expandedText, launchIntent);

            if (mSettings.getPlaySound())
            {
                notification.defaults |= Notification.DEFAULT_SOUND;
            }
            if (mSettings.getVibrate())
            {
                notification.defaults |= Notification.DEFAULT_LIGHTS;
            }
            if (mSettings.getLed())
            {
                notification.defaults |= Notification.DEFAULT_VIBRATE;
            }

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(ReceiveNotification.NOTIFICATION_ID, notification);
        }
    }
}
