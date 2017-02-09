package ru.startandroid.retrofit.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;

import org.greenrobot.eventbus.EventBus;

import ru.startandroid.retrofit.Model.geninvoice.InvoiceMain;
import ru.startandroid.retrofit.events.AcceptEmptyEvent;
import ru.startandroid.retrofit.events.LoadGeneralErrorEvent;
import ru.startandroid.retrofit.events.ShowGeneralInvoiceEvent;
import ru.startandroid.retrofit.models.NetworkService;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static ru.startandroid.retrofit.Const.CREATE_INVOICE_PRIORITY;
import static ru.startandroid.retrofit.Const.LOAD_INVOICE_PRIORITY;

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
        Observable<InvoiceMain> callEdges =
                NetworkService.getApiService().getGeneralInvoice();

        callEdges
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (response.getStatus().equals("success")) {

                                EventBus.getDefault().post(new ShowGeneralInvoiceEvent(response));
                            } else {

                                EventBus.getDefault().post(new AcceptEmptyEvent(""));

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
