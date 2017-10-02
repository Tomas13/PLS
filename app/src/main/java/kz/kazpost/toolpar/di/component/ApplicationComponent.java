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

package kz.kazpost.toolpar.di.component;


import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import kz.kazpost.toolpar.Application;
import kz.kazpost.toolpar.data.DataManager;
import kz.kazpost.toolpar.data.network.NetworkService;
import kz.kazpost.toolpar.di.ApplicationContext;
import kz.kazpost.toolpar.di.module.ApplicationModule;

/**
 * Created by janisharali on 27/01/17.
 */

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(Application app);

    @ApplicationContext
    Context context();

    Application application();

    DataManager getDataManager();

    NetworkService getNetworkService();
}