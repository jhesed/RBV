package com.jhesed.rbv.ui.random_bible_verse;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.jhesed.rbv.R;

public class FragmentRBV extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_random_bible_verse,
                container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new
//        Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

//        BottomNavigationView bottomNavigationView =
//                root.findViewById(R.id.navigation_rbv);
//
//        bottomNavigationView.setOnNavigationItemSelectedListener(
//                new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//                    @Override
//                    public boolean onNavigationItemSelected(
//                            @NonNull MenuItem item) {
//                        Fragment selectedFragment = null;
//                        switch (item.getItemId()) {
//
////                            case R.id.navigation_random:
////                                selectedFragment = new SubFragmentRBVRandom();
////                                break;
//                        }
//
//                        FragmentTransaction transaction =
//                                getFragmentManager().beginTransaction();
//                        transaction
//                                .replace(R.id.frame_layout, selectedFragment);
//                        transaction.addToBackStack(null);
//                        transaction.commit();
//                        return true;
//                    }
//                });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction =
                getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, SubFragmentRBVRandom.newInstance());
        transaction.addToBackStack(null);
        transaction.commit();

        return root;
    }
}