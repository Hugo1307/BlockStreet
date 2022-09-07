package hugog.blockstreet.utils.httpclient.response;

import lombok.Getter;

import java.util.List;

@Getter
public abstract class HTTPResponse {

    private final int requestResponseCode;
    private final List<String> requestResponseMessage;

    public HTTPResponse(int requestResponseCode, List<String> requestResponseMessage) {
        this.requestResponseCode = requestResponseCode;
        this.requestResponseMessage = requestResponseMessage;
    }

    public String responseAsString() {
        StringBuilder stringBuilder = new StringBuilder();
        requestResponseMessage.forEach(stringBuilder::append);
        return stringBuilder.toString();
    }

}
