package dev.hugog.minecraft.blockstreet.data.repositories;

import dev.hugog.minecraft.blockstreet.data.PluginData;
import dev.hugog.minecraft.blockstreet.data.sources.DataSource;
import lombok.Getter;


@Getter
public abstract class RepositoryImpl implements Repository {

    protected final PluginData pluginData;

    public RepositoryImpl(PluginData pluginData) {
        this.pluginData = pluginData;
    }

    /**
     * Verifies if the data source is valid for the repository.
     * Each repository has its own data source, so this method
     * is used to verify if the data source is valid or not.
     *
     * @return true if the data source is valid, false otherwise.
     */
    public abstract boolean verifyDataSource();

}
