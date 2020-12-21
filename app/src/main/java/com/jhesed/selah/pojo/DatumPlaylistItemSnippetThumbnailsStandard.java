package com.jhesed.selah.pojo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DatumPlaylistItemSnippetThumbnailsStandard {

    @SerializedName("url")
    @Expose
    private String url = null;

    public String getUrl() {
        return url;
    }

    public void setUrl() {
        this.url = url;
    }
}