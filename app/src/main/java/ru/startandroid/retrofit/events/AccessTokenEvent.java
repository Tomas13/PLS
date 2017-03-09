package ru.startandroid.retrofit.events;

import ru.startandroid.retrofit.Model.login.LoginResponse;

/**
 * Created by root on 3/9/17.
 */

public class AccessTokenEvent {
    private LoginResponse loginResponse;

    public AccessTokenEvent(LoginResponse loginResponse) {
        this.loginResponse = loginResponse;
    }

    public LoginResponse getLoginResponse() {
        return loginResponse;
    }
}
