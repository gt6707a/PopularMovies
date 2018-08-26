package com.android.gt6707a.popularmovies.utilities;

import com.android.gt6707a.popularmovies.entities.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class TrailerJsonUtils {
    private final static String ID = "id";
    private final static String NAME = "name";
    private final static String KEY = "key";
    private final static String SITE = "site";

    public static List<Trailer> toTrailers(String json)
            throws JSONException {

        JSONObject jsonObject = new JSONObject(json);
        JSONArray results = jsonObject.getJSONArray("results");

        List<Trailer> trailers = new ArrayList<>();

        for (int i = 0; i < results.length(); i++) {
            JSONObject trailerJson = results.getJSONObject(i);
            String id = trailerJson.getString(ID);
            String name = trailerJson.getString(NAME);
            String site = trailerJson.getString(SITE);
            String key = trailerJson.getString(KEY);

            Trailer trailer = new Trailer()
                    .setId(id)
                    .setName(name)
                    .setSite(site)
                    .setKey(key);

            trailers.add(trailer);
        }

        return trailers;
    }
}
