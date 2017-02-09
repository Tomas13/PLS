package ru.startandroid.retrofit.events;

import ru.startandroid.retrofit.Model.routes.Routes;

/**
 * Created by root on 2/9/17.
 */

public class LoadRoutesEvent {

    private Routes routes;

    public LoadRoutesEvent(Routes routes) {
        this.routes = routes;
    }

    public Routes getRoutes() {
        return routes;
    }
}
