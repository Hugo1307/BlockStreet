package dev.hugog.minecraft.blockstreet.data.repositories.implementations;

import dev.hugog.minecraft.blockstreet.data.entities.PlayerEntity;
import dev.hugog.minecraft.blockstreet.data.repositories.Repository;
import dev.hugog.minecraft.blockstreet.data.sources.DataSource;
import dev.hugog.minecraft.blockstreet.data.sources.yml.implementations.PlayersYml;
import dev.hugog.minecraft.blockstreet.enums.DataFilePath;

import java.util.*;

public class PlayersRepository implements Repository<UUID, PlayerEntity> {

    private final DataSource<PlayerEntity> dataSource;

    public PlayersRepository(DataSource<PlayerEntity> dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<PlayerEntity> getById(UUID playerUniqueId) {

        if (!isDataSourceValid()) return Optional.empty();

        if (!exists(playerUniqueId)) return Optional.empty();

        return Optional.ofNullable(dataSource.load(DataFilePath.PLAYERS.getFullPathById(playerUniqueId.toString()), PlayerEntity.class));

    }

    @Override
    public boolean exists(UUID playerUniqueId) {

        if (!isDataSourceValid()) return false;

        String uniqueIdAsString = playerUniqueId.toString();
        return dataSource.exists(DataFilePath.PLAYERS.getFullPathById(uniqueIdAsString));

    }

    @Override
    public void save(PlayerEntity playerToUpdate) {

        if (!isDataSourceValid()) return;

        dataSource.save(DataFilePath.PLAYERS.getFullPathById(playerToUpdate.getUniqueId()), playerToUpdate);

    }

    @Override
    public void delete(UUID uniqueId) {

        if (!isDataSourceValid()) return;

        dataSource.delete(DataFilePath.PLAYERS.getFullPathById(uniqueId.toString()));

    }

    @Override
    public boolean isDataSourceValid() {
        // TODO: Update verification with other data sources.
        return dataSource instanceof PlayersYml;
    }

}

