package com.example.android.quakereporter;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.view.View.OnFocusChangeListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchActivity extends AppCompatActivity {

    protected ActionBar mActionBar;

    protected Spinner mOrderBySpinner;
    protected Spinner mPlusMinusSpinner;

    protected TextInputEditText mStartDateEdtTxt;
    protected TextInputEditText mEndDateEdtTxt;
    protected TextInputEditText mStartTimeEdtTxt;
    protected TextInputEditText mEndTimeEdtTxt;
    protected TextInputEditText mMinMagEdtTxt;
    protected TextInputEditText mMaxMagEdtTxt;

    protected EditText mTimeZoneEdtTxt;

    protected TextInputLayout mStartDateTxtInputLayout;
    protected TextInputLayout mEndDateTxtInputLayout;
    protected TextInputLayout mStartTimeTxtInputLayout;
    protected TextInputLayout mEndTimeTxtInputLayout;
    protected TextInputLayout mMinMagTxtInputLayout;
    protected TextInputLayout mMaxMagTxtInputLayout;
    protected TextInputLayout mTimeZoneTxtInputLayout;

    protected TextWatcher mDateWatcher;
    protected TextWatcher mTimeWatcher;
    protected TextWatcher mMagnitudeWatcher;

    private Boolean mEditError = false;

    protected OnFocusChangeListener mDateFieldFocusChngListnr;
    protected OnFocusChangeListener mTimeFieldFocusChngListnr;
    protected OnFocusChangeListener mMagnitudeFocusChngListnr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.searchActivityToolbar);
        setSupportActionBar(toolbar);

        getReferences();

        setUpTheSpinners();

        setUpTheTextWatchers();

        setUpTheTextChangedListeners();

        setUpTheFocusChangeListeners();

        setUpTheOnFocusChangedListeners();
    }

    private void setUpTheOnFocusChangedListeners() {

        mStartDateEdtTxt.setOnFocusChangeListener(mDateFieldFocusChngListnr);
        mEndDateEdtTxt.setOnFocusChangeListener(mDateFieldFocusChngListnr);
        mStartTimeEdtTxt.setOnFocusChangeListener(mTimeFieldFocusChngListnr);
        mEndTimeEdtTxt.setOnFocusChangeListener(mTimeFieldFocusChngListnr);
        mTimeZoneEdtTxt.setOnFocusChangeListener(mTimeFieldFocusChngListnr);
        mMinMagEdtTxt.setOnFocusChangeListener(mMagnitudeFocusChngListnr);
        mMaxMagEdtTxt.setOnFocusChangeListener(mMagnitudeFocusChngListnr);
    }

    private void setUpTheFocusChangeListeners() {

        mDateFieldFocusChngListnr = new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                TextInputEditText currInputEdtTxt = ((TextInputEditText) v);
                String inputEdtTxtValue = currInputEdtTxt.getText().toString();

                if (!hasFocus) {

                    Pattern pattern = Pattern.compile("\\d{4}-[01]\\d{1}-[0123]\\d{1}");
                    Matcher matcher = pattern.matcher(inputEdtTxtValue);

                    if (!matcher.matches() && !inputEdtTxtValue.isEmpty()) {
                        Log.i("asas", ((TextInputEditText) v).getText().toString());
                        ((TextInputLayout) findViewById(getCurrTextInputLayoutID(v.getId()))).setError("Invalid Date Format!");
                        mEditError = true;
                    } else {

                        ((TextInputLayout) findViewById(getCurrTextInputLayoutID(v.getId()))).setError(null);
                        mEditError = false;
                    }
                } else {

                    ((TextInputLayout) findViewById(getCurrTextInputLayoutID(v.getId()))).setError(null);
                }
            }
        };

        mTimeFieldFocusChngListnr = new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                TextInputEditText currInputEdtTxt = ((TextInputEditText) v);
                String inputEdtTxtValue = currInputEdtTxt.getText().toString();

                if (!hasFocus) {

                    Pattern pattern = Pattern.compile("[01]\\d{1}:[0-6]\\d{1}");
                    Matcher matcher = pattern.matcher(inputEdtTxtValue);

                    if (!matcher.matches() && !inputEdtTxtValue.isEmpty()) {

                        ((TextInputLayout) findViewById(getCurrTextInputLayoutID(v.getId()))).setError("Invalid Time Format!");
                        mEditError = true;
                    } else {

                        ((TextInputLayout) findViewById(getCurrTextInputLayoutID(v.getId()))).setError(null);
                        mEditError = false;
                    }
                } else {
                    ((TextInputLayout) findViewById(getCurrTextInputLayoutID(v.getId()))).setError(null);
                }
            }
        };

        mMagnitudeFocusChngListnr = new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                EditText currInputEdtTxt = ((EditText) v);
                String inputEdtTxtValue = currInputEdtTxt.getText().toString();
                if (!hasFocus) {

                    Pattern pattern = Pattern.compile("\\d[.]\\d");
                    Matcher matcher = pattern.matcher(inputEdtTxtValue);

                    if (!matcher.matches() && !inputEdtTxtValue.isEmpty()) {

                        ((TextInputLayout) findViewById(getCurrTextInputLayoutID(v.getId()))).setError("Invalid Magnitude!");
                        mEditError = true;
                    } else {

                        ((TextInputLayout) findViewById(getCurrTextInputLayoutID(v.getId()))).setError(null);
                        mEditError = false;
                    }
                }
            }
        };
    }

    private void setUpTheTextWatchers() {

        mDateWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                int currId = -1;
                int currTextInputLayoutId = -1;
                TextInputLayout txtLayout = null;

                TextInputEditText currEditTxt = (TextInputEditText) getCurrentFocus();
                try {
                   currId = currEditTxt.getId();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                if (currId != -1) {

                    currTextInputLayoutId = getCurrTextInputLayoutID(currId);
                }

                if (currTextInputLayoutId != -1) {

                    txtLayout = (TextInputLayout) findViewById(currTextInputLayoutId);
                }

                if (s.length()-1 == 4 || s.length()-1 == 7) {

                    if (s.toString().charAt(s.length()-1) != '-') {

                        txtLayout.setError("Invalid Date!");
                        mEditError = true;
                    } else {

                        txtLayout.setError(null);
                        mEditError = false;
                    }
                }


                }
            };

            mTimeWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    int currId = -1;
                    int currTextInputLayoutId = -1;
                    TextInputLayout txtLayout = null;

                    TextInputEditText currEditTxt = (TextInputEditText) getCurrentFocus();
                    try {
                        currId = currEditTxt.getId();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                    if (currId != -1) {

                        currTextInputLayoutId = getCurrTextInputLayoutID(currId);
                    }

                    if (currTextInputLayoutId != -1) {

                        txtLayout = (TextInputLayout) findViewById(currTextInputLayoutId);
                    }

                    if (s.length()-1 == 2) {

                        if (s.toString().charAt(s.length()-1) != ':') {

                            try {
                                txtLayout.setError("Invalid Time!");
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                                ((EditText)s).setError("Invalid Time!");
                            }
                            mEditError = true;
                        } else {

                            try {
                                txtLayout.setError(null);
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                                ((EditText) s).setError(null);
                            }
                        }
                    }
                }
            };

            mMagnitudeWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    EditText currEditTxt = (EditText) getCurrentFocus();

                    if (s.length() - 1 == 1) {

                        if (s.toString().charAt(s.length() - 1) != '.') {

                            try {
                                currEditTxt.setError("Invalid Magnitude!");
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                                ((EditText) s).setError("Invalid Magnitude!");
                            }
                            mEditError = true;
                        } else {

                            try {
                                currEditTxt.setError(null);
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                                ((EditText) s).setError(null);
                            }
                        }
                    }
                }
            };
        }

    private int getCurrTextInputLayoutID(int inputEdtTxtID) {

        switch (inputEdtTxtID) {

            case R.id.startDateEditText:
                return R.id.textInputLayoutStartDate;

            case R.id.startTimeEditText:
                return R.id.textInputLayoutStartTime;

            case R.id.endDateEditText:
                return R.id.textInputLayoutEndDate;

            case R.id.endTimeEditText:
                return R.id.textInputLayoutEndTime;

            case R.id.minMagEditText:
                return R.id.textInputLayoutMinMag;

            case R.id.maxMagEditText:
                return R.id.textInputLayoutMaxMag;

            case R.id.timeZoneEditText:
                return R.id.textInputLayoutTimeZone;

            default:
                return -1;
        }
    }

    private void setUpTheTextChangedListeners() {

        mStartDateEdtTxt.addTextChangedListener(mDateWatcher);
        mEndDateEdtTxt.addTextChangedListener(mDateWatcher);
        mStartTimeEdtTxt.addTextChangedListener(mTimeWatcher);
        mEndTimeEdtTxt.addTextChangedListener(mTimeWatcher);
        mTimeZoneEdtTxt.addTextChangedListener(mTimeWatcher);
        mMinMagEdtTxt.addTextChangedListener(mMagnitudeWatcher);
        mMaxMagEdtTxt.addTextChangedListener(mMagnitudeWatcher);
    }

    private void setUpTheSpinners() {

        /* Setting Up the Order By Spinner */
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.orderByArray, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mOrderBySpinner.setAdapter(adapter);

        /* Setting up the Plus Minus Spinner */
        ArrayAdapter<CharSequence> plusMinusAdapter = ArrayAdapter.createFromResource(this,
                R.array.plusMinusArray, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPlusMinusSpinner.setAdapter(plusMinusAdapter);
    }

    private void getReferences() {

        mActionBar        = getSupportActionBar();
        mOrderBySpinner   = (Spinner) findViewById(R.id.orderBySpinner);
        mStartDateEdtTxt  = (TextInputEditText) findViewById(R.id.startDateEditText);
        mEndDateEdtTxt    = (TextInputEditText) findViewById(R.id.endDateEditText);
        mEndTimeEdtTxt    = (TextInputEditText) findViewById(R.id.endTimeEditText);
        mStartTimeEdtTxt  = (TextInputEditText) findViewById(R.id.startTimeEditText);
        mMinMagEdtTxt     = (TextInputEditText) findViewById(R.id.minMagEditText);
        mMaxMagEdtTxt     = (TextInputEditText) findViewById(R.id.maxMagEditText);
        mTimeZoneEdtTxt   = (EditText) findViewById(R.id.timeZoneEditText);
        mPlusMinusSpinner = (Spinner) findViewById(R.id.plusMinusSpinner);

        mStartDateTxtInputLayout = (TextInputLayout) findViewById(R.id.textInputLayoutStartDate);
        mEndDateTxtInputLayout   = (TextInputLayout) findViewById(R.id.textInputLayoutEndDate);
        mStartTimeTxtInputLayout = (TextInputLayout) findViewById(R.id.textInputLayoutStartTime);
        mEndTimeTxtInputLayout   = (TextInputLayout) findViewById(R.id.textInputLayoutEndTime);
        mMinMagTxtInputLayout    = (TextInputLayout) findViewById(R.id.textInputLayoutMinMag);
        mMaxMagTxtInputLayout    = (TextInputLayout) findViewById(R.id.textInputLayoutMaxMag);
        mTimeZoneTxtInputLayout  = (TextInputLayout) findViewById(R.id.textInputLayoutTimeZone);

    }

    private void makeToast(String message){

        Toast.makeText(getApplicationContext(),
                message,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {

            case R.id.editComplete:

                if (!mEditError) {

                    getEdits();
                } else {

                    makeToast("Please Check and Rectify the Input Fields!");
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getEdits() {

        String startDate;
        String endDate;
        String startTime;
        String endTime;
        String timeZone;
        String minMag;
        String maxMag;
        String orderBy;
        String plusMinus;
        String url = "https://earthquake.usgs.gov/fdsnws/event/1/query?for" +
                "mat=geojson";

        startDate = mStartDateEdtTxt.getText().toString();
        endDate   = mEndDateEdtTxt.getText().toString();
        startTime = mStartTimeEdtTxt.getText().toString();
        endTime   = mEndTimeEdtTxt.getText().toString();
        timeZone  = mTimeZoneEdtTxt.getText().toString();
        maxMag    = mMaxMagEdtTxt.getText().toString();
        minMag    = mMinMagEdtTxt.getText().toString();
        orderBy   = mOrderBySpinner.getSelectedItem().toString();
        plusMinus = mPlusMinusSpinner.getSelectedItem().toString();

        if (startDate.isEmpty()) {
            mStartDateTxtInputLayout.setError("Required Field!");
            makeToast("Specify a Start Date!");
            return;
        }

        if (endDate.isEmpty()) {
            mEndDateTxtInputLayout.setError("Required Field!");
            makeToast("Specify a End Date!");
            return;
        }

        if (startTime.isEmpty()) {

            startTime = "00:00";
        }

        if (endTime.isEmpty()) {

            endTime = "23:59";
        }

        if (timeZone.isEmpty()) {

            timeZone = "00:00";
        }

        switch (plusMinus) {

            case "+":
                plusMinus = "%2B";
                break;

            case "-":
                plusMinus = "%2D";
                break;

        }

        switch (orderBy) {

            case "Descending Time":
                orderBy = "time";
                break;

            case "Ascending Time":
                orderBy = "time-asc";
                break;

            case "Descending Magnitude":
                orderBy = "magnitude";
                break;

            case "Ascending Magnitude":
                orderBy = "magnitude-asc";
                break;
        }

        url = url+"&starttime="+startDate+"T"+startTime+plusMinus+timeZone+"&endtime="+endDate+"T"
                        +endTime+plusMinus+timeZone+"&orderby="+orderBy+"&maxmagnitude="+maxMag
                        +"&minmagnitude="+minMag;

        Log.i("asas", url);

        Intent data = new Intent();
        data.setData(Uri.parse(url));
        setResult(RESULT_OK, data);
        finish();
    }
}
