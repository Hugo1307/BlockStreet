package dev.hugog.minecraft.blockstreet.data.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;

public interface Dao<T> {

    T toEntity();

    Dao<T> fromEntity(T entity);

    default JsonObject toJson() {
        return new ObjectMapper().convertValue(this, JsonObject.class);
    }

}
