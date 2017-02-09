package ru.startandroid.retrofit.events;

/**
 * Created by root on 2/9/17.
 */

public class LoadGeneralErrorEvent {
    private Throwable error;

    public LoadGeneralErrorEvent(Throwable error) {
        this.error = error;
    }

}
