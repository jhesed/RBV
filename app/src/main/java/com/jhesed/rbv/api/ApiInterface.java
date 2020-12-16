package com.jhesed.rbv.api;

import com.jhesed.rbv.pojo.PlaylistItemResource;
import com.jhesed.rbv.pojo.PlaylistResource;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("/youtube/v3/playlists")
    Call<PlaylistResource> getPlaylistId(
            @Query("channelId") String channelId,
            @Query("maxResults") int maxResults,
            @Query("key") String key,
            @Query("part") String part
    );

    @GET("/youtube/v3/playlistItems")
    Call<PlaylistItemResource> getPlaylistVideos(
            @Query("playlistId") String playlistId,
            @Query("maxResults") int maxResults,
            @Query("key") String key,
            @Query("part") String part
    );
}

