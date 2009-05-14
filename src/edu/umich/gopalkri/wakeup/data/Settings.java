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

package edu.umich.gopalkri.wakeup.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Handles persistence of notification settings. Also provides an interface to query and update
 * settings.
 * @author Gopalkrishna Sharma
 */
public class Settings
{
    private static final String SETTINGS_FILE = "Settings.txt";
    private static final String PLAY_SOUND = "PLAY_SOUND";
    private static final String VIBRATE = "VIBRATE";
    private static final String LED = "LED";
    private static final int DEFAULT_VALUE = -1;
    private static final int YES = 1;
    private static final int NO = 0;

    /**
     * Settings object as obtained by this context.
     */
    private SharedPreferences mSettings;

    /**
     * Initializes shared settings object with Context ctx.
     * @param ctx Context with which initialize Settings object.
     */
    public Settings(Context ctx)
    {
        mSettings = ctx.getSharedPreferences(SETTINGS_FILE, Context.MODE_PRIVATE);
    }

    /**
     * Returns whether or not to play sound.
     * @return True if sound should be played, false otherwise.
     */
    public boolean getPlaySound()
    {
        return YES == getSetting(PLAY_SOUND);
    }

    /**
     * Sets whether or not to play the sound.
     * @param playSound True if should should be played, false otherwise.
     */
    public void setPlaySound(boolean playSound)
    {
        setSetting(PLAY_SOUND, playSound ? YES : NO);
    }

    /**
     * Returns whether or not phone should vibrate.
     * @return True if phone should vibrate, false otherwise.
     */
    public boolean getVibrate()
    {
        return YES == getSetting(VIBRATE);
    }

    /**
     * Sets whether or not phone should vibrate.
     * @param vibrate True if phone should vibrate, false otherwise.
     */
    public void setVibrate(boolean vibrate)
    {
        setSetting(VIBRATE, vibrate ? YES : NO);
    }

    /**
     * Returns whether or not LED should flash.
     * @return True if LED should flash, false otherwise.
     */
    public boolean getLed()
    {
        return YES == getSetting(LED);
    }

    /**
     * Sets whether or not LED should flash.
     * @param led True if LED should flash, false otherwise.
     */
    public void setLed(boolean led)
    {
        setSetting(LED, led ? YES : NO);
    }

    /**
     * Gets value of setting that corresponds to key. If none exists, it is initialized with YES.
     * @param key Identifier of setting.
     * @return Value of setting.
     */
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

    /**
     * Sets setting identified with key with value.
     * @param key Identifier of setting.
     * @param value Value of setting.
     */
    private void setSetting(String key, int value)
    {
        mSettings.edit().putInt(key, value).commit();
    }
}
