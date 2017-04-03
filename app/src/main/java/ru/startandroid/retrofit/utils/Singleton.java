package ru.startandroid.retrofit.utils;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by root on 12/26/16.
 */

public class Singleton {

    public static OkHttpClient getUserClient(String credentials){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addNetworkInterceptor(new StethoInterceptor()); //подключаю Stetho
        httpClient.readTimeout(60, TimeUnit.SECONDS);
        httpClient.connectTimeout(60, TimeUnit.SECONDS);
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();

            Request request = original.newBuilder()
                    .header("Authorization", credentials) //добавляю хедер
                    .method(original.method(), original.body())
                    .build();

            return chain.proceed(request);
        });

        return httpClient.build();
    }


    public static OkHttpClient getUserClient(){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
//        httpClient.addNetworkInterceptor(new StethoInterceptor()); //подключаю Stetho
        httpClient.readTimeout(60, TimeUnit.SECONDS);
        httpClient.connectTimeout(60, TimeUnit.SECONDS);
       /* httpClient.addInterceptor(chain -> {
            Request original = chain.request();

            Request request = original.newBuilder()
//                    .header("Authorization", credentials) //добавляю хедер
//                    .header("grant_type", "password")
//                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .method(original.method(), original.body())
                    .build();

            return chain.proceed(request);
        });*/

        return httpClient.build();
    }



}
