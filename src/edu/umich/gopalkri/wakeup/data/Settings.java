package edu.umich.gopalkri.wakeup.data;

import android.content.Context;
import android.content.SharedPreferences;

public class Settings
{
    private static final String SETTINGS_FILE = "Settings.txt";
    private static final String PLAY_SOUND = "PLAY_SOUND";
    private static final String VIBRATE = "VIBRATE";
    private static final String LED = "LED";
    private static final int DEFAULT_VALUE = -1;
    private static final int YES = 1;
    private static final int NO = 0;

    private SharedPreferences mSettings;

    public Settings(Context ctx)
    {
        mSettings = ctx.getSharedPreferences(SETTINGS_FILE, Context.MODE_PRIVATE);
    }

    private int getSetting(String key)
    {
        int value = mSettings.getInt(key, DEFAULT_VALUE);
        if (value == DEFAULT_VALUE)
        {
            setSetting(key, YES);
            return YES;
        }
        return value;
    }

    private void setSetting(String key, int value)
    {
        mSettings.edit().putInt(key, value).commit();
    }

    public boolean getPlaySound()
    {
        return YES == getSetting(PLAY_SOUND);
    }

    public void setPlaySound(boolean playSound)
    {
        setSetting(PLAY_SOUND, playSound ? YES : NO);
    }

    public boolean getVibrate()
    {
        return YES == getSetting(VIBRATE);
    }

    public void setVibrate(boolean vibrate)
    {
        setSetting(VIBRATE, vibrate ? YES : NO);
    }

    public boolean getLed()
    {
        return YES == getSetting(LED);
    }

    public void setLed(boolean led)
    {
        setSetting(LED, led ? YES : NO);
    }
}
