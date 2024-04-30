package dev.hugog.minecraft.blockstreet.api.endpoints;

import lombok.extern.log4j.Log4j2;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Log4j2
public abstract class APIEndpointImpl<T> implements ApiEndpoint<T> {

    protected OkHttpClient httpClient;
    protected String endpointUrl;
    protected Response response;

    public APIEndpointImpl(String endpointUrl) {
        this.httpClient = new OkHttpClient();
        this.endpointUrl = endpointUrl;
    }

    protected Request buildRequest() {
        return new Request.Builder()
                .url(this.endpointUrl)
                .build();
    }

    @Override
    public ApiEndpoint<?> executeRequest() {

        try {
            this.response = httpClient.newCall(buildRequest()).execute();
        } catch (Exception e) {
            log.error("Error while executing request to API endpoint: " + this.endpointUrl, e);
        }

        return this;

    }

}
