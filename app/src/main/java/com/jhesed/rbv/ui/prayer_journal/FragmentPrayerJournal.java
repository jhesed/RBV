package com.jhesed.rbv.ui.prayer_journal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.jhesed.rbv.R;

public class FragmentPrayerJournal extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root =
                inflater.inflate(R.layout.fragment_prayer_journal, container,
                        false);
        return root;
    }
}