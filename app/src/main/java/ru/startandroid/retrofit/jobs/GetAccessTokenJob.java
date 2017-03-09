package ru.startandroid.retrofit.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.startandroid.retrofit.Interface.GitHubService;
import ru.startandroid.retrofit.Model.login.LoginResponse;
import ru.startandroid.retrofit.events.AccessTokenErrorEvent;
import ru.startandroid.retrofit.events.AccessTokenEvent;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static ru.startandroid.retrofit.Const.ACCESS_TOKEN_PRIORITY;
import static ru.startandroid.retrofit.Const.BASE_URL;
import static ru.startandroid.retrofit.Const.passwordConst;
import static ru.startandroid.retrofit.Const.usernameConst;
import static ru.startandroid.retrofit.utils.Singleton.getUserClient;

/**
 * Created by root on 3/9/17.
 */

public class GetAccessTokenJob extends Job {

    public GetAccessTokenJob() {
        super(new Params(ACCESS_TOKEN_PRIORITY).requireNetwork().persist());
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        Retrofit retrofitRoutes = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(getUserClient())
                .build();

        GitHubService gitHubServ = retrofitRoutes.create(GitHubService.class);


        Observable<LoginResponse> callCreate =
                gitHubServ.postLogin(
                        "no-cache",
                        "password",
                        "toolpar-mobile",
                        "offline-access",
                        usernameConst,
                        passwordConst);

        callCreate
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {

                            EventBus.getDefault().postSticky(new AccessTokenEvent(response));


                        },
                        throwable -> {

                            if (throwable.getMessage().equals("HTTP 401 Unauthorized")){

                                EventBus.getDefault().postSticky(new AccessTokenErrorEvent(throwable.getMessage()));

                            }

                        });
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return RetryConstraint.createExponentialBackoff(runCount, 1000);

    }
}
