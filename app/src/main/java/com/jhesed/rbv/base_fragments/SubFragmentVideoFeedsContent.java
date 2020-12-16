package com.jhesed.rbv.base_fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.jhesed.rbv.BuildConfig;
import com.jhesed.rbv.R;
import com.jhesed.rbv.adapters.RSSVideoViewModel;
import com.jhesed.rbv.adapters.VideoAdapter;
import com.jhesed.rbv.api.ApiClient;
import com.jhesed.rbv.api.ApiInterface;
import com.jhesed.rbv.pojo.DatumPlaylist;
import com.jhesed.rbv.pojo.DatumPlaylistItem;
import com.jhesed.rbv.pojo.DatumPlaylistItemSnippet;
import com.jhesed.rbv.pojo.PlaylistItemResource;
import com.jhesed.rbv.pojo.PlaylistResource;
import com.prof.youtubeparser.models.videos.Video;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

public class SubFragmentVideoFeedsContent extends Fragment {

    static ApiInterface apiPlaylistInterface;
    private static String channelId;
    private static String channelTitle;
    private static String playlistId;
    private static ArrayList<String> videos;
    private RecyclerView mRecyclerView;
    private VideoAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar progressBar;
    private RSSVideoViewModel viewModel;
    private ScrollView relativeLayout;
    private TextView sourceTitleTextView;

    private ArrayList<Video> mVideoList = new ArrayList<>();

    public static SubFragmentVideoFeedsContent newInstance(String rssChannelId,
                                                           String rssChannelTitle
//                                                           String rssPlaylistId
    ) {
        channelId = rssChannelId;
//        playlistId = rssPlaylistId;
        channelTitle = rssChannelTitle;
        return new SubFragmentVideoFeedsContent();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiPlaylistInterface = ApiClient.getYoutubeClient(getContext()).create(ApiInterface.class);

        final View layout =
                inflater.inflate(
                        R.layout.sub_fragment_online_service,
                        container, false);

        viewModel = new ViewModelProvider(this).get(RSSVideoViewModel.class);
        viewModel.setChannelId(channelId);
        viewModel.setPlayListId(playlistId);

        progressBar = layout.findViewById(R.id.progressBar);

        mRecyclerView = layout.findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);

        relativeLayout = layout.findViewById(R.id.root_layout);
        sourceTitleTextView = layout.findViewById(R.id.source_title);
        sourceTitleTextView.setText(channelTitle);


        mAdapter = new VideoAdapter(new ArrayList<Video>(), this);
        mRecyclerView.setAdapter(mAdapter);

        viewModel.getVideoLiveList().observe(getActivity(), new Observer<List<Video>>() {
            @Override
            public void onChanged(List<Video> videos) {
                if (videos != null) {

//                    callPlaylistApi();

                    mAdapter.setVideos(videos);
                    mAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        viewModel.getSnackbar().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null) {
                    Snackbar.make(relativeLayout, s, Snackbar.LENGTH_LONG).show();
                    viewModel.onSnackbarShowed();
                }
            }
        });


