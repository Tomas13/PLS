package kz.kazpost.toolpar.events;

import kz.kazpost.toolpar.Model.CreateResponse;

/**
 * Created by root on 2/9/17.
 */

public class InvoiceEvent {
    private CreateResponse createResponse;

    public InvoiceEvent(CreateResponse createResponse) {
        this.createResponse = createResponse;
    }

    public CreateResponse getCreateResponse() {
        return createResponse;
    }
}
