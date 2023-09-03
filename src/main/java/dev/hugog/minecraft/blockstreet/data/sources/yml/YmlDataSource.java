package dev.hugog.minecraft.blockstreet.data.sources.yml;

import dev.hugog.minecraft.blockstreet.data.entities.DataEntity;
import dev.hugog.minecraft.blockstreet.data.sources.DataSource;

import java.util.List;

public interface YmlDataSource<T extends DataEntity> extends DataSource<T> {

    List<String> getAllIds(String directoryName);
    Long getNextId(String directoryName);

}
