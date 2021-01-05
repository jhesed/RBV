package com.jhesed.selah.pojo.cdhd;

/**
 * Created by jhesed on 7/28/2018.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GlimpseListResource {

    @SerializedName("data")
    @Expose
    private List<DatumList> data = null;

    public List<DatumList> getData() {
        return data;
    }

    public void setData(List<DatumList> data) {
        this.data = data;
    }

}