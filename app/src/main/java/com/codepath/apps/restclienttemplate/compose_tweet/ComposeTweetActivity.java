package com.codepath.apps.restclienttemplate.compose_tweet;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.rest_api.TwitterApp;
import com.codepath.apps.restclienttemplate.rest_api.TwitterClient;
import com.codepath.apps.restclienttemplate.timeline.TimelineActivity;
import com.codepath.apps.restclienttemplate.timeline.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;


import okhttp3.Headers;

public class ComposeTweetActivity extends AppCompatActivity {
    private final String TAG = "ComposeTweetActivity";
    private final int CHARACTER_LIMIT = 140;
    public static final String KEY_TWEET = "tweet";
    EditText etComposeTweet;
    Button btnTweet;
    TwitterClient client;
    TextView tvCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_tweet);

        client = TwitterApp.getRestClient(this);

        tvCounter = findViewById(R.id.tvCounter);
        etComposeTweet = findViewById(R.id.etCompose);
        etComposeTweet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String strCount = etComposeTweet.getText().length() + "/140";
                tvCounter.setText(strCount);
            }
        });

        btnTweet = findViewById(R.id.btnTweet);
        btnTweet.setOnClickListener(view -> {
            String tweetContent =  etComposeTweet.getText().toString();
            if (tweetContent.isEmpty()) {
                return;
            }

            if (tweetContent.length() > CHARACTER_LIMIT) {
                return;
            }

            client.publishTweet(tweetContent, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    Intent data = new Intent(ComposeTweetActivity.this, TimelineActivity.class);

                    try {
                        Tweet tweet = Tweet.fromJson(json.jsonObject);
                        data.putExtra(KEY_TWEET, Parcels.wrap(tweet));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    setResult(Activity.RESULT_OK, data);

                    // close the screen and go back to MainActivity
                    finish();
                }

                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    Log.e(TAG, "onFailure publish tweet error", throwable);
                }
            });
        });
    }
}