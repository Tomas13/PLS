package kz.kazpost.toolpar.jobs;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import kz.kazpost.toolpar.Interface.GitHubService;
import kz.kazpost.toolpar.Model.login.LoginResponse;
import kz.kazpost.toolpar.events.AccessTokenCollateEvent;
import kz.kazpost.toolpar.events.AccessTokenErrorEvent;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static kz.kazpost.toolpar.Const.ACCESS_TOKEN_PRIORITY;
import static kz.kazpost.toolpar.Const.BASE_URL;
import static kz.kazpost.toolpar.Const.REFRESH_TOKEN;
import static kz.kazpost.toolpar.Const.TOKEN_SHARED_PREF;
import static kz.kazpost.toolpar.utils.Singleton.getUserClient;

/**
 * Created by root on 3/29/17.
 */

public class GetAccessTokenCollateJob extends Job {

    public GetAccessTokenCollateJob() {
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

        SharedPreferences pref1 = getApplicationContext().getSharedPreferences(TOKEN_SHARED_PREF, 0);

        String accessToken = pref1.getString(REFRESH_TOKEN, "0");

        Observable<LoginResponse> callCreate =
                gitHubServ.postAccess(
                        "no-cache",
                        "refresh_token",
                        "toolpar-mobile",
                        accessToken);

        callCreate
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {

                            EventBus.getDefault().post(new AccessTokenCollateEvent(response));


                        },
                        throwable -> {

                            if (throwable.getMessage().equals("HTTP 401 Unauthorized")) {

                                EventBus.getDefault().post(new AccessTokenErrorEvent(throwable.getMessage()));

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
