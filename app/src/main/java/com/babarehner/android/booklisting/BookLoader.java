package com.babarehner.android.booklisting;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by mike on 7/6/17.
 */

public class BookLoader extends AsyncTaskLoader<List<Book>> {

    private static final String LOG_TAG = BookLoader.class.getName();
    private String mUrl;

    /**
     * Constructs a new {@link BookLoader}
     * @param context of the actiivity
     * @param url to load date from
     */
    public BookLoader(Context context, String url) {
        super(context);
        Log.v(LOG_TAG, "Constructor, initLoader()");
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        Log.v(LOG_TAG, "onStartLoading method");
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {
        Log.v(LOG_TAG, "loadInBackground method");
        // Don't perform the request if there are no URLs, or the first URL is null.
        // What happened to <if (mUrl.length == 0 || ) ????
        if ( mUrl == null){
            return null;
        }

        // List<Earthquake> earthquakes = QueryUtils.fetchEarthquakeData(mUrl);
        return QueryUtils.fetchBookData(mUrl);
    }


}
