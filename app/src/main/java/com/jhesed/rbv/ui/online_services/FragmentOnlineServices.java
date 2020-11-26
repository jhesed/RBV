package com.jhesed.rbv.ui.online_services;

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

public class FragmentOnlineServices extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_online_services,
                container, false);

        BottomNavigationView bottomNavigationView =
                root.findViewById(R.id.navigation_online_service);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(
                            @NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.sub_nav_devotional1:
                                // JSOS
                                selectedFragment = SubFragmentVideoFeedsContent
                                        .newInstance("UCgJi0hyM78LM3pAYdTM2e8g");
                                break;
                        }
                        switch (item.getItemId()) {
                            case R.id.sub_nav_devotional2:
                                // Elevation
                                selectedFragment = SubFragmentVideoFeedsContent
                                        .newInstance("UCIQqvZbHSwX0yKNVK1MyYjQ");
                                break;
                        }
                        switch (item.getItemId()) {
                            case R.id.sub_nav_devotional3:
                                // Transformation
                                selectedFragment = SubFragmentVideoFeedsContent
                                        .newInstance("UCYv-siSKd3Gn9IsliO95gIw");
                                break;
                        }
                        switch (item.getItemId()) {
                            case R.id.sub_nav_devotional4:
                                // Passion Church
                                selectedFragment = SubFragmentVideoFeedsContent
                                        .newInstance("UCzT4tQfAZEsm_yMql_10dpg");
                                break;
                        }
                        switch (item.getItemId()) {
                            case R.id.sub_nav_devotional5:
                                // Victory Estancia
                                selectedFragment = SubFragmentVideoFeedsContent
                                        .newInstance("UCf0C93fFPtf94s4z9uYxO3Q");
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
                        .newInstance("UCgJi0hyM78LM3pAYdTM2e8g"));
        transaction.addToBackStack(null);
        transaction.commit();

        return root;
    }

}