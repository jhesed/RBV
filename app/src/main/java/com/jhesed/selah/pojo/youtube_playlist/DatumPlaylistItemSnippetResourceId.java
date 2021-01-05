package com.jhesed.selah.pojo.youtube_playlist;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DatumPlaylistItemSnippetResourceId {

    @SerializedName("videoId")
    @Expose
    private String videoId = null;

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId() {
        this.videoId = videoId;
    }
}