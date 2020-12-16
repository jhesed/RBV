package com.jhesed.rbv.adapters;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.prof.youtubeparser.Parser;
import com.prof.youtubeparser.models.stats.Statistics;
import com.prof.youtubeparser.models.videos.Video;

import java.util.List;

public class RSSVideoViewModel extends ViewModel {

    private static String CHANNEL_ID;
    private static String PLAYLIST_ID;

    private MutableLiveData<List<Video>> mVideoLiveList = new MutableLiveData<>();
    private MutableLiveData<String> mSnackbar = new MutableLiveData<>();
    private MutableLiveData<Statistics> mLiveStats = new MutableLiveData<>();
    private String mNextToken = null;
    private Parser mParser = new Parser();
//    private ArrayList<Video> mVideoList = new ArrayList<>();


    // All Channel IDS

    /*
        [
            JSOS service, Elevation service, Transformation Service, Passion Service, Victory
            Service,
            JSOS Music, Elevation music, Transformation Music, Passion Music, Victory Music,
            Answers in Genesis, Kent Hovind
        ]
    */
//    private final static List<String> CHANNEL_IDS =
//            Arrays.asList(
//                    // Services
//                    "UCgJi0hyM78LM3pAYdTM2e8g", "UCIQqvZbHSwX0yKNVK1MyYjQ",
//                    "UCYv-siSKd3Gn9IsliO95gIw", "UCzT4tQfAZEsm_yMql_10dpg",
//                    "UCf0C93fFPtf94s4z9uYxO3Q",
//                    // Music
//                    "UCTximiYuUDOlgChGaco2Otg", "UCSf-NCzjwcnXErUBW_qeFvA",
//                    "UCkD-5QEpMPN9Fr3u_k1WJyw", "UCsh-HOmo0Fk_5uq1kut8dWw",
//                    "UCJwUUYlyEL4RwhpSaWV9EaA",
//                    // Science and the Bible
//                    "UCicvc24eAbFp-thRrBnSP2A", "UCxiEtqPja47nnqsJNrdOIQQ"
//            );

    public MutableLiveData<List<Video>> getVideoLiveList() {
        return mVideoLiveList;
    }

    public void setVideoLiveList(List<Video> videoList) {
        this.mVideoLiveList.postValue(videoList);
    }

    public void setChannelId(String channelId) {
        CHANNEL_ID = channelId;
    }

    public void setPlayListId(String playListId) {
        PLAYLIST_ID = playListId;
    }

    public MutableLiveData<String> getSnackbar() {
        return mSnackbar;
    }

    public void onSnackbarShowed() {
        this.mSnackbar.setValue(null);
    }

}