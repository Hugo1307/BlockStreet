package dev.hugog.minecraft.blockstreet.data.sources;

import dev.hugog.minecraft.blockstreet.data.entities.DataEntity;

public interface DataSource<T extends DataEntity> {

    T load(String fileName, Class<T> dataEntityClass);

    void save(String fileName, T data);

    boolean exists(String fileName);

    void delete(String fileName);

}
