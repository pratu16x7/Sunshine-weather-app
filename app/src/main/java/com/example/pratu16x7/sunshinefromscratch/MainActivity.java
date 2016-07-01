package com.example.pratu16x7.sunshinefromscratch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import static com.example.pratu16x7.sunshinefromscratch.Utility.getPreferredLocation;

public class MainActivity extends AppCompatActivity {

    String mLocation;
    private static final String FORECASTFRAGMENT_TAG = "FFTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ForecastFragment(), FORECASTFRAGMENT_TAG)
                    .commit();
        }
        mLocation = getPreferredLocation(this);
        Log.v("WEEEEEEE", "onCreate");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        if (id == R.id.action_view_map) {
            openPreferredLocationOnMap();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openPreferredLocationOnMap(){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String code = sharedPref.getString(getString(R.string.pref_location_key) ,getString(R.string.pref_location_default) );
        final String BASE = "geo:0,0?";
        final String QUERY_PARAM = "q";
        Uri geoLocation = Uri.parse(BASE).buildUpon()
                .appendQueryParameter(QUERY_PARAM, code).build();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v("WEEEEEEE", "onPause");
    }

    @Override
    protected void onResume() {
        super.onStart();
        String location = Utility.getPreferredLocation(this);
        if (location != null && !location.equals(mLocation)){
            ForecastFragment ff = (ForecastFragment)getSupportFragmentManager()
                    .findFragmentByTag(FORECASTFRAGMENT_TAG);
            if (ff != null){ ff.onLocationChanged(); }
            mLocation = location;
        }
        Log.v("WEEEEEEE", "onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v("WEEEEEEE", "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v("WEEEEEEE", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("WEEEEEEE", "onDestroy");
    }

    /**
     * A placeholder fragment containing a simple view.
     */


}
