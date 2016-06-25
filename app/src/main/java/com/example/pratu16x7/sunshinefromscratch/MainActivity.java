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

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ForecastFragment())
                    .commit();
        }
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
