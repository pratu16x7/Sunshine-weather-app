package com.example.pratu16x7.sunshinefromscratch;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static com.example.pratu16x7.sunshinefromscratch.ForecastFragment.COL_WEATHER_DATE;
import static com.example.pratu16x7.sunshinefromscratch.ForecastFragment.COL_WEATHER_DESC;

/**
 * {@link ForecastAdapter} exposes a list of weather forecasts
 * from a {@link android.database.Cursor} to a {@link android.widget.ListView}.
 */
public class ForecastAdapter extends CursorAdapter {

    private final int VIEW_TYPE_COUNT = 2;
    private final int VIEW_TYPE_TODAY = 0;
    private final int VIEW_TYPE_FUTURE_DAY = 1;

    public ForecastAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE_DAY;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = (viewType == VIEW_TYPE_TODAY) ? R.layout.list_item_forecast_today
                : R.layout.list_item_forecast;
        return LayoutInflater.from(context).inflate(layoutId, parent, false);
    }

    /*
        This is where we fill-in the views with the contents of the cursor.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // our view is pretty simple here --- just a text view
        // we'll keep the UI functional with a simple (and slow!) binding.

//        TextView tv = (TextView)view;
//        tv.setText(convertCursorRowToUXFormat(cursor));
        Boolean isMetric = Utility.isMetric(mContext);

        TextView highTv = (TextView)view.findViewById(R.id.list_item_high_textview);
        String highText = Utility.formatTemperature(cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP),
                isMetric) + "°";
        highTv.setText(highText);

        TextView lowTv = (TextView)view.findViewById(R.id.list_item_low_textview);
        String lowText = Utility.formatTemperature(cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP),
                isMetric) + "°";
        lowTv.setText(lowText);

        ((TextView)view.findViewById(R.id.list_item_forecast_textview)).setText(cursor.getString(COL_WEATHER_DESC));

        TextView dateTv = (TextView)view.findViewById(R.id.list_item_date_textview);
        String date = Utility.getFriendlyDayString(context, cursor.getLong(COL_WEATHER_DATE));
        dateTv.setText(date);

    }
}