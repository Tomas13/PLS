package ru.startandroid.retrofit.events;

/**
 * Created by root on 2/8/17.
 */

public class CollateErrorEvent {

    public Throwable error;

    public CollateErrorEvent(Throwable error) {
        this.error = error;
    }
}
