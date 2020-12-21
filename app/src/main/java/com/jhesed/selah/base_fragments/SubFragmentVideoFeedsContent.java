package com.jhesed.selah.base_fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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
import com.jhesed.selah.BuildConfig;
import com.jhesed.selah.R;
import com.jhesed.selah.adapters.RSSVideoViewModel;
import com.jhesed.selah.adapters.VideoAdapter;
import com.jhesed.selah.api.ApiClient;
import com.jhesed.selah.api.ApiInterface;
import com.jhesed.selah.pojo.DatumPlaylist;
import com.jhesed.selah.pojo.DatumPlaylistItem;
import com.jhesed.selah.pojo.DatumPlaylistItemSnippet;
import com.jhesed.selah.pojo.PlaylistItemResource;
import com.jhesed.selah.pojo.PlaylistResource;
import com.prof.youtubeparser.models.videos.Video;

import java.util.ArrayList;
import java.util.Collections;
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
    private final ArrayList<Video> mVideoList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private VideoAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar progressBar;
    private RSSVideoViewModel viewModel;
    private ScrollView relativeLayout;
    private TextView sourceTitleTextView;

    public static SubFragmentVideoFeedsContent newInstance(String rssChannelId,
                                                           String rssChannelTitle,
                                                           String rssPlaylistId
    ) {
        channelId = rssChannelId;
        playlistId = rssPlaylistId;
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

                mAdapter.getList().clear();
                mAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(true);
                if (videos == null) {
                    if (playlistId != null) {
                        callPlaylistItemApi();
                    } else {
                        callPlaylistApi();
                    }
                }
            }
        });

        final TextView errorMessage = layout.findViewById(R.id.error_message);
        if (!isNetworkAvailable() && videos == null) {
            progressBar.setVisibility(GONE);
            errorMessage.setVisibility(View.VISIBLE);
        } else if (isNetworkAvailable()) {
            errorMessage.setVisibility(GONE);

            if (videos == null) {
                if (playlistId != null) {
                    callPlaylistItemApi();
                } else {
                    callPlaylistApi();
                }
            }
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

        Call<PlaylistResource>
                call = apiPlaylistInterface.getPlaylistId(channelId, 1, BuildConfig.KEY, "snippet");
        call.enqueue(new Callback<PlaylistResource>() {
            @Override
            public void onResponse(Call<PlaylistResource> call,
                                   Response<PlaylistResource> response) {

                PlaylistResource resource = response.body();

                try {
                    List<DatumPlaylist> items = resource.getItems();
                    playlistId = items.get(items.size() - 1).getId();
                } catch (Exception e) {
                    playlistId = null;
                }

                callPlaylistItemApi();
            }

            @Override
            public void onFailure(Call<PlaylistResource> call, Throwable t) {
                progressBar.setVisibility(GONE);
                call.cancel();
            }
        });

    }


    public void callPlaylistItemApi() {
        Call<PlaylistItemResource>
                call = apiPlaylistInterface
                .getPlaylistVideos(playlistId, 30, BuildConfig.KEY, "snippet");


        call.enqueue(new Callback<PlaylistItemResource>() {
            @Override
            public void onResponse(Call<PlaylistItemResource> call,
                                   Response<PlaylistItemResource> response) {
                playlistItemCallback(response);
            }

            @Override
            public void onFailure(Call<PlaylistItemResource> call, Throwable t) {
                progressBar.setVisibility(GONE);
                viewModel.setVideoLiveList(new ArrayList<Video>());
            }
        });

    }

    private void playlistItemCallback(Response<PlaylistItemResource> response) {
        PlaylistItemResource resource = response.body();

        List<DatumPlaylistItem> items;

        try {
            items = resource.getItems();
            Collections.reverse(items);

            for (int i = 0; i < items.size(); i++) {

                DatumPlaylistItemSnippet snippet = items.get(i).getSnippet();

                try {

                    mVideoList.add(new Video(snippet.getTitle(),
                            snippet.getResourceId().getVideoId(),
                            snippet.getThumbnails().getStandard().getUrl(),
                            snippet.getPublishedAt().substring(0, 10)));
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
            viewModel.setVideoLiveList(mVideoList);
        } catch (Exception e) {

            viewModel.setVideoLiveList(new ArrayList<Video>());

            e.printStackTrace();
        }

        progressBar.setVisibility(GONE);
    }
}