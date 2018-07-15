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

import com.android.gt6707a.popularmovies.entities.Trailer;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link TrailersAdapter} exposes a list of trailers
 * to a {@link RecyclerView}.
 */
class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.ViewHolder> {
    private final List<Trailer> mTrailers;
    private final Context mContext;

    final private TrailersAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface TrailersAdapterOnClickHandler {
        void onClick(Trailer trailer);
    }

    /**
     * A ViewHolder is a required part of the pattern for RecyclerViews. It mostly behaves as
     * a cache of the child views for an item. It's also a convenient place to set an
     * OnClickListener, since it has access to the adapter and the views.
     */
    class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        final TextView mTrailerNameTextView;

        ViewHolder(View view) {
            super(view);
            mTrailerNameTextView = view.findViewById(R.id.tv_trailer_name);
            view.setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click. We fetch the date that has been
         * selected, and then call the onClick handler registered with this adapter, passing that
         * date.
         *
         * @param v the View that was clicked
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Trailer trailer = mTrailers.get(adapterPosition);
            mClickHandler.onClick(trailer);
        }
    }

    public TrailersAdapter(@NonNull Context context, TrailersAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
        mTrailers = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.trailer_list_item, viewGroup, false);
        view.setFocusable(true);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailersAdapter.ViewHolder holder, int position) {
        Trailer trailer = mTrailers.get(position);

        holder.mTrailerNameTextView.setText(trailer.getName());
    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }

    void updateTrailers(List<Trailer> trailers) {
        mTrailers.clear();
        mTrailers.addAll(trailers);
        notifyDataSetChanged();
    }
}