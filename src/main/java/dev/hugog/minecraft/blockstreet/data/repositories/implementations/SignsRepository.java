package dev.hugog.minecraft.blockstreet.data.repositories.implementations;

import dev.hugog.minecraft.blockstreet.data.entities.SignEntity;
import dev.hugog.minecraft.blockstreet.data.repositories.Repository;
import dev.hugog.minecraft.blockstreet.data.sources.DataSource;
import dev.hugog.minecraft.blockstreet.data.sources.yml.YmlDataSourceImpl;
import dev.hugog.minecraft.blockstreet.data.sources.yml.implementations.SignsYml;
import dev.hugog.minecraft.blockstreet.enums.DataFilePath;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class SignsRepository implements Repository<Integer, SignEntity> {

    private final DataSource<SignEntity> dataSource;

    public SignsRepository(DataSource<SignEntity> dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<SignEntity> getById(Integer id) {

        if (!isDataSourceValid()) return Optional.empty();

        if (!exists(id)) return Optional.empty();

        return Optional.ofNullable(dataSource.load(DataFilePath.SIGNS.getFullPathById(id.toString()), SignEntity.class));

    }

    @Override
    public boolean exists(Integer id) {

        if (!isDataSourceValid()) return false;
        return dataSource.exists(DataFilePath.SIGNS.getFullPathById(id.toString()));

    }

    @Override
    public void save(SignEntity dataEntity) {

        if (!isDataSourceValid()) return;
        dataSource.save(DataFilePath.SIGNS.getFullPathById(String.valueOf(dataEntity.getId())), dataEntity);

    }

    @Override
    public void delete(Integer id) {

        if (!isDataSourceValid()) return;
        dataSource.delete(DataFilePath.SIGNS.getFullPathById(id.toString()));

    }

    @Override
    public boolean isDataSourceValid() {
        return dataSource instanceof SignsYml;
    }

    public List<SignEntity> getSignsByCompanyId(long companyId) {

        if (!isDataSourceValid()) return null;

        return getAllIds().stream()
                .map(signId -> getById(signId).orElse(null))
                .filter(Objects::nonNull)
                .filter(signEntity -> signEntity.getCompanyId() == companyId)
                .collect(Collectors.toList());

    }

    public List<Integer> getAllIds() {

        if (!isDataSourceValid()) return null;

        return ((YmlDataSourceImpl<?>) dataSource).getAllIds(DataFilePath.SIGNS.getDataPath())
                .stream()
                .filter(stringId -> stringId.matches("\\d+"))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

    }

    public Long getNextId() {

        if (!isDataSourceValid()) return null;

        if (dataSource instanceof YmlDataSourceImpl) {
            return ((YmlDataSourceImpl<?>) dataSource).getNextId(DataFilePath.SIGNS.getDataPath());
        }

        return null;

    }

}
