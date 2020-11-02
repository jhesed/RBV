package com.jhesed.rbv.ui.prayer_journal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.jhesed.rbv.R;
import com.jhesed.rbv.db.RandomBibleVerseDbHelper;


public class SubFragmentHelp extends Fragment {

    RandomBibleVerseDbHelper db;

    public static SubFragmentHelp newInstance() {
        return new SubFragmentHelp();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View layout =
                inflater.inflate(
                        R.layout.sub_fragment_prayer_journal_tutorial,
                        container, false);
        return layout;
    }
}