package ru.startandroid.retrofit.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;

import org.greenrobot.eventbus.EventBus;

import ru.startandroid.retrofit.Model.routes.Routes;
import ru.startandroid.retrofit.events.LoadRoutesEvent;
import ru.startandroid.retrofit.events.RoutesEmptyEvent;
import ru.startandroid.retrofit.events.RoutesEventError;
import ru.startandroid.retrofit.models.NetworkService;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static ru.startandroid.retrofit.Const.LOAD_ROUTES_PRIORITY;

/**
 * Created by root on 2/9/17.
 */

public class LoadRoutesJob extends Job {

    public LoadRoutesJob() {
        super(new Params(LOAD_ROUTES_PRIORITY).requireNetwork().persist());

    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        Observable<Routes> callEdges =
                NetworkService.getApiService().getRoutesInfo();

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
                            EventBus.getDefault().post(new RoutesEventError(throwable));
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
