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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.google.android.maps.GeoPoint;

import edu.umich.gopalkri.wakeup.data.Alarm;
import edu.umich.gopalkri.wakeup.data.Alarms;

public class Utilities
{
    public static final String ERROR = "ERROR!";

    public static AlertDialog createAlertDialog(Context ctx, String title, String message)
    {
        AlertDialog dialog = new AlertDialog.Builder(ctx).create();
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setButton("OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {}
        });
        return dialog;
    }

    private static final String LOCATION_ENCODE_SEPARATOR = "Z";

    public static String encodeLocation(Double latitude, Double longitude)
    {
        return latitude.toString() + LOCATION_ENCODE_SEPARATOR + longitude.toString();
    }

    public static String encodeLocation(GeoPoint gp)
    {
        return encodeLocation(getLatitudeFromGeoPoint(gp), getLongitudeFromGeoPoint(gp));
    }

    public static GeoPoint decodeLocationString(String locationString)
    {
        String[] split = locationString.split(LOCATION_ENCODE_SEPARATOR);
        if (split.length != 2)
        {
            return null;
        }
        try
        {
            Double latitude = Double.parseDouble(split[0]) * 1E6;
            Double longitude = Double.parseDouble(split[1]) * 1E6;
            return new GeoPoint(latitude.intValue(), longitude.intValue());
        }
        catch (NumberFormatException ex)
        {
            return null;
        }
    }

    public static double getLatitudeFromGeoPoint(GeoPoint gp)
    {
        return gp.getLatitudeE6() / 1E6;
    }

    public static double getLongitudeFromGeoPoint(GeoPoint gp)
    {
        return gp.getLongitudeE6() / 1E6;
    }

    public static String convertInputStreamToString(InputStream is) throws IOException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = br.readLine()) != null)
        {
            sb.append(line);
            sb.append("\n");
        }
        return sb.toString();
    }

    public static void writeStringToFile(String text, OutputStream os)
    {
        PrintWriter pw = new PrintWriter(os);
        pw.write(text);
        pw.flush();
        pw.close();
    }

    public static Alarm getActiveAlarm(Context ctx)
    {
        String alarmName;
        try
        {
            alarmName = Utilities.convertInputStreamToString(ctx.openFileInput(GPSService.ACTIVE_ALARM_FILE)).trim();
        }
        catch (IOException e)
        {
            // No alarm.
            return null;
        }
        Alarms alarms = new Alarms(ctx);
        Alarm alarm = alarms.getAlarm(alarmName);
        return alarm;
    }
}
