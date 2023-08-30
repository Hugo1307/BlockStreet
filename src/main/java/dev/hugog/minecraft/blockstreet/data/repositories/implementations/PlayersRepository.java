package dev.hugog.minecraft.blockstreet.data.repositories.implementations;

import dev.hugog.minecraft.blockstreet.data.entities.PlayerEntity;
import dev.hugog.minecraft.blockstreet.data.repositories.Repository;
import dev.hugog.minecraft.blockstreet.data.sources.DataSource;
import dev.hugog.minecraft.blockstreet.data.sources.yml.implementations.PlayersYml;
import dev.hugog.minecraft.blockstreet.enums.DataFilePath;

import java.util.Optional;
import java.util.UUID;

public class PlayersRepository implements Repository<UUID, PlayerEntity> {

    private final DataSource<PlayerEntity> dataSource;

    public PlayersRepository(DataSource<PlayerEntity> dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<PlayerEntity> getById(UUID playerUniqueId) {

        if (!isDataSourceValid()) return Optional.empty();

        if (!exists(playerUniqueId)) {

            // If the player doesn't exist, then we create the player
            PlayerEntity initialPlayerProfile = new PlayerEntity();
            initialPlayerProfile.setUniqueId(playerUniqueId.toString());

            return Optional.of(initialPlayerProfile);

        }

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
    public void delete(UUID uniqueIdAsString) {

        if (!isDataSourceValid()) return;

        dataSource.delete(DataFilePath.PLAYERS.getFullPathById(uniqueIdAsString.toString()));

    }

    @Override
    public boolean isDataSourceValid() {
        // TODO: Update verification with other data sources.
        return dataSource instanceof PlayersYml;
    }

}

