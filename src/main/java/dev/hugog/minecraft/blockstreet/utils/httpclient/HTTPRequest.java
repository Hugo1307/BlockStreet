package dev.hugog.minecraft.blockstreet.utils.httpclient;

import dev.hugog.minecraft.blockstreet.utils.httpclient.response.HTTPResponse;
import dev.hugog.minecraft.blockstreet.utils.httpclient.response.NullResponse;
import dev.hugog.minecraft.blockstreet.utils.httpclient.response.ValidResponse;
import lombok.Getter;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class HTTPRequest {

    private final HTTPRequestType requestType;
    private final String URI;
    private final String body;

    protected HTTPRequest(Builder builder) {
        this.requestType = builder.getRequestType();
        this.URI = builder.getURI();
        this.body = builder.getBody();
    }

    public HTTPResponse sendRequest() {

        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(5 * 1000)
                .setConnectionRequestTimeout(5 * 1000)
                .setSocketTimeout(5 * 1000).build();

        try (CloseableHttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(config).build()) {

            HttpRequestBase httpRequestBase;

            switch (requestType) {
                case GET:
                    httpRequestBase = new HttpGet(URI);
                    break;
                case POST:
                    httpRequestBase = new HttpPost(URI);
                    HttpEntity entity = new StringEntity(body);
                    ((HttpPost) httpRequestBase).setEntity(entity);
                    break;
                default:
                    return new NullResponse();
            }

            HttpResponse response = client.execute(httpRequestBase);

            if (response.getStatusLine().getStatusCode() == 200) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                return new ValidResponse(response.getStatusLine().getStatusCode(), rd.lines().collect(Collectors.toList()));
            }else{
                return new NullResponse();
            }

        }catch (IOException ioException) {
            ioException.printStackTrace();
            return new NullResponse();
        }

    }

    @Getter
    public static class Builder {

        private final HTTPRequestType requestType;
        private final String URI;
        private String body;

        public Builder(HTTPRequestType requestType, String URI) {
            this.requestType = requestType;
            this.URI = URI;
        }

        public Builder withBody(String body) {
            this.body = body;
            return this;
        }

        public HTTPRequest build() {
            return new HTTPRequest(this);
        }

    }

}
