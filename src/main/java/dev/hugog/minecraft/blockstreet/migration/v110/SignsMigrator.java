package dev.hugog.minecraft.blockstreet.migration.v110;

import dev.hugog.minecraft.blockstreet.data.services.SignsService;
import dev.hugog.minecraft.blockstreet.migration.Migrator;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Objects;
import java.util.Set;

public class SignsMigrator implements Migrator {

    private final File pluginDataDirectory;
    private final SignsService signsService;

    public SignsMigrator(File pluginDataDirectory, SignsService signsService) {
        this.pluginDataDirectory = pluginDataDirectory;
        this.signsService = signsService;
    }

    @Override
    public void migrate() {

        File signsFile = getSignsFile();
        FileConfiguration signsConfiguration = YamlConfiguration.loadConfiguration(signsFile);

        Set<String> signsIds = Objects.requireNonNull(
                signsConfiguration.getConfigurationSection("Signs")
        ).getKeys(false);

        signsIds.forEach(signId -> {

            Location signLocation = signsConfiguration.getLocation("Signs." + signId + ".Location");
            int companyId = signsConfiguration.getInt("Signs." + signId + ".CompanyId");

            signsService.createSign(companyId, signLocation);

        });

        archiveOldData();

    }

    @Override
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void archiveOldData() {
        getSignsFile().renameTo(new File(pluginDataDirectory, "signs.yml.old"));
    }

    @Override
    public String getOldDataVersion() {
        return "v1.1.0";
    }

    private File getSignsFile() {
        return new File(pluginDataDirectory, "signs.yml");
    }

}
