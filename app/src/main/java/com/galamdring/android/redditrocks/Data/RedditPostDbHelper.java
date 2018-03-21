package com.galamdring.android.redditrocks.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by LMCKECHNIE on 3/20/2018.
 */

public class RedditPostDbHelper extends SQLiteOpenHelper {

    /*
     * This is the name of our database. Database names should be descriptive and end with the
     * .db extension.
     */
    public static final String DATABASE_NAME = "reddit.db";

    /*
     * If you change the database schema, you must increment the database version or the onUpgrade
     * method will not be called.
     *
     * The reason DATABASE_VERSION starts at 3 is because Sunshine has been used in conjunction
     * with the Android course for a while now. Believe it or not, older versions of Sunshine
     * still exist out in the wild. If we started this DATABASE_VERSION off at 1, upgrading older
     * versions of Sunshine could cause everything to break. Although that is certainly a rare
     * use-case, we wanted to watch out for it and warn you what could happen if you mistakenly
     * version your databases.
     */
    private static final int DATABASE_VERSION = 1;

    public RedditPostDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_POST_TABLE =
                "CREATE TABLE " + PostContract.PostEntry.TABLE_NAME+" (" +
                        PostContract.PostEntry._ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        PostContract.PostEntry.COLUMN_COMMENTS_COUNT + " INTEGER NOT NULL, " +
                        PostContract.PostEntry.COLUMN_LINK + "TEXT NOT NULL, "+
                        PostContract.PostEntry.COLUMN_POST_TIME + " INTEGER NOT NULL, "+
                        PostContract.PostEntry.COLUMN_SCORE + " INTEGER NOT NULL, " +
                        PostContract.PostEntry.COLUMN_SUBREDDIT + " TEXT NOT NULL, " +
                        PostContract.PostEntry.COLUMN_THUMBNAIL_URL + " TEXT NOT NULL, "+
                        PostContract.PostEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                        PostContract.PostEntry.COLUMN_TYPE +" TEXT NOT NULL, " +
                        PostContract.PostEntry.COLUMN_OVER_18+" INTEGER NOT NULL, " +
                        PostContract.PostEntry.COLUMN_AUTHOR +" TEXT NOT NULL"
                        +"); ";

        db.execSQL(SQL_CREATE_POST_TABLE);
    }
    /**
     * This database is only a cache for online data, so its upgrade policy is simply to discard
     * the data and call through to onCreate to recreate the table. Note that this only fires if
     * you change the version number for your database (in our case, DATABASE_VERSION). It does NOT
     * depend on the version number for your application found in your app/build.gradle file. If
     * you want to update the schema without wiping data, commenting out the current body of this
     * method should be your top priority before modifying this method.
     *
     * @param db             Database that is being upgraded
     * @param oldVersion     The old database version
     * @param newVersion     The new database version
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ PostContract.PostEntry.TABLE_NAME);
        onCreate(db);
    }
}
