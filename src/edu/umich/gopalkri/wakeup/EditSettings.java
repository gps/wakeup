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
        vibrate.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                mSettings.setLed(led.isChecked());
            }
        });
        vibrate.setChecked(mSettings.getLed());
    }
}
