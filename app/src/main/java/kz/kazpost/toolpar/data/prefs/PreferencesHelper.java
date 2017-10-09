package kz.kazpost.toolpar.data.prefs;

/**
 * Created by root on 10/2/17.
 */

public interface PreferencesHelper {

    void savePassword(String password);
    void saveUsername(String username);
    void saveAccessToken(String accessToken);
    void saveRefreshToken(String refreshToken);
    String getAccessToken();
    String getRefreshToken();
    String getUsername();
    String getPassword();

    boolean hasRefreshToken();
}
