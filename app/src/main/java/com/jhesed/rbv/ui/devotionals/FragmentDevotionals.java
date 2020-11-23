package com.jhesed.rbv.ui.devotionals;

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

public class FragmentDevotionals extends Fragment {

    PrayerDbHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.db = new PrayerDbHelper(container.getContext());
        this.db.prepopulateData();

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
                                selectedFragment = SubFragmentDevotionalsContent
                                        .newInstance("https://wordpoints.com/feed/", "Word Points");
                                break;
                            case R.id.sub_nav_devotional2:
                                selectedFragment = SubFragmentDevotionalsContent.newInstance(
                                        "https://devotionaltreasure.wordpress.com/feed/", "Devotional Treasure");
                                break;
                            case R.id.sub_nav_devotional3:
                                selectedFragment = SubFragmentDevotionalsContent
                                        .newInstance("https://everydaywithgod.com/feed/", "Everyday with God");
                                break;
                            case R.id.sub_nav_devotional4:
                                selectedFragment = SubFragmentDevotionalsContent
                                        .newInstance("https://www.biblegateway.com/blog/feed/", "Bible Gateway");
                                break;
                            case R.id.sub_nav_devotional5:
                                selectedFragment = SubFragmentDevotionalsContent.newInstance(
                                        "https://thegoodnewsherald.wordpress.com/feed/", "The Good News Herald");
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
                SubFragmentDevotionalsContent.newInstance("https://wordpoints.com/feed/", "Word Points"));
        transaction.addToBackStack(null);
        transaction.commit();

        return root;
    }

}