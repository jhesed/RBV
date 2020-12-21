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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.jhesed.selah.R;
import com.jhesed.selah.adapters.ArticleAdapter;
import com.jhesed.selah.adapters.RSSViewModel;

import static android.view.View.GONE;

public class SubFragmentRSSFeedContent extends Fragment {

    private static String urlString;
    private static String sourceTitle;
    private static boolean isDailyBread = false;

    private RecyclerView mRecyclerView;
    private ArticleAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar progressBar;
    private RSSViewModel viewModel;
    private ScrollView relativeLayout;
    private TextView sourceTitleTextView;

    public static SubFragmentRSSFeedContent newInstance(String rssFeedUrl, String rssSourceTitle,
                                                        boolean rssIsDailyBread) {
        urlString = rssFeedUrl;
        sourceTitle = rssSourceTitle;
        isDailyBread = rssIsDailyBread;
        return new SubFragmentRSSFeedContent();
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
                        R.layout.sub_fragment_devotionals,
                        container, false);

        viewModel = new ViewModelProvider(this).get(RSSViewModel.class);

        progressBar = layout.findViewById(R.id.progressBar);
        TextView errorMessage = layout.findViewById(R.id.error_message);

        mRecyclerView = layout.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);

        relativeLayout = layout.findViewById(R.id.root_layout);
        sourceTitleTextView = layout.findViewById(R.id.source_title);
        sourceTitleTextView.setText(sourceTitle);

        viewModel.getChannel().observe(getViewLifecycleOwner(), channel -> {
            if (channel != null) {
                if (channel.getTitle() != null) {
                    getActivity().setTitle(channel.getTitle());
                }
                mAdapter = new ArticleAdapter(channel.getArticles(), getContext(), isDailyBread);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                progressBar.setVisibility(GONE);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        viewModel.getSnackbar().observe(getViewLifecycleOwner(), s -> {
            if (s != null) {
                Snackbar.make(relativeLayout, s, Snackbar.LENGTH_LONG).show();
                viewModel.onSnackbarShowed();
            }
        });

        mSwipeRefreshLayout = layout.findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        mSwipeRefreshLayout.canChildScrollUp();
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            errorMessage.setVisibility(GONE);
            mAdapter.getArticleList().clear();
            mAdapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setRefreshing(true);
            viewModel.fetchFeed(urlString);
        });

        if (!isNetworkAvailable()) {
            progressBar.setVisibility(GONE);
            errorMessage.setVisibility(View.VISIBLE);
        } else if (isNetworkAvailable()) {
            errorMessage.setVisibility(GONE);
            viewModel.fetchFeed(urlString);
        }
        return layout;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

