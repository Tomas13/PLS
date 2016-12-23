package ru.startandroid.retrofit.Interface;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import ru.startandroid.retrofit.Model.Contributor;
import ru.startandroid.retrofit.Model.Edges;
import ru.startandroid.retrofit.Model.LastActions;

/**
 * Created by zhangali on 18.12.16.
 */

public interface GitHubService {
    @GET("repos/{owner}/{repo}/contributors")
    Call<List<Contributor>> repoContributors(
            @Path("owner") String owner,
            @Path("repo") String repo);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();


    @GET("/api/edges/")
    Call<List<Edges>> getEdges(

    );

    Retrofit retrofitEdges = new Retrofit.Builder()
            .baseUrl("http://astrabus.otgroup.kz/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();




    @GET("http://pls-test.kazpost.kz/api/mobile/history")
//    @Headers("Authorization : Bearer eyJhbGciOiJSUzI1NiJ9.eyJqdGkiOiIyNTdhZWIzMy0yNTBjLTQxYzUtOTIxYi1hNmQxMzljYjM5MTUiLCJleHAiOjE0ODI0ODYxODcsIm5iZiI6MCwiaWF0IjoxNDgyNDg1ODg3LCJpc3MiOiJodHRwOi8vcGxzLXRlc3Qua2F6cG9zdC5rei9hdXRoL3JlYWxtcy90b29scGFyIiwiYXVkIjoidG9vbHBhci11aSIsInN1YiI6IjMzYTIxMjBiLWU1YWUtNDJkNi04N2IyLTk2Y2JmNDU2MWZjYiIsInR5cCI6IkJlYXJlciIsImF6cCI6InRvb2xwYXItdWkiLCJub25jZSI6IjhkMjQ1OTFiLWI2YmYtNDA1Ni05ZTRhLWEyYzA2MjAwZTZkYiIsInNlc3Npb25fc3RhdGUiOiIxNTQ1MDU4MC1iOTYzLTRmMzAtYTQ2NS1iMThkM2QyMGZkMWEiLCJjbGllbnRfc2Vzc2lvbiI6ImJiMTdmYzkzLTM5ZTktNDFlYi1iYWEzLWIyYmI5YmI0MTUwMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vcGxzLXRlc3Qua2F6cG9zdC5rei8iXSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJ2aWV3LXByb2ZpbGUiXX19LCJuYW1lIjoi0JDQudC20LDQvSDQmtC-0LrRgtC10YPQsdCw0LXQstCwIiwicHJlZmVycmVkX3VzZXJuYW1lIjoidGVzdC5hc3QxNy5ycG8xIiwiZ2l2ZW5fbmFtZSI6ItCQ0LnQttCw0L0iLCJmYW1pbHlfbmFtZSI6ItCa0L7QutGC0LXRg9Cx0LDQtdCy0LAifQ.lgigBwD2bGP8rQ0UNAhZqzw4mVaWDcV_kYRPfWd5qPsfb3WRRCjunj4GoI92mAYq3Efa3f_8EAGbohGVJDzd1z_PvfcNFRdNvTHKX9w8lYqz6PFIkMfJ1Fqs4Hk-9CcGyZV-MyGB0w0iTeW9u-ssn0ZpOgXmrLbKHEzKAE8IYX0SWm-roDFl-emixfNddTvl6OMmkgPoVRPC59v0L9D6BD5li4dnq3ItJYS3kfejs6cVp1nEct-mHf-vqe8_YlkRRyPRSrIXaDbB4YS0X9earamy0FSge2zie5A6Bfno-KI1ks3VKRuCYcDVywjhmmWnZSW39eFMlHnERysngxXkXg")
    Call<List<LastActions>> getLastActions(

    );

    Retrofit retrofitLastActions = new Retrofit.Builder()
            .baseUrl("http://pls-test.kazpost.kz/")
            .addConverterFactory(GsonConverterFactory.create())

            .build();



}