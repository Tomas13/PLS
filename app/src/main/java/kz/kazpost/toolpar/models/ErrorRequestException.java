package kz.kazpost.toolpar.models;


import retrofit2.Response;

public class ErrorRequestException extends Exception {

    private Response mResponse;

    public ErrorRequestException(Response response) {
        mResponse = response;
    }

    public Response getResponse() {
        return mResponse;
    }
}
