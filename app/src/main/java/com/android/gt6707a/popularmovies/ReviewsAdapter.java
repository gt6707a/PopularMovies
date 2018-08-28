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
package com.android.gt6707a.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.gt6707a.popularmovies.entities.Review;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link ReviewsAdapter} exposes a list of trailers
 * to a {@link RecyclerView}.
 */
class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {
    private final List<Review> mReviews;
    private final Context mContext;

    /**
     * A ViewHolder is a required part of the pattern for RecyclerViews. It mostly behaves as
     * a cache of the child views for an item. It's also a convenient place to set an
     * OnClickListener, since it has access to the adapter and the views.
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        final TextView mAuthorTextView;
        final TextView mContentTextView;

        ViewHolder(View view) {
            super(view);
            mAuthorTextView = view.findViewById(R.id.tv_author);
            mContentTextView = view.findViewById(R.id.tv_content);
        }
    }

    public ReviewsAdapter(@NonNull Context context) {
        mContext = context;
        mReviews = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.review_list_item, viewGroup, false);
        view.setFocusable(true);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsAdapter.ViewHolder holder, int position) {
        Review review = mReviews.get(position);

        holder.mAuthorTextView.setText(review.getAuthor());
        holder.mContentTextView.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    void updateReviews(List<Review> reviews) {
        mReviews.clear();
        mReviews.addAll(reviews);
        notifyDataSetChanged();
    }
}