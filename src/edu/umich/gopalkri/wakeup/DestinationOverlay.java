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

import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class DestinationOverlay extends ItemizedOverlay<OverlayItem>
{
    private OverlayItem currentItem = null;

    public GeoPoint getSelectedLocation()
    {
        if (currentItem == null)
        {
            return null;
        }
        return currentItem.getPoint();
    }

    public DestinationOverlay(Drawable defaultMarker, GeoPoint gp)
    {
        super(boundCenterBottom(defaultMarker));
        if (gp != null)
        {
            currentItem = new OverlayItem(gp, "", "");
        }
        populate();
    }

    /**
     * @see com.google.android.maps.ItemizedOverlay#onTap(com.google.android.maps.GeoPoint, com.google.android.maps.MapView)
     */
    @Override
    public boolean onTap(GeoPoint p, MapView mapView)
    {
        super.onTap(p, mapView);

        currentItem = new OverlayItem(p, "", "");
        populate();

        return true;
    }

    @Override
    protected OverlayItem createItem(int arg0)
    {
        return currentItem;
    }

    @Override
    public int size()
    {
        if (currentItem == null)
        {
            return 0;
        }
        return 1;
    }
}
