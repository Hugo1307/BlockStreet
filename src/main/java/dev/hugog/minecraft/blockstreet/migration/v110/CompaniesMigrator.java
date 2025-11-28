package dev.hugog.minecraft.blockstreet.migration.v110;

import dev.hugog.minecraft.blockstreet.data.dao.CompanyDao;
import dev.hugog.minecraft.blockstreet.data.services.CompaniesService;
import dev.hugog.minecraft.blockstreet.migration.Migrator;
import dev.hugog.minecraft.blockstreet.utils.SizedStack;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Optional;
import java.util.Set;

public class CompaniesMigrator implements Migrator {

    private final File pluginDataDirectory;
    private final CompaniesService companiesService;

    public CompaniesMigrator(File pluginDataDirectory, CompaniesService companiesService) {
        this.pluginDataDirectory = pluginDataDirectory;
        this.companiesService = companiesService;
    }

    @Override
    public void migrate() {

        File companiesFile = getCompaniesFile();
        FileConfiguration companiesConfiguration = YamlConfiguration.loadConfiguration(companiesFile);

        if (!companiesFile.exists()) {
            return;
        }

        Set<String> companiesIds = Optional.ofNullable(companiesConfiguration.getConfigurationSection("Companies"))
                .map(configurationSection -> configurationSection.getKeys(false))
                .orElse(Set.of());

        // Delete the example company to avoid duplicates
        companiesService.deleteCompany(0);

        companiesIds.forEach(companyId -> {

            String companyName = companiesConfiguration.getString("Companies." + companyId + ".Name");
            double sharesPrice = companiesConfiguration.getDouble("Companies." + companyId + ".Price");
            int risk = companiesConfiguration.getInt("Companies." + companyId + ".Risk");
            int sharesAmount = companiesConfiguration.getInt("Companies." + companyId + ".AvailableStocks");

            CompanyDao companyDao = CompanyDao.builder()
                    .name(companyName)
                    .icon(Material.EMERALD)
                    .risk(risk)
                    .totalShares(sharesAmount)
                    .availableShares(sharesAmount)
                    .initialSharePrice(sharesPrice)
                    .currentSharePrice(sharesPrice)
                    .historic(new SizedStack<>(500))
                    .build();

            companiesService.createCompany(companyDao);

        });

        archiveOldData();

    }

    @Override
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void archiveOldData() {
        getCompaniesFile().renameTo(new File(pluginDataDirectory, "companies.yml.old"));
    }

    @Override
    public String getOldDataVersion() {
        return "v1.1.0";
    }

    private File getCompaniesFile() {
        return new File(pluginDataDirectory, "companies.yml");
    }

}
