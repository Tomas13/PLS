package kz.kazpost.toolpar.jobs;

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
import kz.kazpost.toolpar.Model.IdsCollate;
import kz.kazpost.toolpar.Model.collatedestination.CollateResponse;
import kz.kazpost.toolpar.events.CollateErrorEvent;
import kz.kazpost.toolpar.events.CollateEvent;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static kz.kazpost.toolpar.Const.AccessTokenConst;
import static kz.kazpost.toolpar.Const.BASE_URL;
import static kz.kazpost.toolpar.Const.COLLATE_PRIORITY;
import static kz.kazpost.toolpar.utils.Singleton.getUserClient;

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


        Retrofit retrofitRoutes = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(getUserClient("Bearer " + AccessTokenConst))
                .build();

        GitHubService gitHubServ = retrofitRoutes.create(GitHubService.class);

        Observable<CollateResponse> postCollateDestinationLists =
                gitHubServ.postCollateDestinationLists(idsCollate);


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
