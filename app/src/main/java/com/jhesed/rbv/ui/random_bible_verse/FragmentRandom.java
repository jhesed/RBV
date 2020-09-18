package com.jhesed.rbv.ui.random_bible_verse;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.jhesed.rbv.R;


public class FragmentRandom extends Fragment {

    public static FragmentRandom newInstance() {
        return new FragmentRandom();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sub_fragment_random_bible_verse_random, container, false);
    }
}