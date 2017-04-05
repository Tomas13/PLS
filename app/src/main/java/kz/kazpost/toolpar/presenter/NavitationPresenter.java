package kz.kazpost.toolpar.presenter;

/**
 * Created by root on 1/19/17.
 */

public interface NavitationPresenter {
    void onDestroy();

    void loadMembershipInfo(String accessToken);

    void unSubscribe();
}
