/*
Copyright (C) 2009 Gopalkrishna Sharma.
Email: gopalkri@umich.edu / gopalkrishnaps@gmail.com

This file is part of WakeUp!.

Wake Up! is free software: you can redistribute it and/or modify
it under the terms of the Lesser GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Wake Up! is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
Lesser GNU General Public License for more details.

You should have received a copy of the Lesser GNU General Public License
along with Wake Up!.  If not, see <http://www.gnu.org/licenses/>.
*/

package edu.umich.gopalkri.wakeup;

import java.io.FileNotFoundException;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import edu.umich.gopalkri.wakeup.data.Alarm;

public class ReceiveNotification extends Activity
{
    public static final int NOTIFICATION_ID = 1;

    private static final int MENU_HOME = Menu.FIRST;

    /**
     * (non-Javadoc)
     *
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.receive_notification);
        TextView text = (TextView) findViewById(R.id.receive_notification_text);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);

        Alarm alarm = Utilities.getActiveAlarm(this);
        if (alarm == null)
        {
            // This should never happen.
            throw new RuntimeException("Notification was triggered but there was no alarm.");
        }

        String content = "You had set alarm: " + alarm.getName() + "\n\n" + "You are now within "
                + ((Double) alarm.getRadius()) + " " + Alarm.UnitsToString(alarm.getUnit())
                + " of your destination.\n\nThe alarm has now been deactiviated.";
        text.setText(content);

        try
        {
            Utilities.writeStringToFile("", openFileOutput(GPSService.ACTIVE_ALARM_FILE, Context.MODE_PRIVATE));
        }
        catch (FileNotFoundException e)
        {
            // Should never happen.
            throw new RuntimeException("Unexpected FileNotFoundException thrown in ReceiveNotification.onCreate.");
        }
    }

    /**
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(Menu.NONE, MENU_HOME, Menu.NONE, R.string.receive_notification_home);
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
        case MENU_HOME:
            Intent i = new Intent(this, Home.class);
            startActivity(i);
            setResult(RESULT_OK);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
