package ru.startandroid.retrofit.view;

import ru.startandroid.retrofit.Model.Member;

/**
 * Created by root on 1/19/17.
 */

public interface NavigationActView {

    void showProgress();

    void hideProgress();

    void getMembershipData(Member member);

    void showMemberEmptyData();

    void showMemberError(Throwable throwable);

}
