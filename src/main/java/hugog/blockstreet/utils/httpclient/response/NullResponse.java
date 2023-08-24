package hugog.blockstreet.utils.httpclient.response;

import java.util.Collections;

public class NullResponse extends HTTPResponse {

    public NullResponse() {
        super(0, Collections.emptyList());
    }

}
