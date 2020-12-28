package com.example.memestore;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.memestore.general_classes.GetPostList;
import com.example.memestore.general_classes.GetRawData;
import com.example.memestore.general_classes.Post;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link memeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class memeFragment extends Fragment implements GetRawData.OnRawDataDownloaded, GetPostList.OnListAvailable {

    private static final String TAG = "memeFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static Context mContext;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public memeFragment(Context context) {
        // Required empty public constructor
        mContext=context;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment memeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static memeFragment newInstance(String param1, String param2) {
        memeFragment fragment = new memeFragment(mContext);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

//        GetRawData getData = new GetRawData(this);
//        getData.execute("https://api.edamam.com/search?app_id=b8939818&app_key=42670557ef175c9e8735b976b0c48c85&q=pastry");
        GetPostList getPostList=new GetPostList(this);
        getPostList.execute("https://api.edamam.com/search?app_id=b8939818&app_key=42670557ef175c9e8735b976b0c48c85&q=pastry");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meme, container, false);
    }

    @Override
    public void onRawDataDownloaded(String downloadedData) {
        Log.d(TAG, "onRawDataDownloaded: Got the raw data");
        Log.d(TAG, "onRawDataDownloaded: " + downloadedData);
    }

    @Override
    public void onListAvailable(ArrayList<Post> posts) {
        Log.d(TAG, "onListAvailable: Posts:"+posts);
    }
}