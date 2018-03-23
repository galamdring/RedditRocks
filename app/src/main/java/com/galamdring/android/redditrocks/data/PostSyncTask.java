package com.galamdring.android.redditrocks.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


/**
 * Created by LMCKECHNIE on 3/20/2018.
 */

public class PostSyncTask {

    private static String after;
    private static String TAG = "PostSyncTask.syncPosts";

    synchronized public static void syncPosts(Context context){
        try{
            URL url = getUrl();
            String jsonPostResponse = getResponseFromHttpUrl(url);
            try {
                ContentValues[] posts = getPostsContentValuesFromJson(context, jsonPostResponse);
                if(posts != null && posts.length != 0){
                    ContentResolver postContentResolver = context.getContentResolver();
                    //Delete the old data.
                    Log.d(TAG, "Deleting old data.");
                    postContentResolver.delete(PostContract.PostEntry.CONTENT_URI,null,null);
                    Log.d(TAG, "Inserting " +posts.length +" posts.");
                    postContentResolver.bulkInsert(PostContract.PostEntry.CONTENT_URI, posts);
                    // TODO : Notify here if we are going to


                }
            }
            catch(JSONException e){
                e.printStackTrace();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        Log.i("PostSyncTask", "URL: "+url);
        URLConnection urlConnection = url.openConnection();
        BufferedReader buff = null;
        try {
             buff= new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuffer strbuff = new StringBuffer();
            String line;
            while ((line = buff.readLine())!=null){
                strbuff.append(line);
            }
            return strbuff.toString();
        } finally {
            if(buff !=null) buff.close();
        }
    }

    private static URL getUrl(){
        try{
            return new URL("https://www.reddit.com/r/askreddit/.json");
        }
        catch(MalformedURLException e){
            e.printStackTrace();
            return null;
        }
    }

    public static ContentValues[] getPostsContentValuesFromJson(Context context, String jsonString)
            throws JSONException {
        if(jsonString.equals("")){
            Toast.makeText(context, "Failed to load data from url: "+getUrl().toString(), Toast.LENGTH_LONG).show();
        }
        JSONObject postJson = new JSONObject(jsonString).getJSONObject("data");
        JSONArray children = postJson.getJSONArray("children");

        after = postJson.getString("after");
        ContentValues[] postContentValues = new ContentValues[children.length()];
        for(int i=0; i < children.length(); i++){
            JSONObject child = children.getJSONObject(i).getJSONObject("data");
            ContentValues postValues = new ContentValues();
            String author = child.optString("author");
            postValues.put(PostContract.PostEntry.COLUMN_AUTHOR, author);
            int comment_count = child.optInt("num_comments");
            postValues.put(PostContract.PostEntry.COLUMN_COMMENTS_COUNT, comment_count);
            String title = child.optString("title");
            postValues.put(PostContract.PostEntry.COLUMN_TITLE, title);
            postValues.put(PostContract.PostEntry.COLUMN_LINK, child.optString("url"));
            postValues.put(PostContract.PostEntry.COLUMN_POST_TIME,child.optLong("created_utc"));
            postValues.put(PostContract.PostEntry.COLUMN_SCORE,child.optLong("score"));
            postValues.put(PostContract.PostEntry.COLUMN_SUBREDDIT,child.optString("subreddit"));
            postValues.put(PostContract.PostEntry.COLUMN_THUMBNAIL_URL,child.optString("thumbnail"));
            postValues.put(PostContract.PostEntry.COLUMN_TYPE,child.optString("link_flair_type"));

            boolean nsfw = child.optBoolean("over_18");
            int over_18 = 0;
            if(nsfw) over_18 =1;
            postValues.put(PostContract.PostEntry.COLUMN_OVER_18, over_18);
            postContentValues[i] = postValues;
        }

        return postContentValues;
    }
}
