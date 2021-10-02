package com.codepath.apps.restclienttemplate.timeline;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.R.id;
import com.codepath.apps.restclienttemplate.compose_tweet.ComposeTweetActivity;
import com.codepath.apps.restclienttemplate.rest_api.TwitterApp;
import com.codepath.apps.restclienttemplate.rest_api.TwitterClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {
    private final String TAG = "TimelineActivity";
    private SwipeRefreshLayout swipeContainer;
    private TwitterClient client;
    List<Tweet> tweets;
    RecyclerView rvTweets;
    TweetsAdaptor adapter;
    ActivityResultLauncher<Intent> mComposeLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);

        // initialize the tweets container(array)
        tweets = new ArrayList<>();
        // need the swipe refresh container with a refresh listener
        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(() -> populateHomeTimeline(client));
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright);
        // Lookup the recyclerview in activity layout
        rvTweets = (RecyclerView) findViewById(R.id.rvTimeline);
        // Create adapter passing in the sample user data
        adapter = new TweetsAdaptor(tweets);
        // Attach the adapter to the recyclerview to populate items
        rvTweets.setAdapter(adapter);
        // Set layout manager to position the items
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(layoutManager);
        // Triggered only when new data needs to be appended to the list
        // Add whatever code is needed to append new items to the bottom of the list
        EndlessRecyclerViewScrollListener scrollListener;
        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadMoreData();
            }
        };
        // Adds the scroll listener to RecyclerView
        rvTweets.addOnScrollListener(scrollListener);
        // handler for getting result from activity
        mComposeLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();

                        assert data != null;
                        Parcelable tweet = data.getParcelableExtra(ComposeTweetActivity.KEY_TWEET);
                        tweets.add(0, Parcels.unwrap(tweet));
                        adapter.notifyItemChanged(0);
                        rvTweets.smoothScrollToPosition(0);
                    }
                }
        );

        client = TwitterApp.getRestClient(this);
        populateHomeTimeline(client);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == id.miCompose) {
            Intent i = new Intent(this, ComposeTweetActivity.class);
            mComposeLauncher.launch(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadMoreData() {
        client.getNextPageOfTweets(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                try {
                    adapter.addAll(Tweet.getTweets(json.jsonArray));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure: load more data", throwable);
            }
        }, tweets.get(tweets.size() - 1).getId());
    }

    private void populateHomeTimeline(TwitterClient client) {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                try {
                    // get clear the old tweets and get the new tweets
                    adapter.clear();
                    adapter.addAll(Tweet.getTweets(json.jsonArray));
                    // Now we call setRefreshing(false) to signal refresh has finished
                    swipeContainer.setRefreshing(false);
                } catch (JSONException e) {
                    Log.e(TAG, "JSON exception" + e.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "Failed API call");
            }
        });

    }
}