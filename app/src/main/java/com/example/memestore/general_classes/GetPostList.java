package com.example.memestore.general_classes;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetPostList extends AsyncTask<String,Void,ArrayList<Post>> implements GetRawData.OnRawDataDownloaded {
    private static final String TAG = "GetPostList";
    private ArrayList<Post> mPosts;

    public interface OnListAvailable{
        void onListAvailable(ArrayList<Post> posts);
    }

    private OnListAvailable mCallback;

    public GetPostList(OnListAvailable callback){
        mCallback = callback;
        mPosts = new ArrayList<>();
    }

    @Override
    protected void onPostExecute(ArrayList<Post> posts) {
        Log.d(TAG, "onPostExecute: Called");
        super.onPostExecute(posts);
        mCallback.onListAvailable(posts);
    }

    @Override
    protected ArrayList<Post> doInBackground(String... strings) {
        Log.d(TAG, "doInBackground: Called");
        String urlPath = strings[0];
        GetRawData getRawData = new GetRawData(this);
        getRawData.getRawDataOnSameThread(urlPath);
        return mPosts;
    }

    @Override
    public void onRawDataDownloaded(String downloadedData) {
        Log.d(TAG, "onRawDataDownloaded: Got the data");
        Log.d(TAG, "onRawDataDownloaded: " + downloadedData);

        try{
            JSONObject jsonData = new JSONObject(downloadedData);
            JSONArray jsonPostArray = jsonData.getJSONArray("data");

            for(int i=0;i<jsonPostArray.length();i++)
            {
                JSONObject jsonPost = (JSONObject) jsonPostArray.get(i);
                Post currentPost = new Post();
                currentPost.setPostImageUrl(jsonPost.getString("image"));
                currentPost.setPostName(jsonPost.getString("name"));
                currentPost.setPostTags(jsonPost.getString("tags"));

                mPosts.add(currentPost);
            }
        }catch(JSONException je){
            Log.d(TAG, "onRawDataDownloaded: Error processing the json data" + je.getMessage());
        }
    }
}