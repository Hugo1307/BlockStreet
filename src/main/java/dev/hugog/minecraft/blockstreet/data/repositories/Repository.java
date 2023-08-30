package dev.hugog.minecraft.blockstreet.data.repositories;

import dev.hugog.minecraft.blockstreet.data.entities.DataEntity;

import java.util.Optional;

public interface Repository<T, K extends DataEntity> {

    Optional<K> getById(T id);

    boolean exists(T id);

    void save(K dataEntity);

    void delete(T id);

    /**
     * Verifies if the data source is valid for the repository.
     * Each repository has its own data source, so this method
     * is used to verify if the data source is valid or not.
     *
     * @return true if the data source is valid, false otherwise.
     */
    boolean isDataSourceValid();

}
