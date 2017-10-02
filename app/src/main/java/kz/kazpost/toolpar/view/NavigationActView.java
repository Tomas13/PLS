package kz.kazpost.toolpar.view;

import kz.kazpost.toolpar.Model.Member;
import kz.kazpost.toolpar.base.MvpView;

/**
 * Created by root on 1/19/17.
 */

public interface NavigationActView extends MvpView {

    void showProgress();

    void hideProgress();

    void getMembershipData(Member member);

    void showMemberEmptyData();

    void showMemberError(Throwable throwable);

}
