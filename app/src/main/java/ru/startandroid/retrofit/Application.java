package ru.startandroid.retrofit;

import android.content.Context;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import rx.Subscription;

/**
 * Created by root on 12/29/16.
 */

public class Application extends MultiDexApplication {
    private Subscription subscription;
    public RealmConfiguration realmConfiguration;

    @Override
    protected void attachBaseContext(Context base) {
        MultiDex.install(this);
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        AppJobManager.getJobManager(this);

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
