package com.jhesed.selah.pojo.cdhd;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DatumDetails {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("glimpse_date")
    @Expose
    private String glimpseDate;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("heading")
    @Expose
    private String heading;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("prayer_focus")
    @Expose
    private String prayerFocus;
    @SerializedName("featured_verse")
    @Expose
    private String featuredVerse;
    @SerializedName("featured_quote")
    @Expose
    private String featuredQuote;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("created_at")
    @Expose
    private Object createdAt;
    @SerializedName("image_link")
    @Expose
    private String imageLink;
    @SerializedName("generic_quote")
    @Expose
    private String genericQuote;
    @SerializedName("reference")
    @Expose
    private String reference;
    @SerializedName("transition")
    @Expose
    private String transition;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGlimpseDate() {
        return glimpseDate;
    }

    public void setGlimpseDate(String glimpseDate) {
        this.glimpseDate = glimpseDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPrayerFocus() {
        return prayerFocus;
    }

    public void setPrayerFocus(String prayerFocus) {
        this.prayerFocus = prayerFocus;
    }

    public String getFeaturedVerse() {
        return featuredVerse;
    }

    public void setFeaturedVerse(String featuredVerse) {
        this.featuredVerse = featuredVerse;
    }

    public String getFeaturedQuote() {
        return featuredQuote;
    }

    public void setFeaturedQuote(String featuredQuote) {
        this.featuredQuote = featuredQuote;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Object createdAt) {
        this.createdAt = createdAt;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getGenericQuote() {
        return genericQuote;
    }

    public String getReference() {
        return reference;
    }

    public String getTransition() {
        return transition;
    }

    public void setTransition(String transition) {
        this.transition = transition;
    }

}