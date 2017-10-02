package kz.kazpost.toolpar.presenter;

import kz.kazpost.toolpar.base.MvpPresenter;
import kz.kazpost.toolpar.view.HistoryView;

/**
 * Created by root on 1/16/17.
 */

public interface HistoryPresenter<V extends HistoryView> extends MvpPresenter<V> {


    void loadHistory();

    void unSubscribe();
}
