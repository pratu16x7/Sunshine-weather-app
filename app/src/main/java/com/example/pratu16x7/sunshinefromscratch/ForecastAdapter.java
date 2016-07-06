package com.example.pratu16x7.sunshinefromscratch;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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


    /*
        A cache for the children views of a forecast list item
     */
    // Tell this to find them and hold them
    public static class ViewHolder{
        private final ImageView iconView;
        private final TextView dateView;
        private final TextView descriptionView;
        private final TextView highTempView;
        private final TextView lowTempView;

        public ViewHolder(View view){
            iconView = (ImageView) view.findViewById(R.id.list_item_icon);
            dateView = (TextView)view.findViewById(R.id.list_item_date_textview);
            descriptionView = (TextView)view.findViewById(R.id.list_item_forecast_textview);
            highTempView = (TextView)view.findViewById(R.id.list_item_high_textview);
            lowTempView = (TextView)view.findViewById(R.id.list_item_low_textview);
        }

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = (viewType == VIEW_TYPE_TODAY) ? R.layout.list_item_forecast_today
                : R.layout.list_item_forecast;
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);

        // First make the viewHolder find the smaller views and hold them
        ViewHolder viewHolder = new ViewHolder(view);
        // Then set it as a tag for the main view, so its has them as a property for all to use
        view.setTag(viewHolder);

        return view;
    }

    /*
        This is where we fill-in the views with the contents of the cursor.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Since getTag returns an object, we have to cast it to our (known to be) ViewHolder type
        ViewHolder viewHolder = (ViewHolder)view.getTag();

//        ImageView image = (ImageView) view.findViewById(R.id.image);
//        if (image == null) Log.v("Waaaaa", "null "+ R.id.image);
//        else Log.v("YAY!", "worked");
//        image.setImageResource(R.mipmap.ic_launcher);
        viewHolder.iconView.setImageResource(R.mipmap.ic_launcher);


        Boolean isMetric = Utility.isMetric(mContext);

        String highText = Utility.formatTemperature(cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP),
                isMetric) + "°";
        viewHolder.highTempView.setText(highText);

        String lowText = Utility.formatTemperature(cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP),
                isMetric) + "°";
        viewHolder.lowTempView.setText(lowText);

        viewHolder.descriptionView.setText(cursor.getString(COL_WEATHER_DESC));

        String date = Utility.getFriendlyDayString(context, cursor.getLong(COL_WEATHER_DATE));
        viewHolder.dateView.setText(date);
    }
}