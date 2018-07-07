package com.codepath.apps.restclienttemplate;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class FollowersActivity extends AppCompatActivity {
    String screenName;
    Bitmap.Config config;
    ArrayList<String> followers;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;
    AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);
        followers = new ArrayList<>();
        lvItems = findViewById(R.id.lvFollowers);

        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, followers);
        lvItems.setAdapter(itemsAdapter);

        client = new AsyncHttpClient();
        screenName = getIntent().getStringExtra("screenName");
        TextView username = (TextView) findViewById(R.id.tvUserFollowers);
        username.setText(screenName + "'s Followers");
        Log.d("onCreate", String.format("%s", screenName));
        TwitterClient tc = new TwitterClient(this);
        Log.i("onFavoritesClick", "check2");
        tc.getFollowers(screenName, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // format data into list view, use to-do app as guide
                Log.d("onSuccess", "reached here");
                try {
                    JSONArray users = response.getJSONArray("users");
                    for (int i = 0; i < users.length(); i++) {
                        User user = User.fromJSON(users.getJSONObject(i));
                        itemsAdapter.add(user.getScreenName());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("TwitterClient2", "got into try");
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    // format data into list view, use to-do app as guide
                Log.d("onSuccess", "reached here");
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            User user = User.fromJSON(response.getJSONObject(i));
                            itemsAdapter.add(user.getScreenName());
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }

                    Log.d("TwitterClient2", "got into try");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString);
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }

        });
    }
}


