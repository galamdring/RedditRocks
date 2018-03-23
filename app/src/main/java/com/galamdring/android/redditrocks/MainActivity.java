package com.galamdring.android.redditrocks;

import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.galamdring.android.redditrocks.data.PostContract;
import com.galamdring.android.redditrocks.data.PostSyncUtils;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, PostAdapter.PostAdapterOnClickHandler {


    public static final String[] MAIN_POST_PROJECTION = {
            PostContract.PostEntry.COLUMN_TITLE,
            PostContract.PostEntry.COLUMN_SUBREDDIT,
            PostContract.PostEntry.COLUMN_SCORE
    };

    public static final int INDEX_POST_TITLE =0;
    public static final int INDEX_POST_SUBREDDIT =1 ;
    public static final int INDEX_POST_SCORE =2 ;

    private static final int ID_POST_LOADER = 12;
    private PostAdapter mPostAdapter;
    private RecyclerView mRecyclerView;
    private int mPosition = RecyclerView.NO_POSITION;
    private ProgressBar mLoadingIndicator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0f);

        mRecyclerView = findViewById(R.id.post_recyclerView);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mPostAdapter = new PostAdapter(this, this);

        mRecyclerView.setAdapter(mPostAdapter);

        showLoading();
        PostSyncUtils.intialize(this);

        getSupportLoaderManager().initLoader(ID_POST_LOADER, null, this);




    }

    private void showLoading() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch(id){
            case ID_POST_LOADER:
                Uri postQueryUri = PostContract.PostEntry.CONTENT_URI;
                Log.d("MainActivity","Getting Posts from URI: "+postQueryUri);
                String sortOrder = null;
                String selection = null;

                return new CursorLoader(this,
                        postQueryUri,
                        MAIN_POST_PROJECTION,
                        selection,
                        null,
                        sortOrder);
            default:
                throw new RuntimeException("Loader Not Implemented. "+id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d("Loader", "onLoadFinished called.");
        mPostAdapter.swapCursor(data);
        if(mPosition==RecyclerView.NO_POSITION) mPosition=0;
        mRecyclerView.smoothScrollToPosition(mPosition);
        if(data.getCount() != 0) showPostDataView();
        else Toast.makeText(this, "Data was empty.", Toast.LENGTH_SHORT).show();
    }

    private void showPostDataView() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mPostAdapter.swapCursor(null);
    }

    @Override
    public void onClick() {
        Toast.makeText(this,"You clicked!",Toast.LENGTH_LONG).show();
    }
}
