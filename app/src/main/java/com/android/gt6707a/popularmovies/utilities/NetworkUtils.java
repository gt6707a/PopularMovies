/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.gt6707a.popularmovies.utilities;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.gt6707a.popularmovies.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the weather servers.
 */
public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String API_KEY_PARAM = "api_key";

    public static URL getUrl(Context context, String queryUri) {
        Uri uri = Uri.parse(queryUri).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, context.getString(R.string.API_KEY))
                .build();

        try {
            URL url = new URL(uri.toString());
            Log.v(TAG, "URL: " + url);
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response, null if no response
     * @throws IOException Related to network and stream reading
     */
    public static String fetch(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        // Ideally, we would want to handle specific exceptions with specific catches and error
        // messages to give more details to the user. For example, device has no connection vs
        // target server not responding are different reasons why there's no response.
        if (!isOnline()) {
            return "";
        }

        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } catch (Exception e) {
          e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return "";
    }

    private static boolean isOnline() {
        try {
            Socket sock = new Socket();
            sock.connect(new InetSocketAddress("8.8.8.8", 53), 1500);
            sock.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}