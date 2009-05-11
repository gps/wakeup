package edu.umich.gopalkri.wakeup;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

public class SelectDestination extends MapActivity
{
    public static final String LOCATION_STRING = "LOCATION_STRING";

    private static final int DEFAULT_ZOOM = 15;

    private static final int MENU_DONE = Menu.FIRST;

    private static final String LOCATION_NOT_SET = "You have not selected a location yet. Please do so by tapping anywhere on the map and try again.";

    private DestinationOverlay mDestinationOverlay;

    /**
     * @see com.google.android.maps.MapActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_destination);

        MapView mapView = (MapView) findViewById(R.id.select_destination_mapview);
        mapView.setSatellite(true);

        GeoPoint currentLocation = getCurrentLocation();
        if (currentLocation != null)
        {
            mapView.getController().setCenter(currentLocation);
            mapView.getController().setZoom(DEFAULT_ZOOM);
        }

        //LinearLayout zoomLayout = (LinearLayout) findViewById(R.id.select_destination_zoomview);
        //zoomLayout.addView(mapView.getZoomControls());
        mapView.setBuiltInZoomControls(true);

        mMyLocationOverlay = new MyLocationOverlay(this, mapView);
        mMyLocationOverlay.enableMyLocation();
        mapView.getOverlays().add(mMyLocationOverlay);

        GeoPoint plotLocation = null;
        if (savedInstanceState != null)
        {
            String plotLocationStr = savedInstanceState.getString(LOCATION_STRING);
            if (plotLocationStr != null)
            {
                plotLocation = Utilities.decodeLocationString(plotLocationStr);
            }
        }
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
                Utilities.reportError(this, Utilities.ERROR, LOCATION_NOT_SET);
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
