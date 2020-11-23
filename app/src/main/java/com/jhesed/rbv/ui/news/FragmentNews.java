package com.jhesed.rbv.ui.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jhesed.rbv.R;
import com.jhesed.rbv.db.PrayerDbHelper;

public class FragmentNews extends Fragment {

    PrayerDbHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.db = new PrayerDbHelper(container.getContext());
        this.db.prepopulateData();

        View root = inflater.inflate(R.layout.fragment_devotionals,
                container, false);

        BottomNavigationView bottomNavigationView =
                root.findViewById(R.id.navigation_prayer_journal);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(
                            @NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.sub_nav_devotional1:
                                selectedFragment = SubFragmentNewsContent
                                        .newInstance("https://www.biblegateway.com/blog/feed/");
                                break;
                            case R.id.sub_nav_devotional2:
                                selectedFragment = SubFragmentNewsContent
                                        .newInstance("https://blog.truthforlife.org/rss.xml");
                                break;
                            case R.id.sub_nav_devotional3:
                                selectedFragment = SubFragmentNewsContent
                                        .newInstance("https://www.crossway.org/articles/rss/");
                                break;
                            case R.id.sub_nav_devotional4:
                                selectedFragment = SubFragmentNewsContent
                                        .newInstance("http://biblestudiesforlife.com/feed/");
                                break;
                            case R.id.sub_nav_devotional5:
                                selectedFragment = SubFragmentNewsContent
                                        .newInstance("http://biblestudiesforlife.com/feed/");
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

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction =
                getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout,
                SubFragmentNewsContent.newInstance("https://www.incourage.me/feed"));
        transaction.addToBackStack(null);
        transaction.commit();

        return root;
    }

}