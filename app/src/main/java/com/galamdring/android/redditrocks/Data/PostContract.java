package com.galamdring.android.redditrocks.Data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by LMCKECHNIE on 3/20/2018.
 */

public class PostContract {
    public static final String CONTENT_AUTHORITY ="com.galamdring.android.redditrocks";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);
    public static final String PATH_POST = "post";

    public static final class PostEntry implements BaseColumns {
        public static final String TABLE_NAME = "posts";
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_POST).build();
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_SUBREDDIT = "subreddit";
        public static final String COLUMN_POST_TIME = "post_time";
        public static final String COLUMN_SCORE = "score";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_THUMBNAIL_URL = "thumbnail_url";
        public static final String COLUMN_LINK = "link";
        public static final String COLUMN_COMMENTS_COUNT = "comments_count";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_OVER_18 = "over_18_post";
    }
}
