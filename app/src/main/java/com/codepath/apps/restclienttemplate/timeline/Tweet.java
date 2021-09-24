package com.codepath.apps.restclienttemplate.timeline;

import com.codepath.apps.restclienttemplate.utils.TimeFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Tweet {
    private final String MIDDLE_DOT = "\u2022";
    private String body;
    private String createdAt;
    private User user;
    private Long id;

    private static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = jsonObject.getString("text");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        tweet.id = jsonObject.getLong("id");

        return tweet;
    }

    public static List<Tweet> getTweets(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++)
            tweets.add(fromJson(jsonArray.getJSONObject(i)));

        return tweets;
    }

    public String getFormattedTimestamp() {
        return MIDDLE_DOT + " " + TimeFormatter.getTimeDifference(createdAt);
    }

    public String getBody() {
        return body;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }

    public Long getId() {
        return id;
    }
}
