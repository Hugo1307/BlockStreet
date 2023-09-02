package dev.hugog.minecraft.blockstreet.api.endpoints;

import java.io.IOException;

public interface ApiEndpoint<T> {

    ApiEndpoint<?> executeRequest();

    T parseData() throws IOException;

}
