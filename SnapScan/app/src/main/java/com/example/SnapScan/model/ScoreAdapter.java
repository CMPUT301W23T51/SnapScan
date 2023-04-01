package com.example.SnapScan.model;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.SnapScan.R;
import com.example.SnapScan.ui.profile.Score;

import java.util.ArrayList;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder> {

    private ArrayList<Score> mScores;
    private OnItemClickListener mOnClickListener;
    private String mClickedUsername;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnClickListener = listener;
    }

    public static class ScoreViewHolder extends RecyclerView.ViewHolder {

        public TextView mNameTextView;
        public TextView mPointsTextView;
        public TextView mRankTextView;

        public ScoreViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mNameTextView = itemView.findViewById(R.id.username_other);
            mPointsTextView = itemView.findViewById(R.id.points_other);
            mRankTextView = itemView.findViewById(R.id.rank_textview);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }

    }

    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        ScoreViewHolder viewHolder = new ScoreViewHolder(view, mOnClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {
        Score currentScore = mScores.get(position);
        holder.mNameTextView.setText(currentScore.getName());
        holder.mPointsTextView.setText(String.valueOf(currentScore.getPoints()));
        holder.mRankTextView.setText(String.valueOf(position + 1));
        if (currentScore.getName().equals(mClickedUsername)) {
            holder.itemView.setBackgroundColor(Color.LTGRAY);
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);
        }
    }


    @Override
    public int getItemCount() {
        return mScores.size();
    }

    public ScoreAdapter(ArrayList<Score> scores, OnItemClickListener listener) {
        mScores = scores;
        mOnClickListener = listener;
    }

    public void setClickedUsername(String username) {
        mClickedUsername = username;
    }

}