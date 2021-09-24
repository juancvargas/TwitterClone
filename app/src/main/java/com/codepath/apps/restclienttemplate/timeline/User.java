package com.codepath.apps.restclienttemplate.timeline;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    private String name;
    private String userName;
    private String avatarUrl;

    public static User fromJson(JSONObject jsonObject) throws JSONException {
        User user = new User();
        user.name = jsonObject.getString("name");
        user.userName = jsonObject.getString("screen_name");
        user.avatarUrl = jsonObject.getString("profile_image_url_https");

        return user;
    }

    public String getName() {
        return name;
    }

    public String getUserName() {
        return "@" + userName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }
}
