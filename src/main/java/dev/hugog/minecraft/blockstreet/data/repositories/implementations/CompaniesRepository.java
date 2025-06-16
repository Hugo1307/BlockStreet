package dev.hugog.minecraft.blockstreet.data.repositories.implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import dev.hugog.minecraft.blockstreet.data.entities.CompanyEntity;
import dev.hugog.minecraft.blockstreet.data.repositories.Repository;
import dev.hugog.minecraft.blockstreet.data.sources.DataSource;
import dev.hugog.minecraft.blockstreet.data.sources.yml.implementations.CompaniesYml;
import dev.hugog.minecraft.blockstreet.enums.DataFilePath;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CompaniesRepository implements Repository<Long, CompanyEntity> {

    private final File pluginDataFolder;
    private final DataSource<CompanyEntity> dataSource;

    public CompaniesRepository(File pluginDataFolder, DataSource<CompanyEntity> dataSource) {
        this.pluginDataFolder = pluginDataFolder;
        this.dataSource = dataSource;
    }

    @Override
    public Optional<CompanyEntity> getById(Long id) {

        if (!isDataSourceValid()) return Optional.empty();
        return Optional.ofNullable(dataSource.load(DataFilePath.COMPANIES.getFullPathById(String.valueOf(id)), CompanyEntity.class));

    }

    @Override
    public boolean exists(Long id) {

        if (!isDataSourceValid()) return false;
        return dataSource.exists(DataFilePath.COMPANIES.getFullPathById(String.valueOf(id)));

    }

    @Override
    public void save(CompanyEntity dataEntity) {

        if (!isDataSourceValid()) return;
        dataSource.save(DataFilePath.COMPANIES.getFullPathById(String.valueOf(dataEntity.getId())), dataEntity);

    }

    @Override
    public void delete(Long id) {

        if (!isDataSourceValid()) return;
        dataSource.delete(DataFilePath.COMPANIES.getFullPathById(String.valueOf(id)));

    }

    public List<Long> getAllIds() {

        if (!isDataSourceValid()) return null;

        CompaniesYml companiesDataSourceYML = (CompaniesYml) dataSource;

        return companiesDataSourceYML.getAllIds(DataFilePath.COMPANIES.getDataPath())
                .stream()
                .filter(stringId -> stringId.matches("\\d+"))
                .map(Long::parseLong)
                .collect(Collectors.toList());

    }

    public Long getNextId() {
        if (!isDataSourceValid()) {
            return null;
        }

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            return mapper.readValue(new File(this.pluginDataFolder, DataFilePath.COMPANIES.getFullPathById("data")), CompanyDataYml.class).nextId;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void incrementNextId() {
        if (!isDataSourceValid()) return;

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            CompanyDataYml companyDataYml = mapper.readValue(new File(this.pluginDataFolder, DataFilePath.COMPANIES.getFullPathById("data")), CompanyDataYml.class);
            companyDataYml.nextId++;
            mapper.writeValue(new File(this.pluginDataFolder, DataFilePath.COMPANIES.getFullPathById("data")), companyDataYml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isDataSourceValid() {
        return dataSource instanceof CompaniesYml;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    private static class CompanyDataYml {
        private Long nextId;
    }

}
