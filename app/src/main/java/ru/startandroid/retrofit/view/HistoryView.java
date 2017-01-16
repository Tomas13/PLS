package ru.startandroid.retrofit.view;

import ru.startandroid.retrofit.Model.LastActions;

/**
 * Created by root on 1/16/17.
 */

public interface HistoryView {

    void showProgress();

    void hideProgress();

    void showHistoryData(LastActions lastActions);

    void showHistoryEmptyData();

    void showHistoryError(Throwable throwable);
}
