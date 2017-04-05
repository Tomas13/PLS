package kz.kazpost.toolpar.events;

/**
 * Created by root on 2/8/17.
 */

public class CollateErrorEvent {

    private Throwable error;

    public CollateErrorEvent(Throwable error) {
        this.error = error;
    }
}
