package ru.startandroid.retrofit.Interface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.startandroid.retrofit.Model.Contributor;
import ru.startandroid.retrofit.Model.Edges;

/**
 * Created by zhangali on 18.12.16.
 */

public interface GitHubService {
    @GET("repos/{owner}/{repo}/contributors")
    Call<List<Contributor>> repoContributors(
            @Path("owner") String owner,
            @Path("repo") String repo);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();


    @GET("/api/edges/")
    Call<List<Edges>> getEdges(

    );

    public static final Retrofit retrofitEdges = new Retrofit.Builder()
            .baseUrl("http://astrabus.otgroup.kz/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

}