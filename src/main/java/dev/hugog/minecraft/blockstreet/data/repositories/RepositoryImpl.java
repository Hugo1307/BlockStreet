package dev.hugog.minecraft.blockstreet.data.repositories;

import dev.hugog.minecraft.blockstreet.data.sources.DataSource;
import lombok.Getter;


@Getter
public abstract class RepositoryImpl implements Repository {

    protected final DataSource dataSource;

    public RepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

}
