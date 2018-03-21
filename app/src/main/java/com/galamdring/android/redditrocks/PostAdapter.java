package com.galamdring.android.redditrocks;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by LMCKECHNIE on 3/21/2018.
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostAdapterViewHolder> {


    private final Context mContext;

    public void swapCursor(Cursor data) {
        mCursor = data;
        notifyDataSetChanged();
    }

    public interface PostAdapterOnClickHandler {
        void onClick();
    }

    final private PostAdapterOnClickHandler mClickHandler;

    private Cursor mCursor;

    public PostAdapter(@NonNull Context context, PostAdapterOnClickHandler clickHandler){
        mContext=context;
        mClickHandler = clickHandler;
    }

    @Override
    public PostAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = R.layout.post_item;
        View view = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        view.setFocusable(true);

        return new PostAdapterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(PostAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        String title = mCursor.getString(MainActivity.INDEX_POST_TITLE);
        String description = mCursor.getString(MainActivity.INDEX_POST_SUBREDDIT);
        int score = mCursor.getInt(MainActivity.INDEX_POST_SCORE);

        holder.textViewTitle.setText(title);
        holder.textViewDetails.setText(description);
        holder.textViewScore.setText(score);
    }

    @Override
    public int getItemCount() {
        if(null == mCursor) return 0;
        return mCursor.getCount();
    }



    class PostAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textViewTitle;
        public TextView textViewDetails;
        public TextView textViewScore;

        public PostAdapterViewHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.post_title);
            textViewDetails = itemView.findViewById(R.id.post_details);
            textViewScore = itemView.findViewById(R.id.post_score);
            //TODO add more items here when we add them to the view
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPostion = getAdapterPosition();
            mCursor.moveToPosition(adapterPostion);
            mClickHandler.onClick();
        }
    }
}
