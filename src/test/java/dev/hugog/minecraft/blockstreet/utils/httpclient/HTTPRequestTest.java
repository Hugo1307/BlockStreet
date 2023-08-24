package dev.hugog.minecraft.blockstreet.utils.httpclient;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class HTTPRequestTest {

    private HTTPRequest httpRequestSpy;

    @Before
    public void setUp() {

        HTTPRequest.Builder builderMock = mock(HTTPRequest.Builder.class);

        when(builderMock.getURI()).thenReturn("https://api.agify.io/?name=meelad");
        when(builderMock.getRequestType()).thenReturn(HTTPRequestType.GET);

        httpRequestSpy = spy(new HTTPRequest(builderMock));

    }

    @Test
    public void testSendRequest() {

        assertEquals(200, httpRequestSpy.sendRequest().getRequestResponseCode());
        assertEquals("{\"name\":\"meelad\",\"age\":29,\"count\":21}", httpRequestSpy.sendRequest().getRequestResponseMessage().get(0));

    }

}