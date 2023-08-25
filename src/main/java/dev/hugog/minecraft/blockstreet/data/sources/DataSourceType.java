package dev.hugog.minecraft.blockstreet.data.sources;

import dev.hugog.minecraft.blockstreet.data.sources.api.ApiDataSource;
import lombok.Getter;

@Getter
public enum DataSourceType {

    YML(DataSource.class),
    API(ApiDataSource.class),
    DATABASE(DataSource.class);

    private final Class<? extends DataSource> dataSourceClass;

    DataSourceType(Class<? extends DataSource> dataSourceClass) {
        this.dataSourceClass = dataSourceClass;
    }

}
