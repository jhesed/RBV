package com.jhesed.selah.api.cdhd;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Jhesed Tacadena 2018-07-21
 */

public class APIClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(Context context) {

        retrofit = new Retrofit.Builder()
                .baseUrl("https://cdhd.000webhostapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(provideOkHttpClient(context))
                .build();

        return retrofit;
    }

    private static OkHttpClient provideOkHttpClient(Context context) {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addInterceptor(provideOfflineCacheInterceptor(context))
                .addInterceptor(logging)
                .addNetworkInterceptor(provideCacheInterceptor())
                .cache(provideCache(context))
                .build();
    }

    private static Cache provideCache(Context context) {
        Cache cache = null;
        try {
            cache = new Cache(new File(context.getCacheDir(), "http-cache"),
                    100 * 1024 * 1024); // 100 MB
        } catch (Exception e) {
//            Timer.e( e, "Could not create Cache!" );
        }
        return cache;
    }

    private static Interceptor provideCacheInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());

                // re-write response header to force use of cache
                CacheControl cacheControl = new CacheControl.Builder()
                        .maxAge(30, TimeUnit.MINUTES)
//                        .maxAge(1, TimeUnit.MILLISECONDS)
                        .build();

                return response.newBuilder()
                        .header("Cache-Control", cacheControl.toString())
                        .build();
            }
        };
    }

    private static Interceptor provideOfflineCacheInterceptor(final Context context) {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

                if (!isInternetAvailable(context)) {
                    CacheControl cacheControl = new CacheControl.Builder()
                            .maxStale(31, TimeUnit.DAYS)
                            .build();

                    request = request.newBuilder()
                            .cacheControl(cacheControl)
                            .build();
                }

                return chain.proceed(request);
            }
        };
    }

    private static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
