package com.example.user.popmov.utiles;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.user.popmov.R;

import org.json.JSONArray;
import org.json.JSONException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewHolder> {

    private JSONArray reviewsArray;
    public ReviewsAdapter(JSONArray reviews) {
        this.reviewsArray = reviews;
    }

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.review_item, parent , false);
        return new ReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {

        try {
            holder.author.setText(JsonUtils.getAuthor(reviewsArray,position));
            holder.review.setText(JsonUtils.getReview(reviewsArray,position));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return reviewsArray.length();
    }

    class ReviewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.review_content)
        TextView review ;
        @BindView(R.id.review_author)
        TextView author ;
        public ReviewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
