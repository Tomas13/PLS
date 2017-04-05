package kz.kazpost.toolpar.presenter;

/**
 * Created by root on 1/16/17.
 */

public interface HistoryPresenter {

    void onDestroy();

    void loadHistory();

    void unSubscribe();
}
