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

/*
 * Copyright (C) 2009 Gopalkrishna Sharma. Email: gopalkri@umich.edu /
 * gopalkrishnaps@gmail.com
 *
 * This file is part of WakeUp!.
 *
 * Wake Up! is free software: you can redistribute it and/or modify it under the
 * terms of the Lesser GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Wake Up! is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the Lesser GNU General Public License for more
 * details.
 *
 * You should have received a copy of the Lesser GNU General Public License
 * along with Wake Up!. If not, see <http://www.gnu.org/licenses/>.
 */

package edu.umich.gopalkri.wakeup.data;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import android.content.Context;
import edu.umich.gopalkri.wakeup.Utilities;

/**
 * Handles persistence of Alarms. Also provides an interface to query and update
 * alarms.
 *
 * @author Gopalkrishna Sharma
 */
public class Alarms
{
    /**
     * Name of known alarms file.
     */
    public static final String ALARMS_FILE = "KnownAlarms.txt";

    /**
     * Initializes this object with Context ctx.
     *
     * @param ctx
     *            Context with which to initialize this object.
     */
    public Alarms(Context ctx)
    {
        this.ctx = ctx;
        initFromFile();
    }

    /**
     * Returns Alarm object with name name. Returns null if not found.
     * @param name Name of alarm.
     * @return Alarm with name name.
     */
    public Alarm getAlarm(String name)
    {
        initFromFile();
        return mAlarmsContainer.getAlarm(name);
    }

    /**
     * Adds alarm to known alarms.
     * @param alarm Alarm to add.
     * @throws AlarmAlreadyExistsException If alarm already exists.
     */
    public void addAlarm(Alarm alarm) throws AlarmAlreadyExistsException
    {
        initFromFile();
        mAlarmsContainer.addAlarm(alarm);
        writeToFile();
    }

    /**
     * Updates alarm which originally had name originalAlarmName with alarm.
     * @param originalAlarmName Original name of alarm.
     * @param alarm Alarm to update.
     * @throws AlarmAlreadyExistsException When an alarm with name alarm.getName() already exists.
     */
    public void updateAlarm(String originalAlarmName, Alarm alarm)
            throws AlarmAlreadyExistsException
    {
        if (originalAlarmName != null)
        {
            mAlarmsContainer.deleteAlarm(originalAlarmName);
            if (mAlarmsContainer.getAlarm(alarm.getName()) != null)
            {
                // Alarm already exists.
                throw new AlarmAlreadyExistsException();
            }
            mAlarmsContainer.addAlarm(alarm);
        }
        writeToFile();
    }

    /**
     * Removes Alarm with name alarmName. If not found, does nothing.
     * @param alarmName Name of alarm to be removed.
     */
    public void deleteAlarm(String alarmName)
    {
        mAlarmsContainer.deleteAlarm(alarmName);
        writeToFile();
    }

    /**
     * Returns a String[] of names of all alarms.
     * @return Names of all alarms.
     */
    public String[] getAllAlarmNames()
    {
        initFromFile();
        return mAlarmsContainer.getAllAlarmNames();
    }

    /**
     * Initializes mAlarmsContainer from file.
     */
    private void initFromFile()
    {
        try
        {
            mAlarmsContainer = new AlarmsContainer(Utilities.convertInputStreamToString(ctx
                    .openFileInput(ALARMS_FILE)));
        }
        catch (FileNotFoundException e)
        {
            // If file does not exist, no problem. It will get created when the
            // first alarm is created.
            mAlarmsContainer = new AlarmsContainer();
        }
        catch (IOException e)
        {
            // If the file could not be read, no problem. Assume that there is
            // nothing in it, and start afresh.
            mAlarmsContainer = new AlarmsContainer();
        }
        catch (InvalidAlarmStringException e)
        {
            // If the file format was illegal, nothing can be done but to reset
            // to blank file.
            mAlarmsContainer = new AlarmsContainer();
        }
    }

    /**
     * Writes mAlarmsContainer to file.
     */
    private void writeToFile()
    {
        try
        {
            Utilities.writeStringToFile(mAlarmsContainer.toString(), ctx.openFileOutput(
                    ALARMS_FILE, Context.MODE_PRIVATE));
        }
        catch (FileNotFoundException e)
        {
            // This should never happen.
            throw new RuntimeException(e);
        }
    }

    /**
     * Context used to construct this object.
     */
    private Context ctx;

    /**
     * AlarmsContainer of this object.
     */
    private AlarmsContainer mAlarmsContainer;

    /**
     * Contains all alarms in existence. Constructs itself from a String representation, and
     * provides a String encoding of itself via toString().
     * @author Gopalkrishna Sharma
     */
    private class AlarmsContainer
    {
        /**
         * Default constructor - does nothing.
         */
        public AlarmsContainer()
        {}

        /**
         * Initializes this object from its String representation acString.
         * @param acString String representation of AlarmsContainer
         * @throws InvalidAlarmStringException When acString is invalid.
         */
        public AlarmsContainer(String acString) throws InvalidAlarmStringException
        {
            for (String alarmStr : acString.split("\n"))
            {
                Alarm alarm = new Alarm(alarmStr);
                alarms.put(alarm.getName(), alarm);
            }
        }

        /**
         * Returns a String representation of this object.
         */
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

        /**
         * Returns Alarm object with name name. Returns null if not found.
         * @param name Name of alarm.
         * @return Alarm with name name.
         */
        public Alarm getAlarm(String name)
        {
            if (alarms.containsKey(name))
            {
                return alarms.get(name);
            }
            return null;
        }

        /**
         * Adds alarm to known alarms.
         * @param alarm Alarm to add.
         * @throws AlarmAlreadyExistsException If alarm already exists.
         */
        public void addAlarm(Alarm alarm) throws AlarmAlreadyExistsException
        {
            if (alarms.containsKey(alarm.getName()))
            {
                throw new AlarmAlreadyExistsException();
            }
            alarms.put(alarm.getName(), alarm);
        }

        /**
         * Removes Alarm with name alarmName. If not found, does nothing.
         * @param alarmName Name of alarm to be removed.
         */
        public void deleteAlarm(String alarmName)
        {
            alarms.remove(alarmName);
        }

        /**
         * Returns a String[] of names of all alarms.
         * @return Names of all alarms.
         */
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

        /**
         * Stores all alarms.
         * Key: Alarm Name.
         * Value: Alarm object.
         */
        private HashMap<String, Alarm> alarms = new HashMap<String, Alarm>();
    }
}
