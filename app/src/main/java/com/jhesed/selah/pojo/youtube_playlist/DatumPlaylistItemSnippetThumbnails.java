package com.jhesed.selah.pojo.youtube_playlist;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DatumPlaylistItemSnippetThumbnails {

    @SerializedName("standard")
    @Expose
    private DatumPlaylistItemSnippetThumbnailsStandard standard = null;

    public DatumPlaylistItemSnippetThumbnailsStandard getStandard() {
        return standard;
    }

    public void setDefault() {
        this.standard = standard;
    }
}