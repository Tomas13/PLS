package ru.startandroid.retrofit.Interface;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import ru.startandroid.retrofit.Const;
import ru.startandroid.retrofit.Const.*;
import ru.startandroid.retrofit.Model.Contributor;
import ru.startandroid.retrofit.Model.Edges;
import ru.startandroid.retrofit.Model.LastActions;
import ru.startandroid.retrofit.Model.Member;
import ru.startandroid.retrofit.Model.geninvoice.InvoiceMain;
import ru.startandroid.retrofit.Model.routes.Routes;

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



//    @GET("http://pls-test.kazpost.kz/api/mobile/history")
//    Call<List<LastActions>> getLastActions(@Header("Authorization") String contentRange);

    @GET("api/mobile/history")
//    @GET("api/security/membership-info")
    Call<ResponseBody> getLastActions(

    );

//    http://pls-test.kazpost.kz/api/mobile/flights

    @GET("api/security/membership-info")
    Call<Member> getMembershipInfo(

    );


    @GET("api/mobile/flights")
    Call<Routes> getRoutesInfo(

    );



    @GET("api/mobile/general-invoices")
    Call<InvoiceMain> getGeneralInvoice(

    );

//    @GET("api/mobile/list-for-vpn")
//    Call<> getListForVPN(
//
//    );
}