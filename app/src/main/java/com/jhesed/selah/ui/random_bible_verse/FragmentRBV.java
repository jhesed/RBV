package com.jhesed.selah.ui.random_bible_verse;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.jhesed.selah.R;

public class FragmentRBV extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_random_bible_verse,
                container, false);
        FragmentTransaction transaction =
                getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, SubFragmentRBVRandom.newInstance());
        transaction.addToBackStack(null);
        transaction.commit();

        return root;
    }
}