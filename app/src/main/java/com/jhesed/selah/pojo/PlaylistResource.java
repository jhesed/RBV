package com.jhesed.selah.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlaylistResource {

    @SerializedName("items")
    @Expose
    private List<DatumPlaylist> items = null;

    public List<DatumPlaylist> getItems() {
        return items;
    }

    public void setData(List<DatumPlaylist> items) {
        this.items = items;
    }

}
