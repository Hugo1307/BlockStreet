package dev.hugog.minecraft.blockstreet.data.repositories.implementations;

import dev.hugog.minecraft.blockstreet.BlockStreet;
import dev.hugog.minecraft.blockstreet.data.api.ApiData;
import dev.hugog.minecraft.blockstreet.data.entities.PluginReleaseEntity;
import dev.hugog.minecraft.blockstreet.data.repositories.RepositoryImpl;
import dev.hugog.minecraft.blockstreet.data.sources.DataSource;
import dev.hugog.minecraft.blockstreet.data.api.PluginReleaseAPIData;
import lombok.extern.log4j.Log4j2;
import org.apache.maven.artifact.versioning.ComparableVersion;

@Log4j2
public class UpdatesRepository extends RepositoryImpl {

    private final BlockStreet plugin;

    public UpdatesRepository(ApiData apiData, BlockStreet plugin) {
        super(apiData);
        this.plugin = plugin;
    }

    public PluginReleaseEntity getLatestRelease() {

        if (!verifyDataSource()) {
            log.warn("The data source is not an instance of PluginReleaseAPIDataSource.");
            return null;
        }

        PluginReleaseAPIData pluginReleaseAPIDataSource = (PluginReleaseAPIData) pluginData;
        return (PluginReleaseEntity) pluginReleaseAPIDataSource.parseData();

    }

    public String getCurrentVersion() {
        return plugin.getDescription().getVersion();
    }

    public boolean isUpdateAvailable() {

        PluginReleaseEntity latestRelease = getLatestRelease();

        if (latestRelease == null) {
            return false;
        }

        String latestReleaseVersion = latestRelease.getReleaseVersion().replace("v", "");

        ComparableVersion latestVersion = new ComparableVersion(latestReleaseVersion);
        ComparableVersion currentVersion = new ComparableVersion(plugin.getDescription().getVersion());

        return currentVersion.compareTo(latestVersion) < 0;

    }

    @Override
    public boolean verifyDataSource() {
        return pluginData instanceof PluginReleaseAPIData;
    }

}
