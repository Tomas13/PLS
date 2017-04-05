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
import kz.kazpost.toolpar.Model.BodyForCreateInvoiceWithout;
import kz.kazpost.toolpar.Model.CreateResponse;
import kz.kazpost.toolpar.events.InvoiceErrorEvent;
import kz.kazpost.toolpar.events.InvoiceEvent;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static kz.kazpost.toolpar.Const.AccessTokenConst;
import static kz.kazpost.toolpar.Const.BASE_URL;
import static kz.kazpost.toolpar.Const.CREATE_INVOICE_PRIORITY;
import static kz.kazpost.toolpar.utils.Singleton.getUserClient;

/**
 * Created by root on 2/9/17.
 */

public class PostCreateInvoiceJob extends Job {

    private BodyForCreateInvoiceWithout bodyForCreateInvoiceWithout;

    public PostCreateInvoiceJob(BodyForCreateInvoiceWithout bodyForCreateInvoiceWithout) {
        super(new Params(CREATE_INVOICE_PRIORITY).requireNetwork().persist());
        this.bodyForCreateInvoiceWithout = bodyForCreateInvoiceWithout;
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

        Observable<CreateResponse> callCreate =
                gitHubServ.postCreateGeneralInvoiceWithout(bodyForCreateInvoiceWithout);

        callCreate
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> EventBus.getDefault().post(new InvoiceEvent(response)),
                        throwable -> EventBus.getDefault().post(new InvoiceErrorEvent(throwable))
                );
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return RetryConstraint.createExponentialBackoff(runCount, 1000);

    }
}
