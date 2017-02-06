package ru.startandroid.retrofit.Interface;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.startandroid.retrofit.Model.BodyForCreateInvoice;
import ru.startandroid.retrofit.Model.BodyForCreateInvoiceWithout;
import ru.startandroid.retrofit.Model.Contributor;
import ru.startandroid.retrofit.Model.CreateResponse;
import ru.startandroid.retrofit.Model.IdsCollate;
import ru.startandroid.retrofit.Model.LastActions;
import ru.startandroid.retrofit.Model.Member;
import ru.startandroid.retrofit.Model.acceptgen.Example;
import ru.startandroid.retrofit.Model.collatedestination.CollateResponse;
import ru.startandroid.retrofit.Model.destinationlist.ResponseDestinationList;
import ru.startandroid.retrofit.Model.geninvoice.InvoiceMain;
import ru.startandroid.retrofit.Model.routes.Routes;
import rx.Observable;

import static ru.startandroid.retrofit.utils.KeycloakHelper.LOGOUT;

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


//    @GET("http://pls-test.kazpost.kz/api/mobile/history")
//    Call<List<LastActions>> getLastActions(@Header("Authorization") String contentRange);

    @GET("api/mobile/history")
//    @GET("api/security/membership-info")
    Observable<LastActions> getLastActions(

    );

//    http://pls-test.kazpost.kz/api/mobile/flights

    @GET("api/security/membership-info")
    Observable<Member> getMembershipInfo(

    );


    @GET("api/mobile/flights-v2")
    Observable<Routes> getRoutesInfo(

    );

    @GET("api/mobile/flights")
    Call<Routes> getRoutesInfoCall(

    );

    //Принятие общей накладной
    @GET("api/mobile/general-invoices")
    Observable<InvoiceMain> getGeneralInvoice(

    );



    @GET("/api/mobile/accept-general-invoice-two")
    Observable<Example> acceptGeneralInvoiceNew(
            @Query("id") Long genInvoiceID
    );



    //    Получение списка s накладных, которые уже извлекли из О
    @GET("/api/mobile/destination-lists")
    Observable<ResponseDestinationList> getDestionationLists(
    );


    @POST("/api/mobile/collate-destination-lists")
    @Headers("Content-Type: text/plain")
    Observable<CollateResponse> postCollateDestinationLists(
            @Body IdsCollate idsCollate
            );




    @POST("/api/mobile/create-general-invoice")
    @Headers("Content-Type: text/plain")
    Observable<CreateResponse> postCreateGeneralInvoice(
            @Body BodyForCreateInvoice bodyForCreateInvoice
    );


    @POST("/api/mobile/create-general-invoice")
    @Headers("Content-Type: text/plain")
    Observable<CreateResponse> postCreateGeneralInvoiceWithout(
            @Body BodyForCreateInvoiceWithout bodyForCreateInvoiceWithout
    );

    //    Получение списка s накладных, которые уже извлекли из О
    @GET("/api/mobile/list-for-vpn")
    Observable<CollateResponse> getListForVpn(

    );



    @GET(LOGOUT)
    Call<ResponseBody> getLogout();

}