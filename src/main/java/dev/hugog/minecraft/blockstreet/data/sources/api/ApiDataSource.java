package dev.hugog.minecraft.blockstreet.data.sources.api;

import dev.hugog.minecraft.blockstreet.data.entities.DataEntity;
import dev.hugog.minecraft.blockstreet.data.sources.DataSource;
import okhttp3.Response;

public interface ApiDataSource extends DataSource {

    Response executeRequest();

    DataEntity parseData();

}
