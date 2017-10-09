package kz.kazpost.toolpar.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import kz.kazpost.toolpar.di.ApplicationContext;
import kz.kazpost.toolpar.di.PreferenceInfo;

import static kz.kazpost.toolpar.Const.ACCESS_TOKEN;
import static kz.kazpost.toolpar.Const.PASSWORD;
import static kz.kazpost.toolpar.Const.REFRESH_TOKEN;
import static kz.kazpost.toolpar.Const.USERNAME;

/**
 * Created by root on 10/2/17.
 */

@Singleton
public class AppPreferencesHelper implements PreferencesHelper {

    private final SharedPreferences mPrefs;

    @Override
    public boolean hasRefreshToken() {
        return mPrefs.contains(REFRESH_TOKEN);
    }

    @Inject
    AppPreferencesHelper(@ApplicationContext Context context,
                         @PreferenceInfo String prefFileName) {
        mPrefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
    }

    @Override
    public void saveAccessToken(String accessToken) {
        mPrefs.edit().putString(ACCESS_TOKEN, accessToken).apply();
    }

    @Override
    public void saveRefreshToken(String refreshToken) {
        mPrefs.edit().putString(REFRESH_TOKEN, refreshToken).apply();
    }

    @Override
    public String getAccessToken() {
        return mPrefs.getString(ACCESS_TOKEN, "No Access Token");
    }

    @Override
    public void savePassword(String password) {
        mPrefs.edit().putString(PASSWORD, password).apply();
    }

    @Override
    public void saveUsername(String username) {
        mPrefs.edit().putString(USERNAME, username).apply();
    }

    @Override
    public String getRefreshToken() {
        return mPrefs.getString(REFRESH_TOKEN, "No Refresh Token");
    }

    @Override
    public String getUsername() {
        return mPrefs.getString(USERNAME, "no_username");
    }

    @Override
    public String getPassword() {
        return mPrefs.getString(PASSWORD, "no_password");
    }
}
