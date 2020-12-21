package com.jhesed.selah.ui.songs;

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
import com.jhesed.selah.base_fragments.SubFragmentVideoFeedsContent;

public class FragmentSongs extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_songs,
                container, false);

        BottomNavigationView bottomNavigationView =
                root.findViewById(R.id.navigation_songs);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(
                            @NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.sub_nav1:
                                // JSOS
                                selectedFragment = SubFragmentVideoFeedsContent
                                        .newInstance("UCTximiYuUDOlgChGaco2Otg", "JSOS Worship",
                                                "PLqpZgV-DNfhRtcHhcqDjAmibrI7S22Sur");
                                break;
                        }
                        switch (item.getItemId()) {
                            case R.id.sub_nav2:
                                // Elevation
                                selectedFragment = SubFragmentVideoFeedsContent
                                        .newInstance("UCSf-NCzjwcnXErUBW_qeFvA",
                                                "Elevation Worship", null);
                                break;
                        }
                        switch (item.getItemId()) {
                            case R.id.sub_nav3:
                                // Transformation
                                selectedFragment = SubFragmentVideoFeedsContent
                                        .newInstance("UC4q12NoPNySbVqwpw4iO5Vg",
                                                "Hillsong Worship", null);
                                break;
                        }
                        switch (item.getItemId()) {
                            case R.id.sub_nav4:
                                // Passion Church
                                selectedFragment = SubFragmentVideoFeedsContent
                                        .newInstance("UCsh-HOmo0Fk_5uq1kut8dWw", "Passion Music",
                                                null);
                                break;
                        }
                        switch (item.getItemId()) {
                            case R.id.sub_nav5:
                                // Victory
                                selectedFragment = SubFragmentVideoFeedsContent
                                        .newInstance("UCJwUUYlyEL4RwhpSaWV9EaA", "Victory Worship",
                                                "PL7YL4KBCn7fahaVNRN-mH1TRdmou_UoyK");
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
                SubFragmentVideoFeedsContent
                        .newInstance("UCTximiYuUDOlgChGaco2Otg", "JSOS Worship",
                                "PLqpZgV-DNfhRtcHhcqDjAmibrI7S22Sur"));
        transaction.addToBackStack(null);
        transaction.commit();

        return root;
    }

}