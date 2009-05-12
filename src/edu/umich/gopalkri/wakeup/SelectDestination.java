/*
Copyright (C) 2009 Gopalkrishna Sharma.
Email: gopalkri@umich.edu / gopalkrishnaps@gmail.com

This file is part of WakeUp!.

Wake Up! is free software: you can redistribute it and/or modify
it under the terms of the Lesser GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Wake Up! is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
Lesser GNU General Public License for more details.

You should have received a copy of the Lesser GNU General Public License
along with Wake Up!.  If not, see <http://www.gnu.org/licenses/>.
*/

package edu.umich.gopalkri.wakeup;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

public class SelectDestination extends MapActivity
{
    public static final String LOCATION_STRING = "LOCATION_STRING";

    private static final int DEFAULT_ZOOM = 15;

    private static final int MENU_DONE = Menu.FIRST;
    private static final int MENU_INSTRUCTIONS = Menu.FIRST + 1;

    private static final String LOCATION_NOT_SET = "You have not selected a location yet. Please do so by tapping anywhere on the map and try again.";
    private static final String INSTRUCTIONS = "Tap any point on the map to select it. It will be marked with a marker. To change the point, simply tap somewhere else. You can pan and zoom the map in the standard way. When you are done, hit Done from the Menu options.";

    private DestinationOverlay mDestinationOverlay;

    /**
     * @see com.google.android.maps.MapActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_destination);

        Toast.makeText(this, INSTRUCTIONS, Toast.LENGTH_LONG).show();

        MapView mapView = (MapView) findViewById(R.id.select_destination_mapview);
        mapView.setSatellite(true);

        GeoPoint plotLocation = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            String plotLocationStr = extras.getString(LOCATION_STRING);
            if (plotLocationStr != null)
            {
                plotLocation = Utilities.decodeLocationString(plotLocationStr);
            }
        }

        if (plotLocation != null)
        {
            mapView.getController().setCenter(plotLocation);
            mapView.getController().setZoom(DEFAULT_ZOOM);
        }
        else
        {
            GeoPoint currentLocation = getCurrentLocation();
            if (currentLocation != null)
            {
                mapView.getController().setCenter(currentLocation);
                mapView.getController().setZoom(DEFAULT_ZOOM);
            }
        }

        mapView.setBuiltInZoomControls(true);

        mMyLocationOverlay = new MyLocationOverlay(this, mapView);
        mMyLocationOverlay.enableMyLocation();
        mapView.getOverlays().add(mMyLocationOverlay);

        Drawable marker = getResources().getDrawable(R.drawable.map_pin_32);
        marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker.getIntrinsicHeight());
        mDestinationOverlay = new DestinationOverlay(marker, plotLocation);
        mapView.getOverlays().add(mDestinationOverlay);
    }

    /**
     * @see com.google.android.maps.MapActivity#onPause()
     */
    @Override
    protected void onPause()
    {
        mMyLocationOverlay.disableMyLocation();
        super.onPause();
    }

    /**
     * @see com.google.android.maps.MapActivity#onResume()
     */
    @Override
    protected void onResume()
    {
        mMyLocationOverlay.enableMyLocation();
        super.onResume();
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
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(Menu.NONE, MENU_DONE, Menu.NONE, R.string.select_destination_done);
        menu.add(Menu.NONE, MENU_INSTRUCTIONS, Menu.NONE, R.string.select_destination_instructions);
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
        case MENU_DONE:
            GeoPoint selectedLocation = mDestinationOverlay.getSelectedLocation();
            if (selectedLocation == null)
            {
                Utilities.createAlertDialog(this, Utilities.ERROR, LOCATION_NOT_SET).show();
                return true;
            }
            String locStr = Utilities.encodeLocation(selectedLocation);
            Intent i = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString(LOCATION_STRING, locStr);
            i.putExtras(bundle);
            setResult(RESULT_OK, i);
            finish();
            return true;
        case MENU_INSTRUCTIONS:
            Utilities.createAlertDialog(this, "Instructions", INSTRUCTIONS).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private GeoPoint getCurrentLocation()
    {
        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (loc == null)
        {
            loc = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        if (loc != null)
        {
            return getGeoPointFromLocation(loc);
        }
        else
        {
            return null;
        }
    }

    private static GeoPoint getGeoPointFromLocation(Location location)
    {
        Double lat = location.getLatitude() * 1E6;
        Double lon = location.getLongitude() * 1E6;
        return new GeoPoint(lat.intValue(), lon.intValue());
    }

    private MyLocationOverlay mMyLocationOverlay;
}
