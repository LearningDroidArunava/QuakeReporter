package com.example.android.quakereporter;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by arunava on 30/12/16.
 *
 * Custom created QuakeAdapter to inflate the Quake objects
 * manually
 */

public class QuakeAdapter extends ArrayAdapter<Quake> {

    /**
     * Constructor to inflate the quake objects
     *
     * @param activity The current activity
     * @param quakeArrayList The quake arrayList that the Adapter will inflate
     */

    QuakeAdapter(Activity activity, ArrayList<Quake> quakeArrayList) {

        /**
         *Calling the super class constructor
         * passing the activity and the list
         * since custom class is being used
         * so we do not pass the resource id to
         * expand so it is passed 0
         */
        super(activity, 0, quakeArrayList);
    }

    /**
     * Overriding the getView() method of the super class
     * to manually expand Quake objects
     *
     * @param position The position of the element in the {@link ArrayList}
     *                 that is to be displayed
     * @param convertView The recyclable view that can be used
     * @param parent The parent ViewGroup where this {@link QuakeAdapter}
     *               inflates eah view
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        /** Setting the currentView equals to the convertView
         *  @param convertView contains View object that can be reused
         *                     that is previously created View so we dont have
         *                     to create a new view and recycle the old one
         */

        View currentView = convertView;

        /**
         * Handling the case if there is no view to be recycled
         * that is the {@param convertView} is null
         */
        if (currentView == null) {

            // If the current view is null then we are inflating a new View
            // And setting it to the parent ViewGroup

            currentView = LayoutInflater.from(getContext()).inflate(R.layout.aquake, parent, false);
        }

        // Getting the current {@link Quake} object that is to be displayed
        Quake currentQuake = getItem(position);

        /**
         * Setting the area of the current view
         */
        // Getting the ID of the @id/addressText
        TextView areaTextID = (TextView) currentView.findViewById(R.id.areaText);

        // Setting the current address
        areaTextID.setText(currentQuake.getArea());


        /**
         * Setting the magnitude of the current View
         */
        // Getting the id of the text displaying the magnitude
        TextView magnitudeText = (TextView) currentView.findViewById(R.id.magnitudeText);

        // Setting the magnitude to the current magnitude
        magnitudeText.setText(currentQuake.getMagnitude());

        /**
         * Changing the color of the background circle
         * according to the magnitude of the earthquake
         */
        // Getting the Drawable backround
        GradientDrawable drawableBackground
                = (GradientDrawable) magnitudeText.getBackground();

        // Getting the desired color ID
        int magnitudeColor = getColorForQuake(Float.parseFloat(currentQuake.getMagnitude()));

        // Setting the desired color
        //drawableBackground.setColor(ContextCompat.getColor(getContext(), magnitudeColor));

        drawableBackground.setColor(getColorForQuake(Float.parseFloat(currentQuake.getMagnitude())));

       // drawableBackground.setColor(R.color.colorPrimaryDark);
        /**
         * Getting the date and time of the current quake instance
         * and deriving it into date and time
         */
        // Getting the time in quake instance and passing it
        // into {@link Date} object constructor
        Date dateObject = new Date(currentQuake.getTime());

        // Getting the Date from the Date object
        // by passing it to the getDate() method
        String date = getDate(dateObject);

        // Getting the time from the Date object
        // by passing it to the getTime() method

        String time = getTime(dateObject);


        /**
         * Setting the time of the current view
         */
        // Getting the text ID showing time
        TextView timeTextID = (TextView) currentView.findViewById(R.id.timeText);

        // Setting the time current view
        timeTextID.setText(time);

        /**
         * Seting the date of the current view
         */
        // Getting the view displaying the date
        TextView dateTextID = (TextView) currentView.findViewById(R.id.dateText);

        // Setting the date in the current view
        dateTextID.setText(date);

        /**
         * Setting the location of the earthquake in the UI
         */
        // Getting the TextView displaying the location
        TextView locationID = (TextView) currentView.findViewById(R.id.locationText);

        // Setting the location text to the current location
        locationID.setText(currentQuake.getLocation());

        // Returning the current View
        return currentView;
    }

    private String getDate(Date dateObject) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    private String getTime(Date timeobject) {

        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        return timeFormat.format(timeobject);
    }

    /**
     * Method to return the desired color for a
     * given magnitude
     *
     * @param mag The magnitude of the earthquake
     *
     * @return The @color resource id of the color that is to be set
     */
    private int getColorForQuake(Float mag) {

        // Casting the double magtnitude value to int
        int intMag = mag.intValue();

        // Int to store the color id to return
        int colorID = R.color.magnitude1;
        // Returning the color according to the magnitude
        switch (intMag) {

            case 0:
            case 1:

                colorID = R.color.magnitude1;
                break;

            case 2:
                colorID = R.color.magnitude2;
                break;

            case 3:
                colorID = R.color.magnitude3;
                break;

            case 4:
                colorID = R.color.magnitude4;
                break;

            case 5:
                colorID = R.color.magnitude5;
                break;

            case 6:
                colorID = R.color.magnitude6;
                break;

            case 7:
                colorID = R.color.magnitude7;
                break;

            case 8:
                colorID = R.color.magnitude8;
                break;

            case 9:
                colorID = R.color.magnitude9;
                break;

            case 10:
                colorID = R.color.magnitude10plus;
                break;

        }

        // Returning the color
        return ContextCompat.getColor(getContext(), colorID);
    }
}
