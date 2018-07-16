package com.example.user.popmov.utiles;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.user.popmov.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder>{

    private final String TRAILER = "Trailer ";

   public interface TrailerClickHandler {
        void onClick(int position);
    }

    private ArrayList<String> trailers;
    private TrailerClickHandler clickHandler;
    public TrailerAdapter(ArrayList<String> trailers , TrailerClickHandler handler) {
        this.trailers = trailers;
        this.clickHandler = handler;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
       View view = inflater.inflate(R.layout.trailer_item,parent,false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {

            holder.trailerNumber.setText(TRAILER + String.valueOf(position + 1));
    }


    @Override
    public int getItemCount() {
       return trailers.size();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener
    {

       @BindView(R.id.trailer_play_button)
       ImageView playButton;
        @BindView(R.id.trailer_number)
        TextView trailerNumber;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickHandler.onClick(getAdapterPosition());
        }
    }
}
