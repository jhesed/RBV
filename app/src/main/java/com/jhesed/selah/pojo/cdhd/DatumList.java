package com.jhesed.selah.pojo.cdhd;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DatumList {

    @SerializedName("glimpse_date")
    @Expose
    private String glimpseDate;
    @SerializedName("heading_phil")
    @Expose
    private String headingPhil;
    @SerializedName("heading_world")
    @Expose
    private String headingWorld;

    public String getGlimpseDate() {
        return glimpseDate;
    }

    public void setGlimpseDate(String glimpseDate) {
        this.glimpseDate = glimpseDate;
    }

    public String getHeadingPhil() {
        return headingPhil;
    }

    public void setHeadingPhil(String headingPhil) {
        this.headingPhil = headingPhil;
    }

    public String getHeadingWorld() {
        return headingWorld;
    }

    public void setHeadingWorld(String headingWorld) {
        this.headingWorld = headingWorld;
    }

}