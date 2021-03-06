package kz.kazpost.toolpar.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import kz.kazpost.toolpar.Interface.GitHubService;
import kz.kazpost.toolpar.Model.routes.Routes;
import kz.kazpost.toolpar.events.LoadRoutesEvent;
import kz.kazpost.toolpar.events.RoutesEmptyEvent;
import kz.kazpost.toolpar.events.RoutesEventError;
import kz.kazpost.toolpar.models.ErrorRequestException;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static kz.kazpost.toolpar.Const.AccessTokenConst;
import static kz.kazpost.toolpar.Const.BASE_URL;
import static kz.kazpost.toolpar.Const.LOAD_ROUTES_PRIORITY;
import static kz.kazpost.toolpar.utils.Singleton.getUserClient;

/**
 * Created by root on 2/9/17.
 */

public class LoadRoutesJob extends Job {

    public LoadRoutesJob() {
        super(new Params(LOAD_ROUTES_PRIORITY).requireNetwork().persist());
    }

    @Override
    public void onAdded() {
        Log.d("LoadRo", " added");
    }

    @Override
    public void onRun() throws Throwable {

        Retrofit retrofitRoutes = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(getUserClient("Bearer " + AccessTokenConst))
                .build();

        GitHubService gitHubServ = retrofitRoutes.create(GitHubService.class);

        Observable<Routes> callEdges = gitHubServ.getRoutesInfo();

        callEdges
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (response.getStatus().equals("success")) {

                                EventBus.getDefault().postSticky(new LoadRoutesEvent(response));
                            } else {
                                EventBus.getDefault().postSticky(new RoutesEmptyEvent());
                            }
                        },
                        throwable -> {
                            EventBus.getDefault().postSticky(new RoutesEventError(throwable));
                        });
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
//        return RetryConstraint.createExponentialBackoff(runCount, 1000);

        if (throwable instanceof ErrorRequestException) {
            ErrorRequestException error = (ErrorRequestException) throwable;
            int statusCode = error.getResponse().raw().code();
            if (statusCode >= 400 && statusCode < 500) {
                return RetryConstraint.CANCEL;
            }
        }

        return RetryConstraint.RETRY;
    }
}
