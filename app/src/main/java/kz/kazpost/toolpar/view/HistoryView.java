package kz.kazpost.toolpar.view;

import kz.kazpost.toolpar.Model.LastActions;
import kz.kazpost.toolpar.base.MvpView;

/**
 * Created by root on 1/16/17.
 */

public interface HistoryView extends MvpView{

    void showProgress();

    void hideProgress();

    void showHistoryData(LastActions lastActions);

    void showHistoryEmptyData();

    void showHistoryError(Throwable throwable);
}
