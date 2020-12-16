package com.jhesed.rbv.pojo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

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