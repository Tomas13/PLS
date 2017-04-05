package kz.kazpost.toolpar.events;

/**
 * Created by root on 2/9/17.
 */

public class RoutesEventError {
    public Throwable error;

    public RoutesEventError(Throwable error) {
        this.error = error;
    }
}
