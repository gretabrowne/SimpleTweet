package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {
    private List<Tweet> mTweets;
    Context context;
    Bitmap.Config config;
    public static boolean isFavorited = false;

    // pass in tweets array in constructor
    public TweetAdapter(List<Tweet> tweets) {
        mTweets = tweets;
    }
    // for each row, inflate layout and cache refs

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }

    // bind vals based on position

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        // get data based on position
        final Tweet tweet = mTweets.get(position);

        // populate views
        holder.tvUserName.setText(tweet.user.name);
        holder.tvBody.setText(tweet.body);
        holder.tvDateTime.setText(tweet.getRelativeTimeAgo());
        holder.faveCount.setText(String.valueOf(tweet.getFaveCount()));

        GlideApp.with(context)
                .load(tweet.user.profileImageUrl)
                .into(holder.ivProfileImage);

        Glide.with(context).load(tweet.embedUrl).into(holder.ivEmbedImage);

        holder.ibReply.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // get tweet at position
                Intent intent = new Intent(context, ComposeActivity.class);
                intent.putExtra("screenName", tweet.user.screenName);
                intent.putExtra("uid", tweet.uid);
                Log.d("TweetAdapter", String.format("%s", tweet.uid));
                // show the activity
                context.startActivity(intent);
            }
        });

        // how to do this
        holder.follows.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v) {
                    Intent intent = new Intent(context, FollowersActivity.class);
                    intent.putExtra("screenName", tweet.user.screenName);
                    context.startActivity(intent);
                }
        });

        // how to do this
        holder.friends.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v) {
                Intent intent = new Intent(context, FriendsActivity.class);
                intent.putExtra("screenName", tweet.user.screenName);
                context.startActivity(intent);
            }
        });

        holder.ivFavorite.setOnClickListener(new View.OnClickListener() {
            public void onClick (final View v) {
                if (!isFavorited) {
                    v.setSelected(true);
                    tweet.setFaveCount(tweet.getFaveCount() + 1);
                    isFavorited = true;
                }

                else {
                    v.setSelected(false);
                    tweet.setFaveCount(tweet.getFaveCount() - 1);
                    isFavorited = false;
                }

            }
        });

        holder.ivRetweet.setOnClickListener(new View.OnClickListener() {
            public void onClick (final View v) {
                v.setSelected(true);
            }
        });
    }



    // create viewholder class

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.ivProfileImage) ImageView ivProfileImage;
        @BindView(R.id.tvUserName) TextView tvUserName;
        @BindView(R.id.tvBody) TextView tvBody;
        @BindView(R.id.tvDateTime) TextView tvDateTime;
        @BindView(R.id.ibReply) ImageButton ibReply;
        @BindView(R.id.ivEmbeddedImage) ImageView ivEmbedImage;
        @BindView(R.id.tvFaveCount) TextView faveCount;
        @BindView(R.id.button2) Button friends;
        @BindView(R.id.button3) Button follows;
        @BindView(R.id.ivFavorite2) ImageView ivFavorite;
        @BindView(R.id.ivRetweet2) ImageView ivRetweet;

        public ViewHolder (View itemView) {
            super (itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick (View view) {
            int position = getAdapterPosition();
            // ensure valid position
            if (position != RecyclerView.NO_POSITION) {
                // get tweet at position
                Tweet tweet = mTweets.get(position);
                Intent intent = new Intent(context, TweetDetailsActivity.class);
                intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
                // bitmap config problem?
                intent.putExtra(Bitmap.Config.class.getSimpleName(), Parcels.wrap(config));
                // show the activity
                context.startActivity(intent);
            }
        }

    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<Tweet> list) {
        mTweets.addAll(list);
        notifyDataSetChanged();
    }

}
