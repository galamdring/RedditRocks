package com.galamdring.android.redditrocks.Data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;


/**
 * Created by LMCKECHNIE on 3/20/2018.
 */

public class PostSyncTask {

    private static String after;

    synchronized public static void syncPosts(Context context){
        try{
            URL url = getUrl();
            String jsonPostResponse = getResponseFromHttpUrl(url);
            try {
                ContentValues[] posts = getPostsContentValuesFromJson(context, jsonPostResponse);
                if(posts != null && posts.length != 0){
                    ContentResolver postContentResolver = context.getContentResolver();
                    //Delete the old data.
                    postContentResolver.delete(PostContract.PostEntry.CONTENT_URI,null,null);

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
            return new URL("http://www.reddit.com/r/askreddit/.json");
        }
        catch(MalformedURLException e){
            e.printStackTrace();
            return null;
        }
    }

    public static ContentValues[] getPostsContentValuesFromJson(Context context, String jsonString)
            throws JSONException {
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
        }
        // TODO: Finish this method
        return postContentValues;
    }
}
