package com.babarehner.android.booklisting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by mike on 6/20/17.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    private final static String LOCATION_SEPARATOR = " of ";


    //TODO Add small thmbnail to list book list item
    public BookAdapter(Context context, List<Book> books) {
            super(context, 0, books);
        }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            // View is in book_list_item.xml
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.book_list_item, parent, false);
        }

        Book currentBook = getItem(position);

        TextView titleView = (TextView) listItemView.findViewById(R.id.title);
        titleView.setText(currentBook.getTitle() + " - " + currentBook.getPublishedDate());

        TextView authorView = (TextView) listItemView.findViewById(R.id.authors);
        authorView.setText(currentBook.getAuthors());

        //TextView publishDateView = (TextView) listItemView.findViewById(R.id.publishDate);
        //publishDateView.setText("Published Date: " + currentBook.getPublishedDate());

        TextView snippetView = (TextView) listItemView.findViewById(R.id.textSnippet);
        snippetView.setText(currentBook.getTextSnippet());

        return listItemView;
    }

}
