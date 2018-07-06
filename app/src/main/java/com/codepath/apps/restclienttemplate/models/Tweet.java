package com.codepath.apps.restclienttemplate.models;

import android.text.format.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
@Parcel
public class Tweet {

    // define attributes
    public String body;
    public long uid;
    public User user;
    public String createdAt;
    public String embedUrl;
    public String faveCount;

    public Tweet() {}
    // deserialize the data
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        // extract values from JSON
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        tweet.faveCount = jsonObject.getString("favorite_count");
        JSONObject entities = jsonObject.getJSONObject("entities");
        try {
            JSONArray media = entities.getJSONArray("media");
                    if (media.length() != 0) {
                        tweet.embedUrl = media.getJSONObject(0).getString("media_url");
                    }
        } catch (Exception e){
            e.printStackTrace();
        }


        return tweet;
    }

    public String getRelativeTimeAgo () {
        // from gist.github.com/nesquena/f786232f5ef72f6e10a7
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(createdAt).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    public long getUid() {
        return uid;
    }

    public String getBody() {
        return body;
    }

    public User getUser() {
        return user;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getFaveCount() {
        return faveCount;
    }
}
