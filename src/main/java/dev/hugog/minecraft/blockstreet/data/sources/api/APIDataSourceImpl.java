package dev.hugog.minecraft.blockstreet.data.sources.api;

import lombok.extern.log4j.Log4j2;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Log4j2
public abstract class APIDataSourceImpl implements ApiDataSource {

    protected OkHttpClient httpClient;
    protected String apiEndpoint;

    public APIDataSourceImpl(String apiEndpoint) {
        this.httpClient = new OkHttpClient();
        this.apiEndpoint = apiEndpoint;
    }

    protected Request buildRequest() {
        return new Request.Builder()
                .url(this.apiEndpoint)
                .build();
    }

    @Override
    public Response executeRequest() {

        try {
            return httpClient.newCall(buildRequest()).execute();
        } catch (Exception e) {
            log.error("Error while executing request to API endpoint: " + this.apiEndpoint, e);
        }

        return null;

    }

}
