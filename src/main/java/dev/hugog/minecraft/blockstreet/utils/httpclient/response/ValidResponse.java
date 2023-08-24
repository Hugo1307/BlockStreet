package dev.hugog.minecraft.blockstreet.utils.httpclient.response;

import java.util.List;

public class ValidResponse extends HTTPResponse {

    public ValidResponse(int requestResponseCode, List<String> requestResponseMessage) {
        super(requestResponseCode, requestResponseMessage);
    }

}
