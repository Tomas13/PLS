package ru.startandroid.retrofit.jobs;

import android.support.annotation.Nullable;
import android.util.Log;

import com.birbit.android.jobqueue.CancelReason;
import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;

import org.greenrobot.eventbus.EventBus;

import ru.startandroid.retrofit.Model.History;
import ru.startandroid.retrofit.Model.LastActions;
import ru.startandroid.retrofit.events.HistoryErrorEvent;
import ru.startandroid.retrofit.events.HistoryEvent;
import ru.startandroid.retrofit.models.NetworkService;
import ru.startandroid.retrofit.presenter.HistoryPresenter;
import ru.startandroid.retrofit.ui.HistoryFragment;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by root on 1/25/17.
 */

// A job to send a tweet
public class GetHistoryJob extends Job {

    public static final int PRIORITY = 1;

    public GetHistoryJob() {
        // This job requires network connectivity,
        // and should be persisted in case the application exits before job is completed.
        super(new Params(PRIORITY).requireNetwork().persist());

    }

    @Override
    public void onAdded() {
        // Job has been saved to disk.
        // This is a good place to dispatch a UI event to indicate the job will eventually run.
        // In this example, it would be good to update the UI with the newly posted tweet.
        if (history != null) {
            EventBus.getDefault().postSticky(new HistoryEvent(history));
        }
    }

    LastActions history;

    @Override
    public void onRun() throws Throwable {
        // Job logic goes here. In this example, the network call to post to Twitter is done here.
        // All work done here should be synchronous, a job is removed from the queue once
        // onRun() finishes.


        Observable<LastActions> observable = NetworkService.getApiService().getLastActions();

        history = new LastActions();
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(

                        response -> {

                            if (response.getStatus().equals("success")) {
                                history = response;
                                EventBus.getDefault().postSticky(new HistoryEvent(history));

                            }
                        },

                        throwable -> {

                            EventBus.getDefault().postSticky(new HistoryErrorEvent(throwable));

                        });

    }


    @Override
    protected RetryConstraint shouldReRunOnThrowable(Throwable throwable, int runCount,
                                                     int maxRunCount) {
        // An error occurred in onRun.
        // Return value determines whether this job should retry or cancel. You can further
        // specify a backoff strategy or change the job's priority. You can also apply the
        // delay to the whole group to preserve jobs' running order.
        return RetryConstraint.createExponentialBackoff(runCount, 1000);
    }

    @Override
    protected void onCancel(@CancelReason int cancelReason, @Nullable Throwable throwable) {
        // Job has exceeded retry attempts or shouldReRunOnThrowable() has decided to cancel.
    }
}
