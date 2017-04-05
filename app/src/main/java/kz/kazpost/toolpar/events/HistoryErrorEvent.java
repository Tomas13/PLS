package kz.kazpost.toolpar.events;

/**
 * Created by root on 1/25/17.
 */

public class HistoryErrorEvent {

    public Throwable error;

    public HistoryErrorEvent(Throwable error) {
        this.error = error;
    }
}
