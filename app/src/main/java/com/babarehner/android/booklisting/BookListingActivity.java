package com.babarehner.android.booklisting;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BookListingActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<Book>>{

    public static final String LOG_TAG = BookListingActivity.class.getName();

    private static final String USGS_REQUEST_URL =
            "https://www.googleapis.com/books/v1/volumes?q=android&maxResults=1";

    private BookAdapter mAdapter;

    private static final int BOOK_LOADER_ID = 1;

    //Textviw that is diplyed when the list is empty
    private TextView mEmptyStateTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_listing_activity);

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // Create a new adapter that takes an empty list of earthquakes as input
        mAdapter = new BookAdapter(this, new ArrayList<Book>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected earthquake.
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                Book currentBook = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri earthquakeUri = Uri.parse(currentBook.getUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        // not sure where this should be place exactly
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        earthquakeListView.setEmptyView(mEmptyStateTextView);

        // Get a reference to the LoaderManager in order to interact with loaders
        LoaderManager loaderManager = getLoaderManager();
        // Initialize the loader. Pass in the int ID constant defined above and pass in null
        // for the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
        // because this activituy implments th LoaderCallbacks interfac
        loaderManager.initLoader(BOOK_LOADER_ID, null, this);

    }



    // Create a new loader
    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        Log.v(LOG_TAG, "onCreateLoader method");

        // Check for network connectivity
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE) ;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if ( isConnected ) {
            return new BookLoader(this, USGS_REQUEST_URL);
        } else {
            // hide the load indicator progress bar
            View loadIndicator = findViewById(R.id.load_progress);
            loadIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText("No internet Connection");
            return null;
        }


    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        Log.v(LOG_TAG, "onLoadFinished method");

        // hide the load indicator progress bar
        View loadIndicator = findViewById(R.id.load_progress);
        loadIndicator.setVisibility(View.GONE);

        // Put this here to keep it from flashing????
        mEmptyStateTextView.setText(R.string.no_books);

        // clear the adapter of previous earthquake data
        mAdapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        Log.v(LOG_TAG, "onLoaderReset method");
        // Loader reset, so we can clear out our existing data
        mAdapter.clear();
    }

}

