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
import ru.startandroid.retrofit.Model.BodyForCreateInvoiceWithout;
import ru.startandroid.retrofit.Model.CreateResponse;
import ru.startandroid.retrofit.adapter.InvoiceErrorEvent;
import ru.startandroid.retrofit.events.InvoiceEvent;
import ru.startandroid.retrofit.models.NetworkService;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static ru.startandroid.retrofit.Const.AccessTokenConst;
import static ru.startandroid.retrofit.Const.BASE_URL;
import static ru.startandroid.retrofit.Const.CREATE_INVOICE_PRIORITY;
import static ru.startandroid.retrofit.utils.Singleton.getUserClient;

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
