package com.example.android.quakereporter;


import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<Quake>> {


    /**
     * Loader ID of the fetching the JSON response from the usgs website
     */
    private static final int fetchJSONID = 1;

    // String containing the NO NETWORK STATUS String
    private static final String NO_NETWORK = "NO NETWORK";

    private final int SEARCH_QUAKE_REQUEST_CODE = 3;

    // The list ID of the ListView that will contain the adapter
    ListView mListID;

    // The ID of the TextView which will be the empty view
    TextView mEmptyView;

    ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Getting the List View which will show the ArrayList<Quake>
        mListID = (ListView) findViewById(R.id.activity_main);

        // Getting the empty view
        mEmptyView = (TextView) findViewById(R.id.emptyView);

        // Setting the empty view
        mListID.setEmptyView(mEmptyView);

        Toolbar toolbar = (Toolbar) findViewById(R.id.noteActivityToolbar);
        setSupportActionBar(toolbar);

        mActionBar = getSupportActionBar();

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
        mListID.setAdapter(quakeAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {

            case R.id.searchQuake:
                Intent searchQuakeIntent = new Intent(getApplicationContext(),
                        SearchActivity.class);
                startActivityForResult(searchQuakeIntent, SEARCH_QUAKE_REQUEST_CODE);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    private void makeToast(String message){

        Toast.makeText(getApplicationContext(),
                message,
                Toast.LENGTH_SHORT).show();
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
