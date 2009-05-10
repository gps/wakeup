package edu.umich.gopalkri.wakeup;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.maps.MapActivity;

public class SelectDestination extends MapActivity
{
    /**
     * @see com.google.android.maps.MapActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected boolean isRouteDisplayed()
    {
        return false;
    }

    /**
     * @see com.google.android.maps.MapActivity#isLocationDisplayed()
     */
    @Override
    protected boolean isLocationDisplayed()
    {
        return true;
    }

    /**
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // TODO Auto-generated method stub
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // TODO Auto-generated method stub
        return super.onOptionsItemSelected(item);
    }
}
