package kz.kazpost.toolpar.login;

import android.content.Context;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.inject.Inject;

import kz.kazpost.toolpar.Model.login.LoginResponse;
import kz.kazpost.toolpar.data.AppDataManager;
import kz.kazpost.toolpar.data.DataManager;
import kz.kazpost.toolpar.data.network.ApiHelper;
import kz.kazpost.toolpar.data.prefs.PreferencesHelper;
import kz.kazpost.toolpar.presenter.LoginPresenter;
import kz.kazpost.toolpar.presenter.LoginPresenterImpl;
import kz.kazpost.toolpar.ui.LoginActivity;
import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.schedulers.Schedulers;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;

/**
 * Created by root on 10/9/17.
 */

public class LoginPresenterTest {

    @Inject
    PreferencesHelper preferencesHelper;
    @Inject
    Context context;
    @Inject
    ApiHelper apiHelper;

    @Mock
    private
    DataManager dataManager;

    @Mock
    LoginActivity loginActivity;

    //    @Inject
    @Mock
    LoginPresenter loginPresenter;

    @Before
    public void setupLoginPresenter() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        RxAndroidPlugins.getInstance().registerSchedulersHook(new RxAndroidSchedulersHook() {
            @Override
            public Scheduler getMainThreadScheduler() {
                return Schedulers.immediate();
            }
        });


        loginPresenter = new LoginPresenterImpl(dataManager);
    }

    @Test
    public void Login() {
        loginPresenter.postLogin("test.ast17.rpo1", "demo");

        dataManager.saveUsername("test.ast17.rpo1");

        verify(dataManager).saveUsername("test.ast17.rpo1");
//        verify(dataManager).saveUsername("test.ast17.rpo1");
//        verify(loginPresenter).savePassword("demo");
    }
}
