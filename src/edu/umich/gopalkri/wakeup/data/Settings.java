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
