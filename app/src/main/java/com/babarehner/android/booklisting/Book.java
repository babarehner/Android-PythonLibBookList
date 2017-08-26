package com.babarehner.android.booklisting;

/**
 * Created by mike on 6/19/17.
 */

public class Book {

    private String mTitle;
    private String mAuthors;
    private String mPublishedDate;
    private String mTextSnippet;
    private String mSmallThumbnail;
    private String mUrl;

    //TODO figure out how to handle situation if no smallThmbnail is provided
    private int mImageResourceId = NO_IMAGE_PROVIDED; //returns the image resource id
    private static final int NO_IMAGE_PROVIDED = -1;


    /**
     * Constructs a new {@link Book} object
     *
     * @param title the title of the book
     * @param authors authors of the book
     * @param publishedDate date book published
     * @param textSnippet text about the book
     * @param smallThumbnail url of a picture of the book
     * @param url web page of book
     */
    public Book(String title, String authors, String publishedDate,
                String smallThumbnail, String textSnippet, String url) {
        mTitle = title;
        mAuthors = authors;
        mPublishedDate = publishedDate;
        mSmallThumbnail = smallThumbnail;
        mTextSnippet = textSnippet;

        mUrl = url;
    }

    public String getTitle() { return mTitle; }

    public String getAuthors(){
        return mAuthors;
    }

    public String getPublishedDate() { return mPublishedDate; }

    public String getSmallThumbnail() { return mSmallThumbnail; }

    public String getTextSnippet() { return mTextSnippet; }
    /**
     * Get the ImageResourceId for the Image
     */
    public int getImageResourceId() { return mImageResourceId; }

    /**
     * Returns whether or not there is an image for this word
     * @return
     */

    public boolean hasImage() { return mImageResourceId != NO_IMAGE_PROVIDED; }

    public String getUrl() {return mUrl; }
}
