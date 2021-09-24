package com.codepath.apps.restclienttemplate.timeline;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;

import java.util.List;

public class TweetsAdaptor extends RecyclerView.Adapter<TweetsAdaptor.ViewHolder>{
    private final String TAG = "TweetsAdaptor";
    private List<Tweet> mTweets;

    public TweetsAdaptor(List<Tweet> tweets) {
        mTweets = tweets;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tweet_item, parent, false);
        return new ViewHolder(v);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(holder, mTweets.get(position), holder.itemView.getContext());
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    // Provide a direct reference to each of the views within a data item
    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProfileImage;
        TextView tvUsername;
        TextView tvBody;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvUsername = itemView.findViewById(R.id.tvUserName);
        }

        public void bind(ViewHolder holder, Tweet tweet, Context context) {
            holder.tvUsername.setText(tweet.getUser().getName());
            holder.tvBody.setText(tweet.getBody());

            Glide.with(context)
                    .load(tweet.getUser().getProfileImgUrl())
                    .circleCrop()
                    .into(ivProfileImage);
        }
    }
}
