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


import dagger.Component;
import kz.kazpost.toolpar.di.PerActivity;
import kz.kazpost.toolpar.di.module.ActivityModule;
import kz.kazpost.toolpar.ui.HistoryFragment;
import kz.kazpost.toolpar.ui.LoginActivity;
import kz.kazpost.toolpar.view.LoginView;

/**
 * Created by janisharali on 27/01/17.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(HistoryFragment fragment);

    void inject(LoginActivity loginActivity);

//    void inject(ChooseIndexActivity activity);


}
