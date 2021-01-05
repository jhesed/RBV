package com.jhesed.selah.ui.cdhd;

/**
 * Created by jhesed on 8/4/2018.
 */


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.jhesed.selah.R;

import static com.jhesed.selah.ui.cdhd.Common.getTodayAsString;

public class FragmentDaily extends Fragment {

    public static FragmentDaily newInstance() {
        return new FragmentDaily();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_daily, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {

        Common.getGlimpse(getTodayAsString(), view, FragmentCDHD.getAPIInterface());

        // Pull to refresh event
//        final SwipeRefreshLayout pullToRefresh = (SwipeRefreshLayout)
//                view.findViewById(R.id.pull_to_refresh);

        //  TODO: Fix me
//        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                getGlimpse(getTodayAsString(), view);
//                pullToRefresh.setRefreshing(false);
//            }
//        });

    }

}