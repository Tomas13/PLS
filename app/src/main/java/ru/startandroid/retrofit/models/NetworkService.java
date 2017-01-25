package ru.startandroid.retrofit.models;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.startandroid.retrofit.Const;
import ru.startandroid.retrofit.Interface.GitHubService;

import static ru.startandroid.retrofit.Const.BASE_URL;
import static ru.startandroid.retrofit.utils.Singleton.getUserClient;

/**
 * Created by root on 1/16/17.
 */

public class NetworkService {

    private Retrofit retrofitRoutes;
    private static GitHubService gitHubServ;

    public NetworkService() {
        retrofitRoutes = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(getUserClient(Const.Token))
                .build();

        gitHubServ = retrofitRoutes.create(GitHubService.class);
    }

    public static GitHubService getApiService() {
        return gitHubServ;
    }

    public static <T> T performRequest(Call<T> request) throws IOException, ErrorRequestException {
        retrofit2.Response<T> response = request.execute();

        if (response == null || !response.isSuccessful() || response.errorBody() != null) {
            throw new ErrorRequestException(response);
        }

        return response.body();
    }


}
