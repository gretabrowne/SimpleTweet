package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {
    AsyncHttpResponseHandler handler;
    EditText etTweet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        ButterKnife.bind(this);
        etTweet = (EditText) findViewById(R.id.etTweet);
        if (getIntent().getStringExtra("screenName") != null) {
            Log.d("ComposeActivity", "got into if statement");
            etTweet.setText("@" + getIntent().getStringExtra("screenName"));
        }

    }

    // todo-- clean this up
    // when compose button is hit
    public void onClick(View v) {
        TwitterClient tc = new TwitterClient(this);
        // EditText etTweet = findViewById(R.id.etTweet);
        String tweet = etTweet.getText().toString();
        if (getIntent().getStringExtra("screenName") != null) {
            // replying to someone else's tweet
            tc.replyToTweet(tweet, getIntent().getLongExtra("uid", 0), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("TwitterClient", response.toString());
                    // Log.d("ComposeActivity1", String.format("%s", getIntent().getStringExtra("uid")));
                    Tweet tw = new Tweet();
                    try {
                        tw.fromJSON(response);
                        Intent i = new Intent(ComposeActivity.this, TimelineActivity.class);
                        i.putExtra("tweet", Parcels.wrap(tw));
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
        else {
            // if new tweet (not a reply)

            tc.sendTweet(tweet, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d("TwitterClient", response.toString());
                        Tweet tw = new Tweet();
                        try {
                            tw.fromJSON(response);
                            Intent i = new Intent(ComposeActivity.this, TimelineActivity.class);
                            i.putExtra("tweet", Parcels.wrap(tw));
                            startActivity(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
//                Log.d("TwitterClient", response.toString());
//                // iterate through json array
//                // deserialize json object, convert to tweet model
//                // add tweet model to data source
//                // notify adapter that there's a new item
//                int i;
//                for (i = 0; i < response.length(); i++) {
//                    try {
//                        Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));
//                        tweets.add(tweet);
//                        tweetAdapter.notifyItemInserted(tweets.size() - 1);
//                    } catch (JSONException e){
//                        e.printStackTrace();
//                    }
//                }
//            }

    }

    public void onSubmit(View v) {
        // closes the activity and returns to first screen

        this.finish();
    }
}
