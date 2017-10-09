/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package kz.kazpost.toolpar.di.module;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import kz.kazpost.toolpar.di.ActivityContext;
import kz.kazpost.toolpar.di.PerActivity;
import kz.kazpost.toolpar.presenter.HistoryPresenter;
import kz.kazpost.toolpar.presenter.HistoryPresenterImpl;
import kz.kazpost.toolpar.presenter.InvoicePresenter;
import kz.kazpost.toolpar.presenter.InvoicePresenterImpl;
import kz.kazpost.toolpar.presenter.LoginPresenter;
import kz.kazpost.toolpar.presenter.LoginPresenterImpl;
import kz.kazpost.toolpar.ui.LoginActivity;
import kz.kazpost.toolpar.view.HistoryView;
import kz.kazpost.toolpar.view.InvoiceView;
import kz.kazpost.toolpar.view.LoginView;

/**
 * Created by janisharali on 27/01/17.
 */

@Module
public class ActivityModule {

    private Activity mActivity;

    public ActivityModule(Activity activity) {
        this.mActivity = activity;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return mActivity;
    }

    @Provides
    Activity provideActivity() {
        return mActivity;
    }

    @Provides
    @PerActivity
    HistoryPresenter<HistoryView> provideHistoryPresenter(HistoryPresenterImpl<HistoryView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    InvoicePresenter<InvoiceView> provideInvoicePresenter(InvoicePresenterImpl<InvoiceView> presenter){
        return presenter;
    }

    @Provides
    @PerActivity
    LoginPresenter<LoginView> provideLoginPresenter(LoginPresenterImpl<LoginView> presenter){
        return presenter;
    }
}
