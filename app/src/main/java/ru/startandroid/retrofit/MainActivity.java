package ru.startandroid.retrofit;

import android.databinding.DataBindingUtil;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.startandroid.retrofit.Interface.GitHubService;
import ru.startandroid.retrofit.Model.Contributor;
import ru.startandroid.retrofit.Model.Edges;
import ru.startandroid.retrofit.Model.LastActions;
import ru.startandroid.retrofit.databinding.ActivityMainBinding;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.OnErrorThrowable;
import rx.functions.Func0;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding activityMainBinding;
    String TAG = "Main";
    private Looper backgroundLooper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this,
                R.layout.activity_main);

        BackgroundThread backgroundThread = new BackgroundThread();
        backgroundThread.start();
        backgroundLooper = backgroundThread.getLooper();

    }

    static class BackgroundThread extends HandlerThread {
        BackgroundThread() {
            super("SchedulerSample-BackgroundThread", THREAD_PRIORITY_BACKGROUND);
        }
    }

    static Observable<String> sampleObservable() {
        return Observable.defer(new Func0<Observable<String>>() {
            @Override public Observable<String> call() {
                try {
                    // Do some long running operation
                    Thread.sleep(TimeUnit.SECONDS.toMillis(5));
                } catch (InterruptedException e) {
                    throw OnErrorThrowable.from(e);
                }
                return Observable.just("one", "two", "three", "four", "five");
            }
        });
    }

    public void Fetch(View view){

        Toast.makeText(this, "Started", Toast.LENGTH_SHORT).show();
        GitHubService gitHubService = GitHubService.retrofit.create(GitHubService.class);

        final Call<List<Contributor>> call =
                gitHubService.repoContributors("square", "retrofit");

        call.enqueue(new Callback<List<Contributor>>() {
            @Override
            public void onResponse(Call<List<Contributor>> call, Response<List<Contributor>> response) {
                Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_SHORT).show();

                activityMainBinding.textView.setText(response.body().toString());
            }
            @Override
            public void onFailure(Call<List<Contributor>> call, Throwable t) {
                activityMainBinding.textView.setText("Something went wrong: " + t.getMessage());
            }
        });


    }


    public void FetchEdges(View view){

        GitHubService gitHubServ = GitHubService.retrofitLastActions.create(GitHubService.class);

        final Call<List<LastActions>> callEdges =
                gitHubServ.getLastActions();

        callEdges.enqueue(new Callback<List<LastActions>>() {
            @Override
            public void onResponse(Call<List<LastActions>> call, Response<List<LastActions>> response) {
//                activityMainBinding.textView.setText(response.body().toString());

                Log.d("Main", "got here");
            }

            @Override
            public void onFailure(Call<List<LastActions>> call, Throwable t) {
//                activityMainBinding.textView.setText("Something went wrong: " + t.getMessage());
//                Log.d("Main", t.getMessage());

            }
        });
    }


    public void RunRX(View view){
        onRunSchedulerExampleButtonClicked();
    }


    void onRunSchedulerExampleButtonClicked() {
        sampleObservable()
                // Run on a background thread
                .subscribeOn(AndroidSchedulers.from(backgroundLooper))
                // Be notified on the main thread
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override public void onCompleted() {
                        Log.d(TAG, "onCompleted()");
                    }

                    @Override public void onError(Throwable e) {
                        Log.e(TAG, "onError()", e);
                    }

                    @Override public void onNext(String string) {
                        Log.d(TAG, "onNext(" + string + ")");
                    }
                });
    }
}
