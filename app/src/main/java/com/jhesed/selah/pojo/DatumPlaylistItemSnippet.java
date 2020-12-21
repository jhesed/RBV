package com.jhesed.selah.pojo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DatumPlaylistItemSnippet {

    @SerializedName("resourceId")
    @Expose
    private DatumPlaylistItemSnippetResourceId resourceId = null;

    @SerializedName("thumbnails")
    @Expose
    private DatumPlaylistItemSnippetThumbnails thumbnails = null;

    @SerializedName("publishedAt")
    @Expose
    private String publishedAt = null;

    @SerializedName("title")
    @Expose
    private String title = null;

    @SerializedName("description")
    @Expose
    private String description = null;

    public DatumPlaylistItemSnippetResourceId getResourceId() {
        return resourceId;
    }

    public void setResourceId() {
        this.resourceId = resourceId;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt() {
        this.publishedAt = publishedAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle() {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription() {
        this.description = description;
    }

    public DatumPlaylistItemSnippetThumbnails getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails() {
        this.thumbnails = thumbnails;
    }
}