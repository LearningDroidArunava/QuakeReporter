package com.example.android.quakereporter;

/**
 * Created by arunava on 31/12/16.
 */

import android.app.Activity;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/**
 * The {@link EarthquakeLoader} loader
 */
public class EarthquakeLoader extends AsyncTaskLoader<ArrayList<Quake>> {

    /**
     * The base URL from which the JSON response will be derived
     */
    public static final String QUERY_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?for" +
            "mat=geojson&starttime=2016-01-01&endtime=2016-01-02";


    /**
     * Custom Constructor to create an instance of this Loader class
     *
     * @param context The context of the application
     */

    EarthquakeLoader(Context context) {

        // Calling the super class constructor with the context
        super(context);
    }

    /**
     * Overriding the loadInBackground method to
     * load and parse the JSON data in the background thread
     *
     * @return An {@link ArrayList} containing {@link Quake} objetcs
     */
    @Override
    public ArrayList<Quake> loadInBackground() {

        /**
         * Fetching the JSON data from over the network
         * using {@link HttpsURLConnection}
         */
        // Declaring the String that will store the JSON data
        String jsonData;

        // Declaring the json object
        JSONObject jsonRootObject;

        // Declaring the JSONArray that will hold the array with
        // the "features" value in the JSON data
        JSONArray jsonRootArray;

        // Declaring an ArrayList to store the Quake objects
        ArrayList<Quake> quakeArrayList = new ArrayList<>();

        // Using a try block to get the JSON data as it can throw
        // an IOException
        try {
            // Calling the method that would make the network call
            // and return the JSON response
            jsonData = fetchJSONFromUSGS();

            // Checking if JSON data is null
            if (jsonData == null) {

                // returning null if jsonData is empty
                return null;
            }
            /**
             * Converting the jsonData to {@link org.json.JSONObject}
             * and extracting the "features" JSON array with an
             * instance of {@link org.json.JSONArray}
             */
            // Creating an instance of the json data received
            jsonRootObject = new JSONObject(jsonData);

            // Creating a json array of "features" value in the given JSON data
            jsonRootArray = jsonRootObject.getJSONArray("features");


            /**
             * If the JSON data is succesfully parsed then we add each
             * earthquake in it to the {@link ArrayList<Quake>} by creating
             * a {@link Quake} object from each earthquake in the "features" array
             * in the JSON data
             */
            // Declaring json object that will be used to store the json
            // array earthquake elements
            JSONObject jsonObject;

            // Declaring the json array that will hold
            // the "properties" array in each json object
            JSONObject jsonPropertiesObject;

            // Declaring var to store the properties of the earthquake
            String mag;
            String place, area, location;
            Long time;
            String[] geo;

            // Looping over each element of the json array created
            // and creating a quake object of the element
            // and adding it to the arrayList
            for (int i = 0; i < jsonRootArray.length(); i++) {

                // Getting the JSON object instance of the element at
                // i-th position of the jsonArray
                jsonObject = jsonRootArray.getJSONObject(i);

                // Creating an instance of the json array of the
                // "properties" value in "features" array
                jsonPropertiesObject = jsonObject.getJSONObject("properties");

                /**
                 * Getting the required details of the earthquake
                 * from the "properties" JSON array
                 */
                // Getting the magnitude
                mag = jsonPropertiesObject.get("mag").toString();

                // Modifying the magnitude
                // try to get the first three digits
                try {
                    mag = mag.substring(0, 3);
                }
                catch (Exception e) {

                    // If cannot substring first three digits
                    // then we have only one digit in the string
                    // thus appending ".0" to it
                    mag += ".0";
                }

                // Getting the location of the earthquake
                place = jsonPropertiesObject.get("place").toString();

                // Regex on location to seperate the area and exact location
                // Wrapping in a try block as some place value cannot be split
                try {
                    // Splitting the place
                    geo = place.split(",");

                    // Getting the area and location from the regex
                    area = geo[1].trim();
                    location = geo[0].trim();
                }
                catch (Exception e) {

                    area = "";
                    location = place;
                }
                // Getting the time
                time = Long.parseLong(jsonPropertiesObject.get("time").toString());

                // passing all these properties to form a Quake object
                // and adding that to the ArrayList<Quake>
                quakeArrayList.add(new Quake(mag, area, location, time));
            }
        }
        // Catching the IOException thrown
        catch (Exception e) {

            // Updating the UI with a no network status
            //updateUI(NO_NETWORK);

            e.printStackTrace();
            return quakeArrayList;
        }


        // Returning the arrayList to update the UI
        return quakeArrayList;
    }

