package ru.startandroid.retrofit.models;

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

public class HistoryService {

    private Retrofit retrofitLastActions;
    private GitHubService gitHubServ;

    public HistoryService() {
        retrofitLastActions = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(getUserClient(Const.Token))
                .build();

        gitHubServ = retrofitLastActions.create(GitHubService.class);
    }

    public GitHubService getApiService() {
        return gitHubServ;
    }


}
