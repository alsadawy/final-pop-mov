package com.example.user.popmov.utiles;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.user.popmov.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder> {
    private ArrayList<String> a;
    final private OnImgClicked clicked;

    public interface OnImgClicked {
        void onClicklistner(int position);
    }

    public MovieAdapter(ArrayList<String> arrayList, OnImgClicked listner) {
        a = arrayList;
        clicked = listner;
    }


    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.image_item, parent, false);
        return new MovieHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieHolder holder, int position) {
        Picasso.get().load(Uri.parse(a.get(position))).into(holder.imageView);
    }

    @Override
    public int getItemCount() {

        return a.size();
    }

    class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.rv_image)
        ImageView imageView;

        public MovieHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            clicked.onClicklistner(position);
        }
    }


}
