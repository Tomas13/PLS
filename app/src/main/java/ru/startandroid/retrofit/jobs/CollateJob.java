package ru.startandroid.retrofit.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;

import org.greenrobot.eventbus.EventBus;

import ru.startandroid.retrofit.Model.IdsCollate;
import ru.startandroid.retrofit.Model.collatedestination.CollateResponse;
import ru.startandroid.retrofit.events.CollateErrorEvent;
import ru.startandroid.retrofit.events.CollateEvent;
import ru.startandroid.retrofit.models.NetworkService;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static ru.startandroid.retrofit.Const.COLLATE_PRIORITY;

/**
 * Created by root on 2/8/17.
 */

public class CollateJob extends Job {

    private IdsCollate idsCollate;

    public CollateJob(IdsCollate idsCollate) {
        super(new Params(COLLATE_PRIORITY).requireNetwork().persist());
        this.idsCollate = idsCollate;
    }


    @Override
    public void onAdded() {

    }


    @Override
    public void onRun() throws Throwable {

        Observable<CollateResponse> postCollateDestinationLists =
                NetworkService.getApiService().postCollateDestinationLists(idsCollate);


        postCollateDestinationLists
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (response.getStatus().equals("success")) {

                                EventBus.getDefault().post(new CollateEvent(response));

                            }
                        },
                        throwable -> {

                            EventBus.getDefault().post(new CollateErrorEvent(throwable));

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
