package com.jhesed.rbv.adapters;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jhesed.rbv.BuildConfig;
import com.jhesed.rbv.pojo.DatumPlaylistItem;
import com.jhesed.rbv.pojo.DatumPlaylistItemSnippet;
import com.jhesed.rbv.pojo.PlaylistItemResource;
import com.jhesed.rbv.pojo.PlaylistResource;
import com.prof.youtubeparser.Parser;
import com.prof.youtubeparser.models.stats.Statistics;
import com.prof.youtubeparser.models.videos.Video;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

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

    private void setNextToken(String nextToken) {
        this.mNextToken = nextToken;
    }

    public void fetchVideos() {

        mParser.onFinish(new Parser.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(@NonNull List<Video> list, @NonNull String nextPageToken) {
//                setNextToken(nextPageToken);
//                mVideoList.addAll(list);
//                setVideoLiveList(mVideoList);
            }

            @Override
            public void onError(@NonNull Exception e) {
                setVideoLiveList(new ArrayList<Video>());
                e.printStackTrace();
                mSnackbar.postValue("An error has occurred. Please try again");
            }
        });

        // Original
//        String requestUrl = mParser.generateRequest(
//                CHANNEL_ID,
//                5,
//                Parser.ORDER_DATE,
//                BuildConfig.KEY
//        );
//
//        mParser.execute(requestUrl);

//        // List playlists
//        String playlistUrl = generatePlaylistRequest(CHANNEL_ID, 3);
//        Log.e("JHESED--------: ", playlistUrl);
//        mParser.execute(playlistUrl);
    }

//    public String generatePlaylistRequest(String channelId, int maxResult) {
//        return BASE_URL_PLAYLIST + channelId + "&maxResults=" + maxResult + "&key=" +
//                BuildConfig.KEY + "&order=date";
//    }
//
//    public String generatePlaylistItemsRequest(String channelId, int maxResult) {
//        return BASE_URL_PLAYLIST_ITEMS + channelId + "&maxResults=" + maxResult + "&key=" +
//                BuildConfig.KEY + "&order=date";
//    }


//    public void fetchNextVideos() {
//        if (mNextToken != null) {
//            mParser.onFinish(new Parser.OnTaskCompleted() {
//                @Override
//                public void onTaskCompleted(@NonNull List<Video> list,
//                                            @NonNull String nextPageToken) {
//                    setNextToken(nextPageToken);
//                    mVideoList.addAll(list);
//                    setVideoLiveList(mVideoList);
//                }
//
//                @Override
//                public void onError(@NonNull Exception e) {
//                    e.printStackTrace();
//                    mSnackbar.postValue("An error has occurred. Please try again");
//                }
//            });
//
//            String requestUrl = mParser.generateMoreDataRequest(
//                    CHANNEL_ID,
//                    20,
//                    Parser.ORDER_DATE,
//                    BuildConfig.KEY,
//                    mNextToken
//            );
//
//            mParser.execute(requestUrl);
//        }
//    }

//    public void callPlaylistApi() {
//
//        Log.e("JHESED------", "callPlaylistApi ");
//        Call<PlaylistResource>
//                call = apiPlaylistInterface.getPlaylistId(channelId, 1, BuildConfig.KEY, "snippet");
//        call.enqueue(new Callback<PlaylistResource>() {
//            @Override
//            public void onResponse(Call<PlaylistResource> call,
//                                   Response<PlaylistResource> response) {
//
//                PlaylistResource resource = response.body();
//                try {
//                    playlistId = resource.getItems().get(0).getId();
//
//                    Log.e("JHESED------", "got playlist id" + playlistId);
//                } catch (Exception e) {
//                    Log.e("JHESED------", "error playlist id" + playlistId);
//                    playlistId = null;
//                }
//                Log.e("JHESED------", "calling callPlaylistItemApi");
//
////                callPlaylistApi();
//                // TODO: Uncomment this
//                callPlaylistItemApi();
//            }
//
//            @Override
//            public void onFailure(Call<PlaylistResource> call, Throwable t) {
//                progressBar.setVisibility(GONE);
//                Log.e("JHESED------", "on failure 2: ");
//                call.cancel();
//            }
//        });
//
//    }
//
//
//    public void callPlaylistItemApi() {
//        Log.e("JHESED------", "BEGGINING ID: " + playlistId);
//        Call<PlaylistItemResource>
//                call = apiPlaylistInterface
//                .getPlaylistVideos(playlistId, 20, BuildConfig.KEY, "snippet");
//        Log.e("JHESED------", "playlist inside ID: " + playlistId);
//
//        Log.e("JHESED----- GOT CALL-", String.valueOf(call.request().url()));
//
//        call.enqueue(new Callback<PlaylistItemResource>() {
//            @Override
//            public void onResponse(Call<PlaylistItemResource> call,
//                                   Response<PlaylistItemResource> response) {
//
//                Log.e("JHESED------", "callPlaylistItemApi BBBBBB ");
//                PlaylistItemResource resource = response.body();
//
//                Log.e("JHESED------", "callPlaylistItemApi AAAAA ");
//                List<DatumPlaylistItem> items;
//
//                Log.e("JHESED------", "onResponse callPlaylistItemApi");
//                try {
//                    items = resource.getItems();
//                    Log.e("JHESED------", "items: " + items);
//
//                    for (int i = 0; i < items.size(); i++) {
//
//                        Log.e("JHESED------", "getting snippet ");
//                        DatumPlaylistItemSnippet snippet = items.get(i).getSnippet();
//                        Log.e("JHESED------", "video ID: " + snippet.getResourceId().getVideoId());
//                        Log.e("JHESED------", "title: " +snippet.getTitle());
//                        Log.e("JHESED------", "url: " +snippet.getThumbnails().getDefault().getUrl());
//                        Log.e("JHESED------", "getPublishedAt: " +snippet.getPublishedAt());
//
//                        try {
//
//                            Log.e("JHESED------", "Adding to video list ");
//                            mVideoList.add(new Video(snippet.getTitle(),
//                                    snippet.getResourceId().getVideoId(),
//                                    snippet.getThumbnails().getDefault().getUrl(),
//                                    snippet.getPublishedAt()));
//                        }
//                        catch (Exception e) {
//
//                            Log.e("JHESED------", "error AAAA: " + e);
//                            e.printStackTrace();
//                        }
//                    }
//                    viewModel.setVideoLiveList(mVideoList);
//                } catch (Exception e) {
//
//                    Log.e("JHESED------", "Setting video live list ");
//                    viewModel.setVideoLiveList(new ArrayList<Video>());
//
//                    Log.e("JHESED------", "error BBBB: " + e);
//                    e.printStackTrace();
////                    mSnackbar.postValue("An error has occurred. Please try again");
//                }
//
//                Log.e("JHESED------", "Clearing up progressbar");
//                progressBar.setVisibility(GONE);
//            }
//
//            @Override
//            public void onFailure(Call<PlaylistItemResource> call, Throwable t) {
////                errorMessage.setVisibility(View.VISIBLE);
//                Log.e("JHESED------", "onFailure ");
//
//                Log.e("JHESED------", String.valueOf(call.request().url()));
//                Log.e("JHESED------ exception", Log.getStackTraceString(new Exception()));
//                progressBar.setVisibility(GONE);
//                viewModel.setVideoLiveList(new ArrayList<Video>());
////                mSnackbar.postValue("An error has occurred. Please try again");
//            }
//        });
//
//    }

}