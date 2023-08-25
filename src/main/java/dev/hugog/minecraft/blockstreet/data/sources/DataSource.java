package dev.hugog.minecraft.blockstreet.data.sources;

import dev.hugog.minecraft.blockstreet.data.PluginData;
import dev.hugog.minecraft.blockstreet.data.entities.DataEntity;

public interface DataSource<T extends DataEntity> extends PluginData {

    T load(Class<T> dataEntityClass);

    void save(T data);

}
