package com.jhesed.rbv.base_fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.jhesed.rbv.R;
import com.jhesed.rbv.adapters.RSSVideoViewModel;
import com.jhesed.rbv.adapters.VideoAdapter;
import com.prof.youtubeparser.models.stats.Statistics;
import com.prof.youtubeparser.models.videos.Video;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class SubFragmentVideoFeedsContent extends Fragment {

    private static String channelId;
    private static String channelTitle;

    private RecyclerView mRecyclerView;
    private VideoAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar progressBar;
    private RSSVideoViewModel viewModel;
    private ScrollView relativeLayout;
    private TextView sourceTitleTextView;


    public static SubFragmentVideoFeedsContent newInstance(String rssChannelId,
                                                           String rssChannelTitle) {
        channelId = rssChannelId;
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

        viewModel.getLiveStats().observe(getActivity(), new Observer<Statistics>() {
            @Override
            public void onChanged(Statistics stats) {
                if (stats != null) {
                    String body = "Views: " + stats.getViewCount() + "\n" +
                            "Like: " + stats.getLikeCount() + "\n" +
                            "Dislike: " + stats.getDislikeCount() + "\n" +
                            "Number of comment: " + stats.getCommentCount() + "\n" +
                            "Number of favourite: " + stats.getFavoriteCount();

                    final AlertDialog.Builder dialogBuilder =
                            new AlertDialog.Builder(getContext());
                    dialogBuilder.setTitle("Stats");
                    dialogBuilder.setMessage(body);
                    dialogBuilder.setCancelable(false);
                    dialogBuilder.setNegativeButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    dialogBuilder.show();
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
                viewModel.fetchVideos();
            }
        });

        final TextView errorMessage = layout.findViewById(R.id.error_message);
        if (!isNetworkAvailable()) {
            progressBar.setVisibility(GONE);
            errorMessage.setVisibility(View.VISIBLE);
        }
        else if (isNetworkAvailable()) {
            errorMessage.setVisibility(GONE);
            viewModel.fetchVideos();
        }
        return layout;
    }

    public void getVideoStats(String videoId) {
        if (viewModel != null) {
            viewModel.fetchStatistics(videoId);
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}