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
/**

 A custom adapter for displaying a list of Scores in a RecyclerView.

 Uses a ScoreViewHolder to display the individual items in the list.
 */

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder> {

    private ArrayList<Score> mScores;
    private OnItemClickListener mOnClickListener;
    private String mClickedUsername;
    /**

     Interface for listening to click events on items in the list.
     */

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    /**

     Sets the click listener for items in the list.
     @param listener The listener to set.
     */

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnClickListener = listener;
    }
    /**

     ViewHolder class for displaying individual items in the list.
     */

    public static class ScoreViewHolder extends RecyclerView.ViewHolder {

        public TextView mNameTextView;
        public TextView mPointsTextView;
        public TextView mRankTextView;
        /**

         Constructor for the ViewHolder.

         @param itemView The View representing the item to display.

         @param listener The click listener to set on the item.
         */

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
    /**

     Called when a new ViewHolder is needed.
     @param parent The parent ViewGroup.
     @param viewType The type of the new View.
     @return A new ScoreViewHolder.
     */

    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        ScoreViewHolder viewHolder = new ScoreViewHolder(view, mOnClickListener);
        return viewHolder;
    }
    /**

     Called when a ViewHolder needs to be updated with new data.
     @param holder The ScoreViewHolder to update.
     @param position The position of the item in the list.
     */

    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {
        Score currentScore = mScores.get(position);
        holder.mNameTextView.setText(currentScore.getName());
        holder.mPointsTextView.setText(String.valueOf(currentScore.getPoints()));
        holder.mRankTextView.setText(String.valueOf(position + 1));
        if (mClickedUsername != null && currentScore.getName() != null) {
            if (currentScore.getName().equals(mClickedUsername)) {
                holder.itemView.setBackgroundColor(Color.LTGRAY);
            } else {
                holder.itemView.setBackgroundColor(Color.WHITE);
            }
        }
    }

    /**

     Gets the number of items in the list.
     @return The number of items in the list.
     */
    @Override
    public int getItemCount() {
        return mScores.size();
    }
    /**

     Constructor for the ScoreAdapter.
     @param scores The list of Scores to display.
     @param listener The listener for click events on items in the list.
     */

    public ScoreAdapter(ArrayList<Score> scores, OnItemClickListener listener) {
        mScores = scores;
        mOnClickListener = listener;
    }
    /**

     Sets the username of the item that has been clicked.
     @param username The username of the clicked item.
     */

    public void setClickedUsername(String username) {
        mClickedUsername = username;
    }

}