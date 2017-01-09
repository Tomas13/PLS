package ru.startandroid.retrofit.utils;

import android.app.Activity;
import android.util.Log;

import org.jboss.aerogear.android.authentication.AbstractAuthenticationModule;
import org.jboss.aerogear.android.authentication.AuthenticationManager;
import org.jboss.aerogear.android.authentication.AuthenticationModule;
import org.jboss.aerogear.android.authentication.digest.HttpDigestAuthenticationConfiguration;
import org.jboss.aerogear.android.authentication.digest.HttpDigestAuthenticationModule;
import org.jboss.aerogear.android.authorization.AuthorizationManager;
import org.jboss.aerogear.android.authorization.AuthzModule;
import org.jboss.aerogear.android.authorization.oauth2.OAuth2AuthorizationConfiguration;
import org.jboss.aerogear.android.core.Callback;
import org.jboss.aerogear.android.pipe.http.HeaderAndBody;
import org.jboss.aerogear.android.pipe.http.HttpException;
import org.jboss.aerogear.android.pipe.module.ModuleFields;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangali on 20.12.16.
 */

public class KeycloakHelper extends HttpDigestAuthenticationConfiguration{


//    private static final String SERVER_URL = "http://pls-test.kazpost.kz";
    private static final String SERVER_URL = "http://172.30.75.218";
    public static final String AUTHZ_URL = SERVER_URL + "/auth";
    private static final String AUTHZ_ENDPOINT = "/realms/toolpar/protocol/openid-connect/auth";
    private static final String AUTHZ_CLIENT_ID = "toolpar-mobile";
    private static final String CLIENT_SECRET = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoRQ3u++H9gcdIyhmDGVeQtT2Yd0WeGZh1zDWFuQ5XUpLmcJ3kpVUCGa8ikZ6zvkrD0hGzHZIl2pLLHnl54UZkDys09lrwbOGl9bdaq+/F4ilGrb5w7C0aVI+HRc8uUDeOB+woaRtKsCw3smXuuVAsFt0x0o1r88/sL1m7CSlAtAfFP45XCc7bIanhCYPfO3W2UCLE0Fkiuz78e0OqYV6qfBEmZqsFBJHIi01ciPn2NfJcd6i+PkyT3d9kslUZJ/6juiJ/cf3nESJ0TG5KAJS4poptzfRTv8mCZw3EZuuiEy6vNHJkVDPx6mjRDoQZ5QQ5YaJUI7o4YUw2WbTRI5NNQIDAQAB";

    private static final String AUTHZ_REDIRECT_URL = "http://oauth2callback";//,"http://oauth2callback","org.aerogear.shoot:/oauth2Callback"];
    private static final String AUTHZ_ACCOUNT_ID = "keycloak-token";
    private static final String MODULE_NAME = "KeyCloakAuthz";
    private static final String TAG = KeycloakHelper.class.getSimpleName();

    private static final String ACCESS_TOKEN_ENDPOINT = "/realms/toolpar/protocol/openid-connect/token";
    private static final String REFRESH_TOKEN_ENDPOINT = "/realms/toolpar/protocol/openid-connect/token";

    public static final String LOGOUT = "/realms/toolpar/protocol/openid-connect/logout";

    static {
        try {
            List<String> scopes = new ArrayList<>();
            scopes.add("offline_access");
            AuthorizationManager.config(MODULE_NAME, OAuth2AuthorizationConfiguration.class)
                    .setBaseURL(new URL(AUTHZ_URL))
                    .setAuthzEndpoint(AUTHZ_ENDPOINT)
                    .setAccessTokenEndpoint(ACCESS_TOKEN_ENDPOINT)
                    .setRefreshEndpoint(REFRESH_TOKEN_ENDPOINT)
                    .setAccountId(AUTHZ_ACCOUNT_ID)
                    .setClientId(AUTHZ_CLIENT_ID)
                    .setClientSecret(CLIENT_SECRET)
                    .setScopes(scopes)
                    .setRedirectURL(AUTHZ_REDIRECT_URL)
                    .asModule();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }




    public static void connect(final Activity activity, final Callback<String> callback) {
        Log.i(TAG, "Run Connect ");
        AuthzModule authzModule = AuthorizationManager.getModule(MODULE_NAME);
        if (!authzModule.isAuthorized()){
            Log.i(TAG, "is Authorized " + authzModule.isAuthorized());

            authzModule.requestAccess(activity, new Callback<String>() {
                @Override
                public void onSuccess(String data) {
                    Log.i(TAG, "loging data " + data);
                    callback.onSuccess(data);
                }

                @Override
                public void onFailure(Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                    callback.onFailure(e);
                }
            });

        }

    }

    public static boolean isConnected() {
        Log.i("MainKeycloak", "check is connected!" + AuthorizationManager.getModule(MODULE_NAME).isAuthorized());
        return AuthorizationManager.getModule(MODULE_NAME).isAuthorized();
    }

    public static void refresh() {
        Log.i(TAG, "refresh is called!");


        Log.d("MainAp", "State" + AuthorizationManager.getModule(MODULE_NAME).refreshAccess());
    }

    public static AuthenticationModule createAuthenticatior() {
        AuthenticationModule module;

        HttpDigestAuthenticationConfiguration authenticationConfiguration =
                null;
        try {
            authenticationConfiguration = AuthenticationManager.config(MODULE_NAME, HttpDigestAuthenticationConfiguration.class)
            .baseURL(new URL(AUTHZ_URL))
            .loginEndpoint("")
            .logoutEndpoint(LOGOUT);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        module = authenticationConfiguration.asModule();


        return module;
    }
}
