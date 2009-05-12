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

package edu.umich.gopalkri.wakeup.data;


/**
 * Handles the persistence of Alarms and provides an interface to query and update alarms.
 * @author Gopalkrishna Sharma
 */
public class Alarm
{
    /**
     * Value used to separate values in an alarm entry - Cannot be part of an Alarm's name.
     */
    public static final String FIELD_SEPARATOR = "@@@@@";

    /**
     * Measure of distance.
     * @author Gopalkrishna Sharma
     */
    public enum Units
    {
        KM, METERS, MILES, FEET
    }

    /**
     * Converts a Unit to its integer representation.
     * @param unit Unit to be converted.
     * @return unit in it's integer representation.
     */
    public static int UnitsToInt(Units unit)
    {
        switch (unit)
        {
        case KM:
            return 0;
        case METERS:
            return 1;
        case MILES:
            return 2;
        case FEET:
            return 4;
        default:
            return -1;
        }
    }

    /**
     * Converts a Unit to its String representation.
     * @param unit Unit to be converted.
     * @return unit in its String representation.
     */
    public static String UnitsToString(Units unit)
    {
        switch (unit)
        {
        case KM:
            return "km";
        case METERS:
            return "meters";
        case MILES:
            return "miles";
        case FEET:
            return "feet";
        default:
            return null;
        }
    }

    /**
     * Converts an integer representing a Unit to a Unit.
     * @param unit Integer representing a unit to be converted.
     * @return unit in its Units representation.
     * @throws InvalidAlarmStringException When the unit is not a valid Unit.
     */
    public static Units IntToUnits(int unit) throws InvalidAlarmStringException
    {
        switch (unit)
        {
        case 0:
            return Units.KM;
        case 1:
            return Units.METERS;
        case 2:
            return Units.MILES;
        case 3:
            return Units.FEET;
        default:
            return null;
        }
    }

    /**
     * Default constructor - does nothing.
     */
    public Alarm()
    {}

    /**
     * Initializes this object after decoding alarmStr.
     * @param alarmStr String representation of alarm.
     * @throws InvalidAlarmStringException When alarmStr is not valid.
     */
    public Alarm(String alarmStr) throws InvalidAlarmStringException
    {
        String split[] = alarmStr.split(FIELD_SEPARATOR);
        if (split.length != 5)
        {
            throw new InvalidAlarmStringException();
        }
        name = split[0];
        try
        {
            latitude = Double.parseDouble(split[1]);
            longitude = Double.parseDouble(split[2]);
            radius = Double.parseDouble(split[3]);
            unit = IntToUnits(Integer.parseInt(split[4]));
        }
        catch (NumberFormatException e)
        {
            throw new InvalidAlarmStringException();
        }
        if (unit == null)
        {
            throw new InvalidAlarmStringException();
        }
    }

    /**
     * Initializes this object with arguments passed in.
     * @param name Name of alarm.
     * @param latitude Latitude of destination.
     * @param longitude Longitude of destination.
     * @param radius Proximity radius
     * @param unit Unit of measurement of proximity radius.
     * @throws InvalidAlarmNameException When alarm name is invalid.
     */
    public Alarm(String name, double latitude, double longitude, double radius, Units unit)
            throws InvalidAlarmNameException
    {
        if (name.contains(FIELD_SEPARATOR))
        {
            throw new InvalidAlarmNameException();
        }
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.unit = unit;
    }

    /**
     * Encodes this object into a String representation.
     * @return String representation of this Alarm object.
     */
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(FIELD_SEPARATOR);
        sb.append(latitude);
        sb.append(FIELD_SEPARATOR);
        sb.append(longitude);
        sb.append(FIELD_SEPARATOR);
        sb.append(radius);
        sb.append(FIELD_SEPARATOR);
        sb.append(UnitsToInt(unit));
        return sb.toString();
    }

    /**
     * Gets name of this Alarm.
     * @return Name of this Alarm.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name of this Alarm.
     * @param name Name of alarm.
     * @throws InvalidAlarmNameException When name is an invalid Alarm name.
     */
    public void setName(String name) throws InvalidAlarmNameException
    {
        if (name.contains(FIELD_SEPARATOR))
        {
            throw new InvalidAlarmNameException();
        }
        this.name = name;
    }

    /**
     * Gets latitude of destination of this Alarm.
     * @return Latitude of destination.
     */
    public double getLatitude()
    {
        return latitude;
    }

    /**
     * Sets latitude of destination of this Alarm.
     * @param latitude Latitude of destination.
     */
    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    /**
     * Gets longitude of destination of this Alarm.
     * @return Longitude of destination.
     */
    public double getLongitude()
    {
        return longitude;
    }

    /**
     * Sets longitude of destination of this Alarm.
     * @param longitude Longitude of destination.
     */
    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    /**
     * Gets proximity radius of this Alarm in Units returned by getUnit().
     * @return Proximity radius.
     */
    public double getRadius()
    {
        return radius;
    }

    /**
     * Sets proximity radius of this Alarm.
     * @param radius Proximity radius.
     */
    public void setRadius(double radius)
    {
        this.radius = radius;
    }

    /**
     * Gets proximity radius of this Alarm in meters.
     * @return Proximity radius.
     */
    public double getRadiusInMeters()
    {
        switch (unit)
        {
        case KM:
            return radius * 1E3;
        case METERS:
            return radius;
        case MILES:
            return radius * 1609.344; // 1 mile = 1609.344 meters.
        case FEET:
            return radius * 0.3048; // 1 foot = 0.3048 meters.
        }
        // Should never happen.
        return -1;
    }

    /**
     * Gets unit of measurement of proximity radius of this Alarm.
     * @return Unit of measurement of proximity radius.
     */
    public Units getUnit()
    {
        return unit;
    }

    /**
     * Sets unit of measurement of proximity radius of this Alarm.
     * @param unit Unit of measurement of proximity radius.
     */
    public void setUnit(Units unit)
    {
        this.unit = unit;
    }

    /**
     * Name of Alarm.
     */
    private String name;

    /**
     * Latitude of destination.
     */
    private double latitude;

    /**
     * Longitude of destination.
     */
    private double longitude;

    /**
     * Proximity radius.
     */
    private double radius;

    /**
     * Unit of radius.
     */
    private Units unit;
}
