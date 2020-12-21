package com.jhesed.selah.pojo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DatumPlaylist {

    @SerializedName("etag")
    @Expose
    private String etag;

    @SerializedName("id")
    @Expose
    private String id;

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