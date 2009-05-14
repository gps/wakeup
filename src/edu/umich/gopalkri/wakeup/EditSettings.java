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

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import edu.umich.gopalkri.wakeup.data.Settings;

public class EditSettings extends Activity
{
    private Settings mSettings;

    /**
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mSettings = new Settings(this);
    }



    /**
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume()
    {
        super.onResume();
        setupUI();
    }



    private void setupUI()
    {
        setContentView(R.layout.edit_settings);

        final CheckBox sound = (CheckBox) findViewById(R.id.edit_settings_sound_checkbox);
        sound.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                mSettings.setPlaySound(sound.isChecked());
            }
        });
        sound.setChecked(mSettings.getPlaySound());

        final CheckBox vibrate = (CheckBox) findViewById(R.id.edit_settings_vibrate_checkbox);
        vibrate.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                mSettings.setVibrate(vibrate.isChecked());
            }
        });
        vibrate.setChecked(mSettings.getVibrate());

        final CheckBox led = (CheckBox) findViewById(R.id.edit_settings_led_checkbox);
        led.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                mSettings.setLed(led.isChecked());
            }
        });
        led.setChecked(mSettings.getLed());
    }
}
