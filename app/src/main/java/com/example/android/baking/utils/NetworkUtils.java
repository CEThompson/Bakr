package com.example.android.baking.utils;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkUtils {

    public static final String BASE_RECIPES_URL="http://go.udacity.com/";
    public static final String RECIPES_ENDPOINT="android-baking-app-json";

    private static Retrofit retrofit;
    private static OkHttpClient client = new OkHttpClient();
    private static GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create();

    public static Retrofit getRetrofitInstance(){
        if (retrofit==null){
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_RECIPES_URL)
                    .client(client)
                    .addConverterFactory(gsonConverterFactory)
                    .build();
        }
        return retrofit;
    }

}
