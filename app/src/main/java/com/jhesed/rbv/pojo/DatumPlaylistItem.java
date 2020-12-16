package com.jhesed.rbv.pojo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DatumPlaylistItem {

    @SerializedName("snippet")
    @Expose
    private DatumPlaylistItemSnippet snippet = null;

    @SerializedName("etag")
    @Expose
    private String etag;

    @SerializedName("id")
    @Expose
    private String id;

    public DatumPlaylistItemSnippet getSnippet() {
        return snippet;
    }

    public void setSnippet() {
        this.snippet = snippet;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag() {
        this.etag = etag;
    }

    public String getId() {
        return id;
    }

    public void setId() {
        this.id = id;
    }

}