        mSwipeRefreshLayout = layout.findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        mSwipeRefreshLayout.canChildScrollUp();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                callPlaylistApi();

//                mAdapter.getList().clear();
//                mAdapter.notifyDataSetChanged();
//                mSwipeRefreshLayout.setRefreshing(true);
//                viewModel.fetchVideos();
            }
        });

        final TextView errorMessage = layout.findViewById(R.id.error_message);
        if (!isNetworkAvailable()) {
            progressBar.setVisibility(GONE);
            errorMessage.setVisibility(View.VISIBLE);
        } else if (isNetworkAvailable()) {
            errorMessage.setVisibility(GONE);
//            viewModel.fetchVideos();
            callPlaylistApi();
        }
        return layout;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void callPlaylistApi() {

        Log.e("JHESED------", "callPlaylistApi ");
        Call<PlaylistResource>
                call = apiPlaylistInterface.getPlaylistId(channelId, 1, BuildConfig.KEY, "snippet");
        call.enqueue(new Callback<PlaylistResource>() {
            @Override
            public void onResponse(Call<PlaylistResource> call,
                                   Response<PlaylistResource> response) {

                PlaylistResource resource = response.body();
                try {
                    playlistId = resource.getItems().get(0).getId();

                    Log.e("JHESED------", "got playlist id" + playlistId);
                } catch (Exception e) {
                    Log.e("JHESED------", "error playlist id" + playlistId);
                    playlistId = null;
                }
                Log.e("JHESED------", "calling callPlaylistItemApi");

//                callPlaylistApi();
                // TODO: Uncomment this
                callPlaylistItemApi();
            }

            @Override
            public void onFailure(Call<PlaylistResource> call, Throwable t) {
                progressBar.setVisibility(GONE);
                Log.e("JHESED------", "on failure 2: ");
                call.cancel();
            }
        });

    }


    public void callPlaylistItemApi() {
        Log.e("JHESED------", "BEGGINING ID: " + playlistId);
        Call<PlaylistItemResource>
                call = apiPlaylistInterface
                .getPlaylistVideos(playlistId, 20, BuildConfig.KEY, "snippet");
        Log.e("JHESED------", "playlist inside ID: " + playlistId);

        Log.e("JHESED----- GOT CALL-", String.valueOf(call.request().url()));

        call.enqueue(new Callback<PlaylistItemResource>() {
            @Override
            public void onResponse(Call<PlaylistItemResource> call,
                                   Response<PlaylistItemResource> response) {

                PlaylistItemResource resource = response.body();

                List<DatumPlaylistItem> items;

                try {
                    items = resource.getItems();

                    for (int i = 0; i < items.size(); i++) {

                        DatumPlaylistItemSnippet snippet = items.get(i).getSnippet();

                        try {

                            mVideoList.add(new Video(snippet.getTitle(),
                                    snippet.getResourceId().getVideoId(),
                                    snippet.getThumbnails().getStandard().getUrl(),
                                    snippet.getPublishedAt()));
                        }
                        catch (Exception e) {

                            e.printStackTrace();
                        }
                    }
                    viewModel.setVideoLiveList(mVideoList);
                } catch (Exception e) {

                    Log.e("JHESED------", "Setting video live list ");
                    viewModel.setVideoLiveList(new ArrayList<Video>());

                    Log.e("JHESED------", "error BBBB: " + e);
                    e.printStackTrace();
//                    mSnackbar.postValue("An error has occurred. Please try again");
                }

                Log.e("JHESED------", "Clearing up progressbar");
                progressBar.setVisibility(GONE);
            }

            @Override
            public void onFailure(Call<PlaylistItemResource> call, Throwable t) {
//                errorMessage.setVisibility(View.VISIBLE);
                Log.e("JHESED------", "onFailure ");

                Log.e("JHESED------", String.valueOf(call.request().url()));
                Log.e("JHESED------ exception", Log.getStackTraceString(new Exception()));
                progressBar.setVisibility(GONE);
                viewModel.setVideoLiveList(new ArrayList<Video>());
//                mSnackbar.postValue("An error has occurred. Please try again");
            }
        });

    }

//
//    public void callPlaylistItemApi() {
//
//        Log.e("JHESED------", "callPlaylistItemApi ");
//        Call<PlaylistItemResource>
//                call = apiPlaylistInterface.getPlaylistVideos(playlistId, 20, BuildConfig.KEY, "snippet");
//        call.enqueue(new Callback<PlaylistItemResource>() {
//            @Override
//            public void onResponse(Call<PlaylistItemResource> call,
//                                   Response<PlaylistItemResource> response) {
//
//                Log.e("JHESED------", "got INSIDE!!!!!! id" + playlistId);
//                PlaylistItemResource resource = response.body();
//                String result = "";
//                try {
//                    result = resource.getItems().get(0).getId();
//
//                    Log.e("JHESED------", "got result id" + result);
//                } catch (Exception e) {
//                    Log.e("JHESED------", "error result id" + result);
//                    playlistId = null;
//                }
//                Log.e("JHESED------", "calling callPlaylistItemApi");
//
//            }
//
//            @Override
//            public void onFailure(Call<PlaylistItemResource> call, Throwable t) {
//                Log.e("JHESED------", String.valueOf(call.request().url()));
//                Log.e("JHESED------ exception", Log.getStackTraceString(new Exception()));
//                progressBar.setVisibility(GONE);
//                Log.e("JHESED------", "on failure 2: ");
//                call.cancel();
//            }
//        });
//
//    }
}