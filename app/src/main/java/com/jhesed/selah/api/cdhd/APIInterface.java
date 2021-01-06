package com.jhesed.selah.api.cdhd;

import com.jhesed.selah.pojo.cdhd.GlimpseDetailsResource;
import com.jhesed.selah.pojo.cdhd.GlimpseListResource;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Jhesed Tacadena 2018-07-21
 */


public interface APIInterface {

    // TODO: This is worth firing myself if this is a legit project; This is a hack. Do not
    //       do this at home. Replace this!! This is just a quick win hack!!!! Never follow this
    //       example!
    //       Replace this before 2038. RIP
//    String byetHeader =
//            "Cookie: __test=02a8bf99a4bbd1a67c6c4405c8aabe52; expires=Friday, January 1, 2038
//            at " +
//                    "7:55:55 AM; path=/";

    //    @Headers(byetHeader)
    @GET("/api/getDataSingle.php")
    Call<GlimpseDetailsResource> getGlimpseToday(@Query("title") String title);

    //    @Headers(byetHeader)
    @GET("/api/getData.php")
    Call<GlimpseListResource> getGlimpseList(@Query("date_range") String date_range);

}
