package com.jhesed.selah.ui.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jhesed.selah.R;
import com.jhesed.selah.base_fragments.SubFragmentRSSFeedContent;

public class FragmentNews extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_news,
                container, false);

        BottomNavigationView bottomNavigationView =
                root.findViewById(R.id.navigation_news);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(
                            @NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.sub_nav_devotional1:
                                selectedFragment = SubFragmentRSSFeedContent
                                        .newInstance("https://christiannewsjournal.com/feed/",
                                                "Christian News Journal", false);
                                break;
                            case R.id.sub_nav_devotional2:
                                selectedFragment = SubFragmentRSSFeedContent.newInstance(
                                        "https://answersingenesis" +
                                                ".org/feeds/answers-research-journal/",
                                        "Answers In Genesis", false);
                                break;
                            case R.id.sub_nav_devotional3:
                                selectedFragment = SubFragmentRSSFeedContent
                                        .newInstance(
                                                "https://www.vomcanada" +
                                                        ".com/News-Articles/?format=feed&type=rss",
                                                "Voice of Martyrs", false);
                                break;
                            case R.id.sub_nav_devotional4:
                                selectedFragment = SubFragmentRSSFeedContent
                                        .newInstance("https://www.christianheadlines.com/rss/",
                                                "Christian Headlines", false);
                                break;
                            case R.id.sub_nav_devotional5:
                                selectedFragment = SubFragmentRSSFeedContent.newInstance(
                                        "https://www.biblegateway.com/blog/feed/",
                                        "Bible Gateway", false);
                                break;
                        }

                        FragmentTransaction transaction =
                                getFragmentManager().beginTransaction();
                        transaction
                                .replace(R.id.frame_layout, selectedFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        return true;
                    }
                });

        bottomNavigationView.setItemIconTintList(null);

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction =
                getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout,
                SubFragmentRSSFeedContent
                        .newInstance("https://christiannewsjournal.com/feed/",
                                "Christian News Journal", false));
        transaction.addToBackStack(null);
        transaction.commit();

        return root;
    }

}