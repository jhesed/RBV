package com.jhesed.selah.pojo.cdhd;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GlimpseDetailsResource {

    @SerializedName("data")
    @Expose
    private List<DatumDetails> data = null;

    public List<DatumDetails> getData() {
        return data;
    }

    public void setData(List<DatumDetails> data) {
        this.data = data;
    }

}