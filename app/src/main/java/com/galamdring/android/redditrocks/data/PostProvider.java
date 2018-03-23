package com.galamdring.android.redditrocks.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by LMCKECHNIE on 3/20/2018.
 */

public class PostProvider extends ContentProvider {

    public static final int CODE_POST = 100;
    public static final int CODE_POST_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private RedditPostDbHelper mOpenHelper;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = PostContract.CONTENT_AUTHORITY;
        matcher.addURI(authority,PostContract.PATH_POST, CODE_POST);
        matcher.addURI(authority,PostContract.PATH_POST+"/#", CODE_POST_WITH_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new RedditPostDbHelper(getContext());
        return true;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        switch(sUriMatcher.match(uri)){
            case CODE_POST:
                db.beginTransaction();
                int rowsInserted = 0;
                try{
                    for(ContentValues value : values){
                        long _id = db.insert(PostContract.PostEntry.TABLE_NAME, null, value);
                        if(_id != -1){
                            rowsInserted++;
                        }
                    }
                }
                finally{
                    db.endTransaction();
                }
                if(rowsInserted>0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                Log.d("PostProvider.bulkInsert","Inserted "+rowsInserted+" rows.");
                return rowsInserted;
            default:
                return super.bulkInsert(uri, values);
        }

    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;

        switch(sUriMatcher.match(uri)){
            case CODE_POST:
                cursor = mOpenHelper.getReadableDatabase().query(
                        PostContract.PostEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported Uri: "+uri);

        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        Log.d("PostProvider.query","Got cursor with "+cursor.getCount()+" records.");
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int numRowsDeleted;

        if(null==selection) selection="1";

        switch (sUriMatcher.match(uri)) {

            case CODE_POST:
                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        PostContract.PostEntry.TABLE_NAME,
                        selection,
                        selectionArgs);

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
