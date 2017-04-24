package com.example.android.quakereporter;


import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

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

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<Quake>> {


    /**
     * Loader ID of the fetching the JSON response from the usgs website
     */
    private static final int fetchJSONID = 1;

    // String containing the NO NETWORK STATUS String
    private static final String NO_NETWORK = "NO NETWORK";

    // The list ID of the ListView that will contain the adapter
    ListView listID;

    // The ID of the TextView which will be the empty view
    TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Getting the List View which will show the ArrayList<Quake>
        listID = (ListView) findViewById(R.id.activity_main);

        // Getting the empty view
        emptyView = (TextView) findViewById(R.id.emptyView);

        // Setting the empty view
        listID.setEmptyView(emptyView);

        getLoaderManager().initLoader(fetchJSONID, null, this).forceLoad();

    }

    /**
     * Method to update the UI view with the ArrayList<Quake> provided
     * @param quakeArrayList
     */
    private void updateUI(ArrayList<Quake> quakeArrayList) {

        if (quakeArrayList == null)
            return;

        // Creating an instance of the QuakeAdapter
        QuakeAdapter quakeAdapter = new QuakeAdapter(this, quakeArrayList);

        // Setting the adapter to the list
        listID.setAdapter(quakeAdapter);
    }






        @Override
        public Loader<ArrayList<Quake>> onCreateLoader(int loaderID, Bundle args) {

            return new EarthquakeLoader(getApplicationContext());
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<Quake>> loader, ArrayList<Quake> data) {

            // Make the progress bar invisible
            // Getting the progress bar id
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

            // Setting its visibility to GONE
            progressBar.setVisibility(View.GONE);

            // returning data to update UI
            updateUI(data);
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Quake>> loader) {

            updateUI(new ArrayList<Quake>());
        }


}
