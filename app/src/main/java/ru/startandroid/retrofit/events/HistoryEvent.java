package ru.startandroid.retrofit.events;

import ru.startandroid.retrofit.Model.History;
import ru.startandroid.retrofit.Model.LastActions;

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
