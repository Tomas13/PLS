package ru.startandroid.retrofit;

import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import io.realm.Realm;
//import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

//import io.realm.Realm;

/**
 * Created by root on 12/29/16.
 */

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initializeWithDefaults(getApplicationContext());

        Realm.init(this);

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());

    }
}
