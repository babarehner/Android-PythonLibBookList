package com.babarehner.android.booklisting;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.babarehner.android.booklisting.BookListingActivity.LOG_TAG;


/**
 * Created by mike on 6/19/17.
 */

/**
 * Helper methods related to requesting and receiving book data from
 * Google book API.
 */
public final class QueryUtils {

    public static List<Book> fetchBookData(String urlRequest) {
        Log.v(LOG_TAG, "in fetchBookData method");

        // make earthquake sleep fot 2 seconds to see if it will show progress bar
        // for testing purposes
        /**
         try{
         Thread.sleep(2000);
         } catch (InterruptedException e) {
         e.printStackTrace();
         }
         **/

        // create URL object
        URL url = createUrl(urlRequest);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem with http request", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        List<Book> books = extractFeatureFromJson(jsonResponse);


        return books;
    }


    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }


    // Returns new URL object from the given string URL.
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }


    // Make an HTTP request to the given URL and return a String as the response.
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();

            // If the request was successful (status 200) read input stream
            // and parse response
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "urlConnection Error");
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }


    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }



    /**
     * Return a list of {@link Book} objects that has been built up from
     * parsing a JSON response.
     */
    public static List<Book> extractFeatureFromJson(String bookJSON) {
        // if no JSON returned get out
        if (TextUtils.isEmpty(bookJSON)) { return null; }


        // Create an empty ArrayList that we can start adding books to
        List<Book> books = new ArrayList<>();


        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // create JSON Object from the JSON repsonse stroing
            JSONObject baseJsonResponse = new JSONObject(bookJSON);

            // Extract the JSONArray associated with the key called "features",
            // which represents a list of features from the Google Books API.
            JSONArray bookArray = baseJsonResponse.getJSONArray("items");

            // For each book in the bookArray, create an {@link Book} object
            for (int i=0; i < bookArray.length(); i++){
                // Get a single book at position i within the list of books
                JSONObject currentBook = bookArray.getJSONObject(i);
                // For a given earthquake, extract the JSONObject associated with the
                // key called "properties", which represents a list of all properties
                // for that earthquake.
                JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");
                // Extract the value for the key called "title"
                // TODO also extract subtitle and add to title
                String title = volumeInfo.getString("title");
                // Extract the value for the key called "authors"
                String a =  volumeInfo.getString("authors");
                // Extract the value for the key called "publishedDate"
                String authors = a.replaceAll("[^\\w\\s]", ""); //strip  everything that is not a word character (a-z in any case, 0-9 or _) or whitespace.
                String publishedDate = volumeInfo.getString("publishedDate");
                // Extract a url value for a small thumbnail
                JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                String smallThumbnail = imageLinks.getString("smallThumbnail");
                // there may not be a searchInfo for all books so have to deal with it
                JSONObject searchInfo = currentBook.getJSONObject("searchInfo");
                String textSnippet = searchInfo.getString("textSnippet");
                // not sure what the difference is between previewLink, infoLink &
                // canonicalVolumeLink
                String url = volumeInfo.getString("previewLink");

                // TODO work out what to do with smallThumbnail image
                if (smallThumbnail.length() == 0){
                    smallThumbnail = "";
                }

                books.add(new Book(title, authors, publishedDate, smallThumbnail,
                        textSnippet, url));

                System.out.println(title + authors);
                Log.v(LOG_TAG, (title + authors));
            }


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the book JSON results", e);
        }

        // Return the list of books
        return books;
    }

}
