package com.codelab.movies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.codelab.movies.R;
import com.codelab.movies.data.constant.Constants;
import com.codelab.movies.model.MovieModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context mContext;
    private final ArrayList<MovieModel> arrayList;
    private ItemClickListener itemClickListener;

    private int viewType;

    public MovieAdapter(Context context, ArrayList<MovieModel> arrayList, int viewType) {
        this.mContext = context;
        this.arrayList = arrayList;
        this.viewType = viewType;
    }

    class HorizontalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView ivCover;
        private final TextView tvTitle, tvReleaseDate, tvVoteCount;


        public HorizontalViewHolder(View itemView) {
            super(itemView);

            ivCover = itemView.findViewById(R.id.iv_cover);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvVoteCount = itemView.findViewById(R.id.tvVoteCount);
            tvReleaseDate = itemView.findViewById(R.id.tvReleaseDate);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }

    class VerticalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView ivCover;
        private final TextView tvTitle, tvReleaseDate, tvVoteCount;


        public VerticalViewHolder(View itemView) {
            super(itemView);

            ivCover = itemView.findViewById(R.id.iv_cover);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvVoteCount = itemView.findViewById(R.id.tvVoteCount);
            tvReleaseDate = itemView.findViewById(R.id.tvReleaseDate);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        if(viewType == Constants.VIEW_HORIZONTAL) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_cover, parent, false);
            viewHolder = new HorizontalViewHolder(view);
        } else if (viewType == Constants.VIEW_VERTICAL){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_cover, parent, false);
            viewHolder = new VerticalViewHolder(view);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {

        if (viewHolder.getItemViewType() == Constants.VIEW_HORIZONTAL) {
            HorizontalViewHolder holder = (HorizontalViewHolder) viewHolder;
            Picasso.get().
                    load(arrayList.get(position).getPoster()).
                    placeholder(R.color.gray).
                    into(holder.ivCover);

            holder.tvTitle.setText(arrayList.get(position).getTitle());
            holder.tvReleaseDate.setText(mContext.getResources().getString(R.string.release_date) + arrayList.get(position).getReleaseDate());
            holder.tvVoteCount.setText(mContext.getResources().getString(R.string.voted) + String.valueOf(arrayList.get(position).getVoteCount()));
        } else if (viewHolder.getItemViewType() == Constants.VIEW_VERTICAL) {
            VerticalViewHolder holder = (VerticalViewHolder) viewHolder;
            Picasso.get().
                    load(arrayList.get(position).getPoster()).
                    placeholder(R.color.gray).
                    into(holder.ivCover);

            holder.tvTitle.setText(arrayList.get(position).getTitle());
            holder.tvReleaseDate.setText(mContext.getResources().getString(R.string.release_date) + arrayList.get(position).getReleaseDate());
            holder.tvVoteCount.setText(mContext.getResources().getString(R.string.voted) + String.valueOf(arrayList.get(position).getVoteCount()));

        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return viewType;
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }


}
