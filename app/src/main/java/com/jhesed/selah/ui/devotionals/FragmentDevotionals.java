package com.jhesed.selah.ui.devotionals;

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

public class FragmentDevotionals extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_devotionals,
                container, false);

        BottomNavigationView bottomNavigationView =
                root.findViewById(R.id.navigation_devotionals);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(
                            @NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.sub_nav_devotional1:
                                selectedFragment = SubFragmentRSSFeedContent
                                        .newInstance("https://wordpoints.com/feed/", "Word Points",
                                                false);
                                break;
                            case R.id.sub_nav_devotional2:
                                selectedFragment = SubFragmentRSSFeedContent.newInstance(
                                        "https://devotionaltreasure.wordpress.com/feed/",
                                        "Devotional Treasure", false);
                                break;
                            case R.id.sub_nav_devotional3:
                                selectedFragment = SubFragmentRSSFeedContent
                                        .newInstance("https://ourdailybread.org/feed/",
                                                "Our Daily Bread", true);
                                break;
                            case R.id.sub_nav_devotional4:
                                selectedFragment = SubFragmentRSSFeedContent.newInstance(
                                        "https://thegoodnewsherald.wordpress.com/feed/",
                                        "The Good News Herald", false);
                                break;
                            case R.id.sub_nav_devotional5:
                                selectedFragment = SubFragmentRSSFeedContent
                                        .newInstance("https://everydaywithgod.com/feed/",
                                                "Everyday with God", false);
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
                        .newInstance("https://wordpoints.com/feed/", "Word Points", false));
        transaction.addToBackStack(null);
        transaction.commit();

        return root;
    }

}