package dev.hugog.minecraft.blockstreet.data.repositories.implementations;

import dev.hugog.minecraft.blockstreet.data.entities.CompanyEntity;
import dev.hugog.minecraft.blockstreet.data.repositories.Repository;
import dev.hugog.minecraft.blockstreet.data.sources.DataSource;
import dev.hugog.minecraft.blockstreet.data.sources.yml.YmlDataSourceImpl;
import dev.hugog.minecraft.blockstreet.data.sources.yml.implementations.CompaniesYml;
import dev.hugog.minecraft.blockstreet.enums.DataFilePath;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
public class CompaniesRepository implements Repository<Long, CompanyEntity> {

    private final DataSource<CompanyEntity> dataSource;

    public CompaniesRepository(DataSource<CompanyEntity> dataSource) {
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

        if (!isDataSourceValid()) return null;

        if (dataSource instanceof YmlDataSourceImpl) {
            return ((YmlDataSourceImpl<?>) dataSource).getNextId(DataFilePath.COMPANIES.getDataPath());
        }

        return null;

    }

    @Override
    public boolean isDataSourceValid() {
        return dataSource instanceof CompaniesYml;
    }

}
