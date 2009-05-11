package edu.umich.gopalkri.wakeup;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import edu.umich.gopalkri.wakeup.data.Alarms;

public class ManageAlarms extends ListActivity
{
    private static final int MENU_CREATE_NEW_ALARM = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;

    /**
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mAlarms = new Alarms(this);

        registerForContextMenu(getListView());
    }

    /**
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume()
    {
        super.onResume();
        updateAlarmsList();
    }

    /**
     * @see android.app.Activity#onCreateContextMenu(android.view.ContextMenu,
     *      android.view.View, android.view.ContextMenu.ContextMenuInfo)
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (!mAlarmsExist)
        {
            return;
        }
        menu.add(ContextMenu.NONE, DELETE_ID, ContextMenu.NONE,
                        R.string.manage_alarms_delete_alarm);
    }

    /**
     * @see android.app.Activity#onContextItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        if (!mAlarmsExist)
        {
            return true;
        }
        switch (item.getItemId())
        {
        case DELETE_ID:
            AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
            mAlarms.deleteAlarm(mAlarms.getAllAlarmNames()[info.position]);
            updateAlarmsList();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    /**
     * @see android.app.ListActivity#onListItemClick(android.widget.ListView,
     *      android.view.View, int, long)
     */
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l, v, position, id);
        if (!mAlarmsExist)
        {
            return;
        }
        Intent i = new Intent(this, EditAlarm.class);
        i.putExtra(EditAlarm.ALARM_NAME, mAlarms.getAllAlarmNames()[position]);
        startActivity(i);
    }

    /**
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(Menu.NONE, MENU_CREATE_NEW_ALARM, Menu.NONE, R.string.home_create_new_alarm);
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
        case MENU_CREATE_NEW_ALARM:
            Intent i = new Intent(this, EditAlarm.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateAlarmsList()
    {
        String[] entries = null;
        String[] alarmNames = mAlarms.getAllAlarmNames();
        if (alarmNames == null)
        {
            entries = NO_ALARMS;
            mAlarmsExist = false;
        }
        else
        {
            entries = alarmNames;
            mAlarmsExist = true;
        }
        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                entries));
    }

    private Alarms mAlarms;
    private boolean mAlarmsExist;

    private static final String[] NO_ALARMS = { "No alarms exist yet!" };
}
