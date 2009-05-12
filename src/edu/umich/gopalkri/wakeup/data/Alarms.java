/*
Copyright (C) 2009 Gopalkrishna Sharma.
Email: gopalkri@umich.edu / gopalkrishnaps@gmail.com

This file is part of WakeUp!.

Foobar is free software: you can redistribute it and/or modify
it under the terms of the Lesser GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Foobar is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
Lesser GNU General Public License for more details.

You should have received a copy of the Lesser GNU General Public License
along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
*/

package edu.umich.gopalkri.wakeup.data;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import edu.umich.gopalkri.wakeup.Utilities;
import edu.umich.gopalkri.wakeup.data.Alarm.InvalidAlarmStringException;

public class Alarms
{
    public static final String ALARMS_FILE = "KnownAlarms.txt";

    public Alarms(Context ctx)
    {
        this.ctx = ctx;
        initFromFile();
    }

    public void initFromFile()
    {
        try
        {
            alarmsContainer = new AlarmsContainer(Utilities.convertInputStreamToString(ctx
                    .openFileInput(ALARMS_FILE)));
        }
        catch (FileNotFoundException e)
        {
            // If file does not exist, no problem. It will get created when the
            // first alarm is created.
            alarmsContainer = new AlarmsContainer();
        }
        catch (IOException e)
        {
            // If the file could not be read, no problem. Assume that there is
            // nothing in it, and start afresh.
            alarmsContainer = new AlarmsContainer();
        }
        catch (InvalidAlarmStringException e)
        {
            // If the file format was illegal, nothing can be done but to reset
            // to blank file.
            alarmsContainer = new AlarmsContainer();
        }
    }

    public void writeToFile() throws FileNotFoundException
    {
        Utilities.writeStringToFile(alarmsContainer.toString(), ctx.openFileOutput(ALARMS_FILE, Context.MODE_PRIVATE));
    }

    public Alarm getAlarm(String name)
    {
        initFromFile();
        return alarmsContainer.getAlarm(name);
    }

    public void addAlarm(Alarm alarm) throws AlarmAlreadyExistsException
    {
        initFromFile();
        alarmsContainer.addAlarm(alarm);
        try
        {
            writeToFile();
        }
        catch (FileNotFoundException e)
        {
            // This should not happen.
            throw new RuntimeException(e);
        }
    }

    public void updateAlarm(Alarm alarm)
    {
        try
        {
            writeToFile();
        }
        catch (FileNotFoundException e)
        {
            // This should not happen.
            throw new RuntimeException(e);
        }
    }

    public void deleteAlarm(String alarmName)
    {
        alarmsContainer.deleteAlarm(alarmName);
        try
        {
            writeToFile();
        }
        catch (FileNotFoundException e)
        {
            // This should not happen.
            throw new RuntimeException(e);
        }
    }

    public String[] getAllAlarmNames()
    {
        initFromFile();
        return alarmsContainer.getAllAlarmNames();
    }

    private Context ctx;

    private AlarmsContainer alarmsContainer;

    private class AlarmsContainer implements java.io.Serializable
    {
        /**
         * Autogenerated.
         */
        private static final long serialVersionUID = -771998816315269294L;

        public AlarmsContainer()
        {}

        public AlarmsContainer(String acString) throws InvalidAlarmStringException
        {
            for (String alarmStr : acString.split("\n"))
            {
                Alarm alarm = new Alarm(alarmStr);
                alarms.put(alarm.getName(), alarm);
            }
        }

        public String toString()
        {
            StringBuilder sb = new StringBuilder();
            for (Alarm alarm : alarms.values())
            {
                sb.append(alarm.toString());
                sb.append("\n");
            }
            return sb.toString();
        }

        public Alarm getAlarm(String name)
        {
            if (alarms.containsKey(name))
            {
                return alarms.get(name);
            }
            return null;
        }

        public void addAlarm(Alarm alarm) throws AlarmAlreadyExistsException
        {
            if (alarms.containsKey(alarm.getName()))
            {
                throw new AlarmAlreadyExistsException();
            }
            alarms.put(alarm.getName(), alarm);
        }

        public void deleteAlarm(String alarmName)
        {
            alarms.remove(alarmName);
        }

        public String[] getAllAlarmNames()
        {
            int numAlarms = alarms.size();
            if (numAlarms <= 0)
            {
                return null;
            }
            String[] ret = new String[numAlarms];
            ret = alarms.keySet().toArray(ret);
            return ret;
        }

        private Map<String, Alarm> alarms = new HashMap<String, Alarm>();
    }
}
