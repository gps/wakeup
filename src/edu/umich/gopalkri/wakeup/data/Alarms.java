package edu.umich.gopalkri.wakeup.data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import edu.umich.gopalkri.wakeup.data.Alarm.InvalidAlarmStringException;

public class Alarms
{
    public static final String ALARMS_FILE = "AlarmsFile.yaml";

    public Alarms(Context ctx)
    {
        this.ctx = ctx;
        try
        {
            alarmsContainer = new AlarmsContainer(convertInputStreamToString(ctx
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

        }
        catch (InvalidAlarmStringException e)
        {
            // If the file format was illegal, nothing can be done but to reset
            // to blank file.
            alarmsContainer = new AlarmsContainer();
        }
    }

    public Alarm getAlarm(String name)
    {
        return alarmsContainer.getAlarm(name);
    }

    public void addAlarm(Alarm alarm) throws AlarmAlreadyExistsException, FileNotFoundException
    {
        alarmsContainer.addAlarm(alarm);
        PrintWriter pw = new PrintWriter(ctx.openFileOutput(ALARMS_FILE, Context.MODE_PRIVATE));
        pw.write(alarmsContainer.toString());
        pw.flush();
        pw.close();
    }

    public String[] getAllAlarmNames()
    {
        return alarmsContainer.getAllAlarmNames();
    }

    private static String convertInputStreamToString(InputStream is) throws IOException
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
            String dbg = acString;
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
