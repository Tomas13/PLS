package kz.kazpost.toolpar.events;

/**
 * Created by root on 2/9/17.
 */

public class AcceptEmptyEvent {

    private String emptyMessage;

    public AcceptEmptyEvent(String emptyMessage) {
        this.emptyMessage = emptyMessage;
    }

    public String getEmptyMessage() {
        return emptyMessage;
    }
}
