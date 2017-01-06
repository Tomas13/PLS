package ru.startandroid.retrofit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import org.jboss.aerogear.android.authorization.oauth2.OAuth2AuthorizationConfiguration;
import org.jboss.aerogear.android.authorization.oauth2.OAuth2AuthzService;
import org.jboss.aerogear.android.authorization.oauth2.OAuth2AuthzSession;
import org.jboss.aerogear.android.authorization.oauth2.OAuth2FetchAccess;
import org.jboss.aerogear.android.core.Callback;

import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import ru.startandroid.retrofit.ui.LoginActivity;
import ru.startandroid.retrofit.ui.NavigationActivity;
import ru.startandroid.retrofit.utils.KeycloakHelper;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static ru.startandroid.retrofit.Const.TOKEN;
import static ru.startandroid.retrofit.Const.TOKEN_SHARED_PREF;
import static ru.startandroid.retrofit.Const.Token;

/**
 * Created by root on 12/29/16.
 */

public class Application extends android.app.Application {

    public RealmConfiguration realmConfiguration;
    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initializeWithDefaults(getApplicationContext());

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
}
