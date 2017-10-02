package kz.kazpost.toolpar.data.network;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by root on 10/2/17.
 */

@Singleton
public class AppApiHelper implements ApiHelper {

    @Inject
    NetworkService networkService;

    @Inject
    public AppApiHelper() {
    }
}
