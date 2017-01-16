package ru.startandroid.retrofit.presenter;

/**
 * Created by root on 1/16/17.
 */

public interface HistoryPresenter {

    void onResume();

    void onDestroy();

    void loadHistory();
}
