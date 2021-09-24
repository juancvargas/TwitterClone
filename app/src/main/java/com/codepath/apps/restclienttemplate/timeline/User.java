package com.codepath.apps.restclienttemplate.timeline;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    private String name;
    private String handle;
    private String profileImgUrl;

    public static User fromJson(JSONObject jsonObject) throws JSONException {
        User user = new User();
        user.name = jsonObject.getString("name");
        user.handle = jsonObject.getString("screen_name");
        user.profileImgUrl = jsonObject.getString("profile_image_url_https");

        return user;
    }

    public String getName() {
        return name;
    }

    public String getHandle() {
        return "@" + handle;
    }

    public String getProfileImgUrl() {
        return profileImgUrl;
    }
}
