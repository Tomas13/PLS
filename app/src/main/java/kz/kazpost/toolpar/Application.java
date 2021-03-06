package kz.kazpost.toolpar;

import android.content.Context;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import java.io.File;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import kz.kazpost.toolpar.data.DataManager;
import kz.kazpost.toolpar.di.component.ApplicationComponent;
import kz.kazpost.toolpar.di.component.DaggerApplicationComponent;
import kz.kazpost.toolpar.di.module.ApplicationModule;
import rx.Subscription;

/**
 * Created by root on 12/29/16.
 */

public class Application extends MultiDexApplication {
    private Subscription subscription;
    public RealmConfiguration realmConfiguration;

    private FirebaseAnalytics mFirebaseAnalytics;

    private ApplicationComponent mApplicationComponent;

    @Override
    protected void attachBaseContext(Context base) {
        MultiDex.install(this);
        super.attachBaseContext(base);
    }

    @Inject
    DataManager dataManager;

    @Override
    public void onCreate() {
        super.onCreate();

        AppJobManager.getJobManager(this);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this)).build();

        mApplicationComponent.inject(this);


        Realm.init(this);

        realmConfiguration = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());

    }

    public ApplicationComponent getComponent() {
        return mApplicationComponent;
    }


    public static File getDirectory() {
        final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "UCC" + File.separator);
        root.mkdirs();
        final String fname = Const.CACHE_FILE_NAME;
        final File sdImageMainDirectory = new File(root, fname);
        return sdImageMainDirectory;
    }

    private void LogMessage(String s) {
        Log.d("Main", s);
    }


   /* @Override
    public void onTerminate() {
        super.onTerminate();
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }*/

}
