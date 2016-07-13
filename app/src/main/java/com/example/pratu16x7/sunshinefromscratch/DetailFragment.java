package com.example.pratu16x7.sunshinefromscratch;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pratu16x7.sunshinefromscratch.data.WeatherContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();

    private ShareActionProvider mShareActionProvider;
    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";
    private String mForecastStr;
    public static final int DetailLoaderID = 1;
    static final String DETAIL_URI = "URI";
    private Uri mUri;
    ImageView mIconIv;
    TextView mDayTv, mMonthDateTv, mMaxTempTv, mMinTempTv, mDescTv, mHumidityTv, mWindTv, mPressureTv;

    private static final String[] DETAIL_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
            WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
            WeatherContract.WeatherEntry.COLUMN_DEGREES,
            WeatherContract.WeatherEntry.COLUMN_PRESSURE,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID

    };

    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
    // must change.
    static final int COL_WEATHER_ID = 0;
    static final int COL_WEATHER_DATE = 1;
    static final int COL_WEATHER_DESC = 2;
    static final int COL_WEATHER_MAX_TEMP = 3;
    static final int COL_WEATHER_MIN_TEMP = 4;
    static final int COL_HUMIDITY = 5;
    static final int COL_WIND_SPEED = 6;
    static final int COL_DEGREES = 7;
    static final int COL_PRESSURE = 8;
    static final int COL_WEATHER_FORECAST_ID = 9;

    public DetailFragment() {
        setHasOptionsMenu(true);
    }


    void onLocationChanged(String newLocation ) {
        // replace the uri, since the location has changed
        Uri uri = mUri;
        if (null != uri) {
            long date = WeatherContract.WeatherEntry.getDateFromUri(uri);
            Uri updatedUri = WeatherContract.WeatherEntry.buildWeatherLocationWithDate(newLocation, date);
            mUri = updatedUri;
            getLoaderManager().restartLoader(DetailLoaderID, null, this);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(DetailLoaderID, null, this);
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (mUri != null) {
            return new CursorLoader(getActivity(), mUri, DETAIL_COLUMNS, null, null, null);
        }
        return  null;
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (!cursor.moveToFirst()) { return; }

        long dateInMillis = cursor.getLong(COL_WEATHER_DATE);
        String date = Utility.formatDate(dateInMillis);
        String day = Utility.getDayName(getActivity(), dateInMillis);
        String monthDate = Utility.getFormattedMonthDay(getActivity(), dateInMillis);

        Boolean isMetric = Utility.isMetric(getActivity());
        String maxTemp = Utility.formatTemperature(getActivity(), cursor.getDouble(COL_WEATHER_MAX_TEMP), isMetric);
        String minTemp = Utility.formatTemperature(getActivity(), cursor.getDouble(COL_WEATHER_MIN_TEMP), isMetric);

        String shortDesc = cursor.getString(COL_WEATHER_DESC);

        String humidity = Utility.formatHumidity(getActivity(), cursor.getDouble(COL_HUMIDITY));
        String wind = Utility.formatWind(getActivity(), cursor.getDouble(COL_WIND_SPEED), cursor.getDouble(COL_DEGREES));
        String pressure = Utility.formatPressure(getActivity(), cursor.getDouble(COL_PRESSURE));

        int weatherId = cursor.getInt(COL_WEATHER_FORECAST_ID);
        mIconIv.setImageResource(Utility.getArtResourceForWeatherCondition(weatherId));

        mDayTv.setText(day);
        mMonthDateTv.setText(monthDate);
        mMaxTempTv.setText(maxTemp);
        mMinTempTv.setText(minTemp);
        mDescTv.setText(shortDesc);
        mHumidityTv.setText(humidity);
        mWindTv.setText(wind);
        mPressureTv.setText(pressure);

        mForecastStr = String.format("%s - %s - %s/%s", date,shortDesc, maxTemp, minTemp);
        //((TextView)getView().findViewById(R.id.details)).setText(mForecastStr);

        // If onCreateOptionsMenu has already happened, we need to update the share intent now
        if (mShareActionProvider != null){
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }

    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle args = getArguments();
        if(args != null){
            mUri = args.getParcelable(DETAIL_URI);
        }

        if (container != null) {
            container.removeAllViews();
        }

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        mIconIv = (ImageView) rootView.findViewById(R.id.detail_icon);

        mDayTv = (TextView) rootView.findViewById(R.id.detail_day);
        mMonthDateTv = (TextView) rootView.findViewById(R.id.detail_month_date);
        mMaxTempTv = (TextView) rootView.findViewById(R.id.detail_high_temp);
        mMinTempTv = (TextView) rootView.findViewById(R.id.detail_low_temp);
        mDescTv = (TextView) rootView.findViewById(R.id.detail_forecast);
        mHumidityTv = (TextView) rootView.findViewById(R.id.detail_humidity);
        mWindTv = (TextView) rootView.findViewById(R.id.detail_wind);
        mPressureTv = (TextView) rootView.findViewById(R.id.detail_pressure);

        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.

        //This method is called twice
        Log.v("CALLED", "menucreate");
        //This doesn't work:
        //if(menu.findItem(R.menu.detailfragment)==null)
        //menu.removeItem(R.menu.detailfragment);

        //Better to clear and do all of the inflating here, leave nothing to DetailActivity
        menu.clear();
        inflater.inflate(R.menu.menu_detail, menu);
        inflater.inflate(R.menu.detailfragment, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);
        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // Attach an intent to this ShareActionProvider.  You can update this at any time,
        // like when the user selects a new piece of data they might like to share.

        // If onLoadFinished happens before this, set share intent now
        if (mForecastStr != null ) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }
    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                mForecastStr + FORECAST_SHARE_HASHTAG);
        return shareIntent;
    }
}
