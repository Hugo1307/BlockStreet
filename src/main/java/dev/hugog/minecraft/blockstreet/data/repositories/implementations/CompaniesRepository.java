package dev.hugog.minecraft.blockstreet.data.repositories.implementations;

import dev.hugog.minecraft.blockstreet.data.entities.CompanyEntity;
import dev.hugog.minecraft.blockstreet.data.repositories.Repository;
import dev.hugog.minecraft.blockstreet.data.sources.DataSource;
import dev.hugog.minecraft.blockstreet.data.sources.yml.implementations.CompaniesYml;
import dev.hugog.minecraft.blockstreet.enums.DataFilePath;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Objects;
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
        return dataSource.exists(DataFilePath.COMPANIES.getFullPath(String.valueOf(id)));

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

    public List<CompanyEntity> getCompaniesByIdInterval(int startingId, int endingId) {

        if (!isDataSourceValid()) return null;

        CompaniesYml companiesDataSourceYML = (CompaniesYml) dataSource;
        List<String> allowedIds = companiesDataSourceYML.getAllIds(DataFilePath.COMPANIES.getDataPath()).stream()
                .filter(id -> Integer.parseInt(id) >= startingId && Integer.parseInt(id) <= endingId)
                .collect(Collectors.toList());

        return allowedIds.stream()
                .map(id -> companiesDataSourceYML.load(DataFilePath.COMPANIES.getFullPathById(id), CompanyEntity.class))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

    }

    @Override
    public boolean isDataSourceValid() {
        return dataSource instanceof CompaniesYml;
    }

}
