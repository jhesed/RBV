package com.jhesed.rbv.ui.prayer_journal;

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

public class FragmentPJ extends Fragment {

    PrayerDbHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.db = new PrayerDbHelper(container.getContext());
        this.db.prepopulateData();

        View root = inflater.inflate(R.layout.fragment_prayer_journal,
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
                            case R.id.sub_nav_prayer_items:
                                selectedFragment = new SubFragmentPJPersonal();
                                break;

                            case R.id.sub_nav_tutorial:
                                selectedFragment = new SubFragmentHelp();
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
        transaction.replace(R.id.frame_layout, new SubFragmentPJPersonal());
        transaction.addToBackStack(null);
        transaction.commit();

        return root;
    }

}