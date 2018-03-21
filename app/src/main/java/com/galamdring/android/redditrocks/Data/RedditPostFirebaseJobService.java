package com.galamdring.android.redditrocks.Data;

import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by LMCKECHNIE on 3/21/2018.
 */

public class RedditPostFirebaseJobService extends JobService {

    private AsyncTask<Void, Void, Void> mFetchPostTask;

    @Override
    public boolean onStartJob(final JobParameters job) {
        mFetchPostTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Context context = getApplicationContext();
                PostSyncTask.syncPosts(context);
                jobFinished(job, false);
                return null;
            }
        };
        mFetchPostTask.execute();
        return true;

    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if(mFetchPostTask!= null){
            mFetchPostTask.cancel(true);
        }
        return true;
    }
}
