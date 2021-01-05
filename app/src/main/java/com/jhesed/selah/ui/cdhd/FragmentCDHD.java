package com.jhesed.selah.ui.cdhd;

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
import com.jhesed.selah.api.cdhd.APIClient;
import com.jhesed.selah.api.cdhd.APIInterface;

public class FragmentCDHD extends Fragment {

    static APIInterface apiInterface;

    public static APIInterface getAPIInterface() {
        return apiInterface;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        apiInterface = APIClient.getClient(getContext()).create(APIInterface.class);

        View root = inflater.inflate(R.layout.fragment_cdhd,
                container, false);

        BottomNavigationView bottomNavigationView =
                root.findViewById(R.id.navigation_cdhd);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(
                            @NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            // ----------------------------- GLIMPSE TODAY
                            // -------------------------------------

                            case R.id.navigation_today:
                                selectedFragment = new FragmentDaily();
                                break;

                            // ------------------------------ GLIMPSE MONTH
                            // ------------------------------------

                            case R.id.navigation_month:
                                selectedFragment = FragmentMonthly.newInstance();
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
        transaction.replace(R.id.frame_layout, new FragmentDaily());
        transaction.addToBackStack(null);
        transaction.commit();

        return root;
    }

}