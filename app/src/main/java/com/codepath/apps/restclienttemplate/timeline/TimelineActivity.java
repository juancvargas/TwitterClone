package com.codepath.apps.restclienttemplate.timeline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.restapi.TwitterApp;
import com.codepath.apps.restclienttemplate.restapi.TwitterClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.facebook.stetho.server.http.LightHttpServer;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {
    private final String TAG = "TimelineActivity";
    private TwitterClient client;
    List<Tweet> tweets;
    RecyclerView rvTweets;
    TweetsAdaptor adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);

        // initialize the tweets container(array)
        tweets = new ArrayList<>();

        // Lookup the recyclerview in activity layout
        rvTweets = (RecyclerView) findViewById(R.id.rvTimeline);
        // Create adapter passing in the sample user data
        adapter = new TweetsAdaptor(tweets);
        // Attach the adapter to the recyclerview to populate items
        rvTweets.setAdapter(adapter);
        // Set layout manager to position the items
        rvTweets.setLayoutManager(new LinearLayoutManager(this));

        client = TwitterApp.getRestClient(this);
        populateHomeTimeline(client);
    }

    private void populateHomeTimeline(TwitterClient client) {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                try {
                    // get the tweets
                    tweets.addAll(Tweet.getTweets(json.jsonArray));
                    adapter.notifyDataSetChanged();
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