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
import kz.kazpost.toolpar.Model.geninvoice.InvoiceMain;
import kz.kazpost.toolpar.events.AcceptEmptyEvent;
import kz.kazpost.toolpar.events.LoadGeneralErrorEvent;
import kz.kazpost.toolpar.events.ShowGeneralInvoiceEvent;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static kz.kazpost.toolpar.Const.AccessTokenConst;
import static kz.kazpost.toolpar.Const.BASE_URL;
import static kz.kazpost.toolpar.Const.LOAD_INVOICE_PRIORITY;
import static kz.kazpost.toolpar.utils.Singleton.getUserClient;

/**
 * Created by root on 2/9/17.
 */

public class LoadGeneralInvoiceJob extends Job {
    public LoadGeneralInvoiceJob() {
        super(new Params(LOAD_INVOICE_PRIORITY).requireNetwork().persist());

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


        Observable<InvoiceMain> callEdges =
                gitHubServ.getGeneralInvoice();

        callEdges
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (response.getStatus().equals("success")) {

                                EventBus.getDefault().post(new ShowGeneralInvoiceEvent(response));
                            } else {

                                EventBus.getDefault().post(new AcceptEmptyEvent("Нет накладных для принятия или отправки"));

//                                view.showRoutesEmptyData();
                            }
                        },
                        throwable -> {

                            EventBus.getDefault().post(new LoadGeneralErrorEvent(throwable));
                        });
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return null;
    }
}