    /**
     * Method to make the expensive network calls
     * to retrive the JSON response of the earthquakes
     * called by background thread
     *
     * @return The JSON response
     */
    private String fetchJSONFromUSGS() throws IOException {

        /**
         * Checking whether there is an avilibility of
         * network connection in the android device
         */
        NetworkInfo networkInfo = checkNetworkStatus();

        // Checking if the device is connected to the network
        if (networkInfo == null || !networkInfo.isConnected()) {

            // returning null as there is no working internet connection
            return null;
        }
        // The URL from which the JSON data will be retrieved
        // The given String of URL is converted to  {@link URL}
        // by passing it to the convertToURL() method
        // which returns a {@link URL} object
        URL fetchURL = convertToURL(QUERY_URL);

        // Cheking if the returned URL is null
        if (fetchURL == null) {

            // Returning null as further operations cannot be
            // performed with malformed URl
            return null;
        }

        // String to store JSON data
        String json = null;

        // Declaring variable to store the InputStream of data
        InputStream stream = null;

        // Declaring a variable for the HttpsUrlConnection
        HttpsURLConnection connection = null;

        // Connecting to the USGS website to retrieve data
        // Using a try block since the Https queries throws IOException
        try {

            // Opening a URL connection
            connection = (HttpsURLConnection) fetchURL.openConnection();

            // Setting manually the connection read timeout
            connection.setReadTimeout(5000);

            // Setting the connection timeout manually
            connection.setConnectTimeout(5000);

            // Setting the request method
            connection.setRequestMethod("GET");

            // setting the connection setDoInput() method to be true
            connection.setDoInput(true);

            // Opening communications link (Network operations occurs here)
            connection.connect();

            // Getting the response code for the network communication
            int responseCode = connection.getResponseCode();

            // Checking the response code to check whether the network operation
            // was successful or not
            if (responseCode != HttpsURLConnection.HTTP_OK) {

                // Throwing a IOException if the responseCode
                // is not HttpsURLConnection.HTTP_OK
                throw new IOException("HTTP ERROR CODE" + responseCode);
            }

            // Getting the InputStream for the network connection made
            stream = connection.getInputStream();

            // Checking if the inputStream is null or not
            if (stream != null) {

                // Reading the JSON response from the stream
                // NOTE -> This json response is in String format
                json = readStream(stream);
            }
        }

        // Now if the try block ran without throwing any exceptions
        // then this block of code is to be executed
        finally {

            // checking if the stream is null or not
            if (stream != null) {

                // If not null then closing the stream
                stream.close();
            }

            // Checking if the connection is there
            if (connection != null) {

                // If the connection is not null then we are closing the connection
                connection.disconnect();
            }
        }


        // returning the json response
        return json;
    }

    /**
     * Method to convert a given {@link String} into {@link URL}
     *
     * @param strURL The URL in the {@link String} form
     * @return The {@link URL} object of the given {@link String} URL
     */
    private URL convertToURL(String strURL) {

        // returning the URL form of the given {@link String}
        // using try-catch block as converting the given URL
        // can throw a MalformedURLException
        try {

            // returning the URL form of the string given
            return (new URL(strURL));
        }

        // catching the MalformedURLException if thrown
        catch (MalformedURLException e) {

            // Logging to the console about the error
            Log.e("ERROR", "Malformed URL String Provided!");

            // returning null
            return null;

        }

    }

    /**
     * Method to convert the InputStream from the network
     * into {@link String} format
     *
     * @param stream The {@link InputStream} from the network
     * @return The {@link String} form of the {@link  InputStream} from the network
     */
    private String readStream(InputStream stream) throws IOException {

        // Declaring he variable to return the result
        String result = null;

        // Initializing a {@link InputStreamReader} to read the input stream
        // with UTF-8 charset
        InputStreamReader reader = new InputStreamReader(stream, "UTF-8");

        // Creating a new instance of the {@link BufferedReader} class
        // to buffer through the unkown quantity of the JSON data
        BufferedReader bufferedReader = new BufferedReader(reader);

        // Creating a new instance of {@link StringBuilder} to create a string
        // from the JSON data received
        StringBuilder outputString = new StringBuilder();

        // Reading a line from the stream
        String line = bufferedReader.readLine();

        // Cheking whether the line is empty or not
        while (line != null) {

            // Appending the line read to the output string
            outputString.append(line);

            // Reading one more line from the loop
            line = bufferedReader.readLine();

            // Reapeating the loop till we reach the end
            // that is the line returned is null

        }

        // returnong the string read
        return outputString.toString();

    }

    /**
     * Method to check the current network status
     *
     * @return An instance of {@link NetworkInfo} of the device which is the
     * current network status
     */
    private NetworkInfo checkNetworkStatus() {

        // Getting the connectivity service of the device
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Getting the active network info
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // returing the network info
        return networkInfo;
    }

}