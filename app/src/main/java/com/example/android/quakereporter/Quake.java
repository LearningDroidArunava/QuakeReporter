package com.example.android.quakereporter;

/**
 * Created by Arunava on 30/12/16.
 *
 * {@link Quake} class to create an instance of the earthquakes
 */

public class Quake {

    private String mMagnitude;
    private String mArea;
    private Long mTime;
    private String mLocation;

    /**
     * Constructor method to create an instance of {@link Quake} class
     *
     * @param mag The Magnitude of the earthquake in {@link String}
     * @param area The area in {@link String} format where the earthquake took place
     * @param time The time when the earthquake took place in {@link Long}
     * @param location The location in {@link String} where the earthquake took place
     */
    Quake(String mag, String area, String location, Long time) {

        mMagnitude = mag;
        mArea = area;
        mTime = time;
        mLocation = location;

    }

    /**
     * Getter method to return the {@param mMagnitude}
     * which is the magnitude of the earthquake
     *
     * @return The magnitude of the earthquake
     */
    public String getMagnitude() {

        return mMagnitude;
    }

    /**
     * Getter method to get the time in String format
     *
     * @return String value which is the time in which the earthquake took place
     */
    public Long getTime() {

        return mTime;
    }

    /**
     * Getter method to get the address of the place\
     * where the earthquake took place
     *
     * @return The place where the earthquake took place in {@link String}
     */
    public String getArea() {

        return mArea;
    }

    /**
     * Getter method to get the location of where the earthquake
     * took place
     *
     * @return The location where the earthquake took place
     */
    public String getLocation() {

        return mLocation;
    }


}
