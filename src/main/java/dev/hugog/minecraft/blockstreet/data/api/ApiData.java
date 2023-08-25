package dev.hugog.minecraft.blockstreet.data.api;

import dev.hugog.minecraft.blockstreet.data.PluginData;
import dev.hugog.minecraft.blockstreet.data.entities.DataEntity;
import okhttp3.Response;

public interface ApiData extends PluginData {

    Response executeRequest();

    DataEntity parseData();

}
