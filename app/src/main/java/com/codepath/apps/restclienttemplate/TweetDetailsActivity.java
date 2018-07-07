package com.codepath.apps.restclienttemplate;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class TweetDetailsActivity extends AppCompatActivity {

    Tweet tweet;
    Bitmap.Config config;
    AsyncHttpClient client;
    public static boolean isFavorited = false;
    // public static final String REST_URL = "https://api.twitter.com/1.1";
    User u;
    @BindView(R.id.tvUsername2) TextView tvUsername;
    @BindView(R.id.tvBody2) TextView tvBody;
    @BindView(R.id.ivProfile) ImageView profileImage;
    @BindView(R.id.tvCreatedAt) TextView dateCreated;
    @BindView(R.id.tvName) TextView tvName;
    @BindView(R.id.ibFavorite) ImageButton ibFavorite;
    @BindView(R.id.tvFaveCount) TextView tvFaveCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);
        ButterKnife.bind(this);
        client = new AsyncHttpClient();

        tweet = Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));
        config = Parcels.unwrap(getIntent().getParcelableExtra(Bitmap.Config.class.getSimpleName()));
        Log.d("TweetDetailsActivity", String.format("Showing details for '%s'", tweet.getBody()));
        u = tweet.getUser();

        // set values
        tvUsername.setText("@" + u.getScreenName());
        tvName.setText(tweet.user.name);
        tvBody.setText(tweet.getBody());
        tvFaveCount.setText("" + tweet.getFaveCount());
        dateCreated.setText(tweet.getRelativeTimeAgo());
        String profUrl = u.getProfileImageUrl();
        Glide.with(this)
                .load(profUrl)
                .into(profileImage);
    }

    // when favorites button is clicked
    public void onFavoritesClick (final View view) {
            // make api call for favoriting
        Log.i("onFavoritesClick", "check");
        TwitterClient tc = new TwitterClient(this);
        Log.i("onFavoritesClick", "check2");
        tc.favoriteTweet(isFavorited, tweet.getUid(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("TwitterClient", response.toString());
                Log.d("ComposeActivity1", String.format("%s", getIntent().getStringExtra("uid")));
                Tweet tw = new Tweet();
                try {
                    tw.fromJSON(response);
                    if (isFavorited) {
                        // if already favorited
                        view.setSelected(false);
                        if (tweet.getFaveCount() > 0) {
                            tweet.setFaveCount(tweet.getFaveCount() - 1);
                        }
                        Log.d("TweetDetailsActivity", String.format("%s", tweet.getFaveCount()));
                        tvFaveCount.setText(String.valueOf(tweet.getFaveCount()));
                        Log.d("TweetDetailsActivity", "changing favorited boolean to false");
                        isFavorited = false;
                    }
                    else {
                        view.setSelected(true);
                         tweet.setFaveCount(tweet.getFaveCount() + 1);
                          tvFaveCount.setText(String.valueOf(tweet.getFaveCount()));
                        Log.d("TweetDetailsActivity", "changing favorited boolean to true");
                        isFavorited = true;
                    }
                    Log.d("TwitterClient2", "got into try");

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
    // when retweet button is clicked
    public void onRetweetClick (final View view) {
        // make api call for retweeting
        TwitterClient tc = new TwitterClient(this);
        tc.retweet(tweet.getUid(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("TweetDetailsActivity", response.toString());
                Tweet tw = new Tweet();
                view.setSelected(true);
                try {
                    tw.fromJSON(response);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TweetDetailsActivity", responseString);
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("TweetDetailsActivity", errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TweetDetailsActivity", errorResponse.toString());
                throwable.printStackTrace();
            }

        });

    }
}
