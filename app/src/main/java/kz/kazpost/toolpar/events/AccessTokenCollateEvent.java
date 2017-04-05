package kz.kazpost.toolpar.events;

import kz.kazpost.toolpar.Model.login.LoginResponse;

/**
 * Created by root on 3/29/17.
 */

public class AccessTokenCollateEvent {
    private LoginResponse loginResponse;

    public AccessTokenCollateEvent(LoginResponse loginResponse) {
        this.loginResponse = loginResponse;
    }

    public LoginResponse getLoginResponse() {
        return loginResponse;
    }

}
