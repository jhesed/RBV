package com.jhesed.selah.ui.random_bible_verse;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.jhesed.selah.R;
import com.jhesed.selah.db.BibleVerseContract;
import com.jhesed.selah.db.RandomBibleVerseDbHelper;

import java.util.Random;


public class SubFragmentRBVRandom extends Fragment {

    RandomBibleVerseDbHelper db;

    public static SubFragmentRBVRandom newInstance() {
        return new SubFragmentRBVRandom();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.db = new RandomBibleVerseDbHelper(container.getContext());
        this.db.prepopulateData();

        final View layout =
                inflater.inflate(
                        R.layout.sub_fragment_random_bible_verse_random,
                        container, false);

        randomizeVerse(layout);

        Button randomButton = layout.findViewById(R.id.button_random);
        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                randomizeVerse(layout);
            }
        });

        return layout;
    }

    public void randomizeVerse(View layout) {
        Cursor cursor = db.select(getRandomId());
        cursor.moveToNext();

        // Set Verse Title
        final String title =
                cursor.getString(cursor.getColumnIndex(
                        BibleVerseContract.BibleVerseEntry.COL_TITLE));
        TextView titleTextView =
                layout.findViewById(R.id.verse_title);
        titleTextView.setText(title);

        // Set MBB Content
        final String mbb =
                cursor.getString(cursor.getColumnIndex(
                        BibleVerseContract.BibleVerseEntry.COL_MBB));
        TextView mbbTextView = layout.findViewById(R.id.text_mbb);
        mbbTextView.setText(mbb);

        // Set NIV Content
        final String niv =
                cursor.getString(cursor.getColumnIndex(
                        BibleVerseContract.BibleVerseEntry.COL_NIV));
        TextView nivTextView = layout.findViewById(R.id.text_niv);
        nivTextView.setText(niv);

        // Set NASB Content
        final String nasb =
                cursor.getString(cursor.getColumnIndex(
                        BibleVerseContract.BibleVerseEntry.COL_NASB));
        TextView nasbTextView = layout.findViewById(R.id.text_nasb);
        nasbTextView.setText(nasb);
    }

    public int getRandomId() {
        final int min = 1;
        final int bibleVerseLength = 33;
        return new Random().nextInt((bibleVerseLength - min) + 1) + min;
    }

}