package com.galamdring.android.redditrocks.data;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by LMCKECHNIE on 3/21/2018.
 */

public class RedditRocksSyncIntentService extends IntentService{
    public RedditRocksSyncIntentService(){
        super("RedditRocksSyncIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        PostSyncTask.syncPosts(this);
    }
}
