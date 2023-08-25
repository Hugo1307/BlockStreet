package dev.hugog.minecraft.blockstreet.data.sources.yml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import dev.hugog.minecraft.blockstreet.data.entities.DataEntity;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.IOException;

@Log4j2
public class YmlDataSourceImpl<T extends DataEntity> implements YmlDataSource<T> {

    protected final String fileName;
    protected final File pluginDataFolder;
    private final ObjectMapper mapper;

    public YmlDataSourceImpl(String fileName, File pluginDataFolder) {
        this.fileName = fileName;
        this.pluginDataFolder = pluginDataFolder;
        this.mapper = new ObjectMapper(new YAMLFactory());
        this.mapper.findAndRegisterModules();
    }

    @Override
    public T load(Class<T> dataEntityClass) {
        try {
            return mapper.readValue(new File(pluginDataFolder, fileName), dataEntityClass);
        } catch (IOException e) {
            log.warn("Error while loading data from file: " + new File(pluginDataFolder, fileName).getAbsolutePath(), e);
        }
        return null;
    }

    @Override
    public void save(T data) {
        try {
            mapper.writeValue(new File(pluginDataFolder, fileName), data);
        } catch (IOException e) {
            log.warn("Error while saving data to file: " + fileName, e);
        }
    }

}
