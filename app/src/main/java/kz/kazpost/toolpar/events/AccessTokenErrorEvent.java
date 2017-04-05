package kz.kazpost.toolpar.events;

/**
 * Created by root on 3/9/17.
 */

public class AccessTokenErrorEvent {
    String error;

    public AccessTokenErrorEvent(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
