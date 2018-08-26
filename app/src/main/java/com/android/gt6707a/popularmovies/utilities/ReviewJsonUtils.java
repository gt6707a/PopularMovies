package com.android.gt6707a.popularmovies.utilities;

import com.android.gt6707a.popularmovies.entities.Review;
import com.android.gt6707a.popularmovies.entities.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class ReviewJsonUtils {
    private final static String ID = "id";
    private final static String AUTHOR = "author";
    private final static String CONTENT = "content";

    public static List<Review> toReviews(String json)
            throws JSONException {

        JSONObject jsonObject = new JSONObject(json);
        JSONArray results = jsonObject.getJSONArray("results");

        List<Review> reviews = new ArrayList<>();

        for (int i = 0; i < results.length(); i++) {
            JSONObject reviewJson = results.getJSONObject(i);
            String id = reviewJson.getString(ID);
            String author = reviewJson.getString(AUTHOR);
            String content = reviewJson.getString(CONTENT);

            reviews.add(new Review()
                    .setId(id)
                    .setAuthor(author)
                    .setContent(content)
            );
        }

        return reviews;
    }
}
