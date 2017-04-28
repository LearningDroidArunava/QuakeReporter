package com.example.android.quakereporter;


import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;

import android.content.SharedPreferences;
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

    ProgressBar mProgressBar;
    TextView mEmptyView;

    ActionBar mActionBar;

    protected SharedPreferences mQuakePreferences;

    protected String SEARCH_URL;

    protected String QUAKE_PREFERENCES = "QUAKE PREFERENCE";
    protected String QUAKE_URL = "QUAKE URL";

    protected Boolean mLoaderInit = false;


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

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        mQuakePreferences = getSharedPreferences(QUAKE_PREFERENCES, MODE_PRIVATE);

        SEARCH_URL = mQuakePreferences.getString(QUAKE_URL, null);

        if (SEARCH_URL == null) {

            searchQuakeClicked();
        } else {

            getLoaderManager().initLoader(fetchJSONID, null, this).forceLoad();
            mLoaderInit = true;
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SEARCH_QUAKE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                SEARCH_URL = data.getData().toString();
                SharedPreferences.Editor sharedEditor = mQuakePreferences.edit();
                sharedEditor.putString(QUAKE_URL, SEARCH_URL).apply();

                if (!mLoaderInit) {
                    getLoaderManager().initLoader(fetchJSONID, null, this).forceLoad();
                    mLoaderInit = true;
                } else {
                    getLoaderManager().restartLoader(fetchJSONID, null, this).forceLoad();
                }
            }
        }
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
                searchQuakeClicked();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void searchQuakeClicked(){

        Intent searchQuakeIntent = new Intent(getApplicationContext(),
                SearchActivity.class);
        startActivityForResult(searchQuakeIntent, SEARCH_QUAKE_REQUEST_CODE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

        @Override
        public Loader<ArrayList<Quake>> onCreateLoader(int loaderID, Bundle args) {

            mProgressBar.setVisibility(View.VISIBLE);
            return new EarthquakeLoader(getApplicationContext(), SEARCH_URL);
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<Quake>> loader, ArrayList<Quake> data) {

            mProgressBar.setVisibility(View.GONE);
            updateUI(data);
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Quake>> loader) {

            updateUI(new ArrayList<Quake>());
        }


}
