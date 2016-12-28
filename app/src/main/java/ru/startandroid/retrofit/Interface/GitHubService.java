package ru.startandroid.retrofit.Interface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.startandroid.retrofit.Model.Contributor;
import ru.startandroid.retrofit.Model.Edges;
import ru.startandroid.retrofit.Model.LastActions;
import ru.startandroid.retrofit.Model.Member;
import ru.startandroid.retrofit.Model.acceptgen.Oinvoice;
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
    Call<LastActions> getLastActions(

    );

//    http://pls-test.kazpost.kz/api/mobile/flights

    @GET("api/security/membership-info")
    Call<Member> getMembershipInfo(

    );


    @GET("api/mobile/flights")
    Call<Routes> getRoutesInfo(

    );


    //Принятие общей накладной
    @GET("api/mobile/general-invoices")
    Call<InvoiceMain> getGeneralInvoice(

    );


//    Получение о накладных
    //    /api/mobile/accept-general-invoice?id={id of general invoice}
    @GET("/api/mobile/accept-general-invoice")
    Call<Oinvoice> acceptGeneralInvoice(
        @Query("id") Long genInvoiceID
    );



//
//    //    Получение о накладных
//    //    /api/mobile/accept-general-invoice?id={id of general invoice}
//    @GET("/api/mobile/accept-general-invoice?id={general_invoice_id}")
//    Call<InvoiceMain> acceptGeneralInvoice(
//            @Path("general_invoice_id") String genInvoiceID
//    );


//    @GET("api/mobile/list-for-vpn")
//    Call<> getListForVPN(
//
//    );
}