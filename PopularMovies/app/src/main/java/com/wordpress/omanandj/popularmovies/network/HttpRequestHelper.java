package com.wordpress.omanandj.popularmovies.network;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by ojha on 28/11/15.
 */
public class HttpRequestHelper
{
    private static final String LOG_TAG = HttpRequestHelper.class.getSimpleName();
    private static final String REQUEST_METHOD_GET = "GET";

    public static String executeSecureGet(Uri uri) throws IOException
    {
        URL url = new URL(uri.toString());

        HttpsURLConnection urlConnection = null;
        BufferedReader reader = null;
        StringBuffer buffer = new StringBuffer();

        try {
            // Connect to URL
            Log.v(LOG_TAG, "Connect to URL : " + url);

            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod(REQUEST_METHOD_GET);
            urlConnection.connect();

            // Read response
            final InputStream inputStream = urlConnection.getInputStream();

            if (inputStream != null) {
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
            }
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        return buffer.toString();
    }
}
