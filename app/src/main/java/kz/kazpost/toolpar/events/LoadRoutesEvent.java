package kz.kazpost.toolpar.events;

import kz.kazpost.toolpar.Model.routes.Routes;

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
