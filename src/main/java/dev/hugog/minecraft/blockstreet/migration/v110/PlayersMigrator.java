package dev.hugog.minecraft.blockstreet.migration.v110;

import dev.hugog.minecraft.blockstreet.data.dao.CompanyDao;
import dev.hugog.minecraft.blockstreet.data.services.PlayersService;
import dev.hugog.minecraft.blockstreet.migration.Migrator;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class PlayersMigrator implements Migrator {

    private final File pluginDataDirectory;
    private final PlayersService playersService;

    public PlayersMigrator(File pluginDataDirectory, PlayersService playersService) {
        this.pluginDataDirectory = pluginDataDirectory;
        this.playersService = playersService;
    }

    @Override
    @SuppressWarnings({"ConstantConditions", "deprecation"})
    public void migrate() {

        File playersFile = getPlayersFile();
        FileConfiguration playersConfiguration = YamlConfiguration.loadConfiguration(playersFile);

        if (!playersFile.exists()) {
            return;
        }

        Set<String> playersNames = Optional.ofNullable(playersConfiguration.getConfigurationSection("Players"))
                .map(configurationSection -> configurationSection.getKeys(false))
                .orElse(Set.of());

        playersNames.forEach(playerName -> {

            Set<String> companiesIds = playersConfiguration.getConfigurationSection("Players." + playerName + ".Companies").getKeys(false);

            OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);
            UUID playerId = player.getUniqueId();

            companiesIds.forEach(companyId -> {
                int sharesAmount = playersConfiguration.getInt("Players." + playerName + ".Companies." + companyId + ".Amount");
                CompanyDao companyDao = CompanyDao.builder()
                        .id(Integer.parseInt(companyId))
                        .currentSharePrice(0.0)
                        .build();
                playersService.addSharesToPlayer(playerId, companyDao, sharesAmount);
            });

        });

        archiveOldData();

    }

    @Override
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void archiveOldData() {
        getPlayersFile().renameTo(new File(pluginDataDirectory, "players.yml.old"));
    }

    @Override
    public String getOldDataVersion() {
        return "1.1.0";
    }

    private File getPlayersFile() {
        return new File(pluginDataDirectory, "players.yml");
    }

}
