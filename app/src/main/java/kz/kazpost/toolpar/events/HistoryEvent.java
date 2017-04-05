package kz.kazpost.toolpar.events;

import kz.kazpost.toolpar.Model.LastActions;

/**
 * Created by root on 1/25/17.
 */

public class HistoryEvent {

    private LastActions history;

    public HistoryEvent(LastActions history) {
        this.history = history;
    }


    public LastActions getHistory() {
        return history;
    }
}
