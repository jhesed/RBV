package com.jhesed.rbv.ui.bible_science_end_times;

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
import com.jhesed.rbv.base_fragments.SubFragmentVideoFeedsContent;

public class FragmentBibleScienceEndTimes extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_bible_science_end_times,
                container, false);

        BottomNavigationView bottomNavigationView =
                root.findViewById(R.id.navigation_bible_science_end_times);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(
                            @NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.sub_nav1:
                                // Answers in Genesis (Ark Encounter)
                                selectedFragment = SubFragmentVideoFeedsContent
                                        .newInstance("UCicvc24eAbFp-thRrBnSP2A",
                                                "Answers in Genesis (Ark Encounter)",
                                                "PL1v9pqs4w1mxPzVyvpDbScRBdq0mHo51B");
                                break;
                        }
                        switch (item.getItemId()) {
                            case R.id.sub_nav2:
                                // Answers in Genesis (Animal Encounter)
                                selectedFragment = SubFragmentVideoFeedsContent
                                        .newInstance("UCicvc24eAbFp-thRrBnSP2A",
                                                "Answers in Genesis (Animal Encounter)",
                                                "PL1v9pqs4w1myh1dZLG8Zx0ERTf9lHXXRS");
                                break;
                        }
                        switch (item.getItemId()) {
                            case R.id.sub_nav3:
                                // Answers in Genesis (News)
                                selectedFragment = SubFragmentVideoFeedsContent
                                        .newInstance("UCicvc24eAbFp-thRrBnSP2A",
                                                "Answers in Genesis (News)",
                                                "PL1v9pqs4w1mwBZow_Io30Npjtyi1O9g9W");
                                break;
                        }
                        switch (item.getItemId()) {
                            case R.id.sub_nav4:
                                // Kent Hovind Debates
                                selectedFragment = SubFragmentVideoFeedsContent
                                        .newInstance("UCxiEtqPja47nnqsJNrdOIQQ",
                                                "Kent Hovind (Bible vs Evolution Debates)",
                                                "PL6-cVj-ZRivpHQhRLUXmLV3nxZ_kWtND-");
                                break;
                        }
                        switch (item.getItemId()) {
                            case R.id.sub_nav5:
                                // Kent Hovind End Times
                                selectedFragment = SubFragmentVideoFeedsContent
                                        .newInstance("UCxiEtqPja47nnqsJNrdOIQQ",
                                                "Kent Hovind (End Times)",
                                                "PL6-cVj-ZRivq-tuSTjte5aNBu8rdU9rtU");
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
                        .newInstance("UCicvc24eAbFp-thRrBnSP2A",
                                "Answers in Genesis (Ark Encounter)",
                                "PL1v9pqs4w1mxPzVyvpDbScRBdq0mHo51B"));
        transaction.addToBackStack(null);
        transaction.commit();

        return root;
    }

}