package ru.startandroid.retrofit.events;

/**
 * Created by root on 2/9/17.
 */

public class AcceptErrorEvent {

    private Throwable error;

    public AcceptErrorEvent(Throwable error) {
        this.error = error;
    }

}
