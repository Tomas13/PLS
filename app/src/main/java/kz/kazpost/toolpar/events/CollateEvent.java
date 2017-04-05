package kz.kazpost.toolpar.events;

import kz.kazpost.toolpar.Model.collatedestination.CollateResponse;

/**
 * Created by root on 2/8/17.
 */

public class CollateEvent {
    private CollateResponse collateResponse;

    public CollateEvent(CollateResponse collateResponse) {
        this.collateResponse = collateResponse;
    }

    public CollateResponse getCollateResponse() {
        return collateResponse;
    }
}
