package com.jhesed.rbv.ui.news;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.jhesed.rbv.R;
import com.jhesed.rbv.adapters.ArticleAdapter;
import com.jhesed.rbv.adapters.MainViewModel;

public class SubFragmentNewsContent extends Fragment {

    // DEVOTIONALS:
    // incourage
    private static String urlString;
    private RecyclerView mRecyclerView;
    private ArticleAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar progressBar;
    private MainViewModel viewModel;
    private RelativeLayout relativeLayout;

//    private String urlString = "https://www.incourage.me/feed";
    // http://maxlucado.com/feed/custom

    // A - Daily Bread
//    private final static String urlString = "https://ourdailybread.org/feed/";
    // A - Answers in Genesis
//    private final static String urlString = "https://answersingenesis
//    .org/feeds/blogs/all-speakers/";

    // B - Voice of Martyr
//    private final static String urlString = "https://www.vomcanada
//    .com/News-Articles/?format=feed&type=rss";

    // C - Christian Headlines
//    private final static String urlString = "https://www.christianheadlines.com/rss";

    public static SubFragmentNewsContent newInstance(String rssFeedUrl) {
        urlString = rssFeedUrl;
        return new SubFragmentNewsContent();
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

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        progressBar = layout.findViewById(R.id.progressBar);

        mRecyclerView = layout.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);

        relativeLayout = layout.findViewById(R.id.root_layout);

        viewModel.getChannel().observe(getViewLifecycleOwner(), channel -> {
            if (channel != null) {
                if (channel.getTitle() != null) {
                    getActivity().setTitle(channel.getTitle());
                }
                mAdapter = new ArticleAdapter(channel.getArticles(), getContext());
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
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
            mAdapter.getArticleList().clear();
            mAdapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setRefreshing(true);
            viewModel.fetchFeed(urlString);
        });

        if (!isNetworkAvailable()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage(R.string.alert_message)
                    .setTitle(R.string.alert_title)
                    .setCancelable(false)
                    .setPositiveButton(R.string.alert_positive,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    getActivity().finish();
                                }
                            });

            AlertDialog alert = builder.create();
            alert.show();

        } else if (isNetworkAvailable()) {
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

