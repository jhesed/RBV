package com.jhesed.selah.adapters;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.prof.youtubeparser.models.videos.Video;

import java.util.List;

public class RSSVideoViewModel extends ViewModel {

    private static String CHANNEL_ID;

    private final MutableLiveData<List<Video>> mVideoLiveList = new MutableLiveData<>();
    private final MutableLiveData<String> mSnackbar = new MutableLiveData<>();

    public MutableLiveData<List<Video>> getVideoLiveList() {
        return mVideoLiveList;
    }

    public void setVideoLiveList(List<Video> videoList) {
        this.mVideoLiveList.postValue(videoList);
    }

    public void setChannelId(String channelId) {
        CHANNEL_ID = channelId;
    }

    public MutableLiveData<String> getSnackbar() {
        return mSnackbar;
    }

    public void onSnackbarShowed() {
        this.mSnackbar.setValue(null);
    }

}