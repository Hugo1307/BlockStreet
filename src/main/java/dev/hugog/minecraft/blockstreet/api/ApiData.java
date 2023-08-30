package dev.hugog.minecraft.blockstreet.api;

import dev.hugog.minecraft.blockstreet.data.entities.DataEntity;
import okhttp3.Response;

public interface ApiData {

    Response executeRequest();

    DataEntity parseData();

}
