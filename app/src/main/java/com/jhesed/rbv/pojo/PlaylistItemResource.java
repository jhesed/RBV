package com.jhesed.rbv.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlaylistItemResource {

    @SerializedName("items")
    @Expose
    private List<DatumPlaylistItem> items = null;

    public List<DatumPlaylistItem> getItems() {
        return items;
    }

    public void setSnippet(List<DatumPlaylistItem> items) {
        this.items = items;
    }

}
