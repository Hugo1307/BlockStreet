package dev.hugog.minecraft.blockstreet.data.sources.yml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import dev.hugog.minecraft.blockstreet.data.entities.DataEntity;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class YmlDataSourceImpl<T extends DataEntity> implements YmlDataSource<T> {

    protected final File pluginDataFolder;
    private final ObjectMapper mapper;

    public YmlDataSourceImpl(File pluginDataFolder) {
        this.pluginDataFolder = pluginDataFolder;
        this.mapper = new ObjectMapper(new YAMLFactory());
        this.mapper.findAndRegisterModules();
    }

    @Override
    public T load(String fileName, Class<T> dataEntityClass) {

        if (!exists(fileName)) {
            return null;
        }

        if (fileName == null) {
            log.warn("Unable to load data source for class " + dataEntityClass.getName() + ". Filename is null");
            return null;
        }

        try {
            return mapper.readValue(new File(pluginDataFolder, fileName), dataEntityClass);
        } catch (IOException e) {
            log.warn("Error while loading data from file: " + new File(pluginDataFolder, fileName).getPath(), e);
        }
        return null;
    }

    @Override
    public void save(String fileName, T data) {

        if (fileName == null) {
            log.warn("Unable to save data source for class " + data.getClass().getName() + ". Filename is null");
            return;
        }

        try {
            mapper.writeValue(new File(pluginDataFolder, fileName), data);
        } catch (IOException e) {
            log.warn("Error while saving data to file: " + fileName, e);
        }

    }

    @Override
    public boolean exists(String fileName) {
        return new File(pluginDataFolder, fileName).exists();
    }

    @Override
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void delete(String fileName) {
        new File(pluginDataFolder, fileName).delete();
    }


    @Override
    public List<String> getAllIds(String directoryName) {

        File[] filesInDataDir = new File(pluginDataFolder.getAbsolutePath(), directoryName).listFiles();

        if (filesInDataDir == null) return Collections.emptyList();

        return Arrays.stream(filesInDataDir)
                .map(File::getName)
                .map(fileName -> fileName.substring(0, fileName.lastIndexOf('.')))
                .collect(Collectors.toList());
    }

    @Override
    public Long getNextId(String directoryName) {

        List<String> existentIds = getAllIds(directoryName);

        if (existentIds.isEmpty())
            return 1L;

        boolean allIdsAreParseable = existentIds.stream()
                .filter(stringId -> stringId.matches("\\d+"))
                .count() == existentIds.size();

        if (!allIdsAreParseable) {
            log.warn("Unable to get next id for directory " + directoryName + ". Not all ids are Integer parseable.");
            return null;
        }

        List<Long> parsedIds = existentIds.stream()
                .map(Long::parseLong)
                .sorted()
                .collect(Collectors.toList());

        return parsedIds.get(parsedIds.size() - 1) + 1;

    }

}
