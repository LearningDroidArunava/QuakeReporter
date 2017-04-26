package com.example.android.quakereporter;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Spinner;

public class SearchActivity extends AppCompatActivity {

    ActionBar mActionBar;
    Spinner mOrderBySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.searchActivityToolbar);
        setSupportActionBar(toolbar);


    }

    private void getReferences() {

        mActionBar = getSupportActionBar();
        mOrderBySpinner = (Spinner) findViewById(R.id.orderBySpinner);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_toolbar_menu, menu);
        return true;
    }
}
