package com.jhesed.rbv.ui.random_bible_verse;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RandomBibleVerseViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RandomBibleVerseViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}