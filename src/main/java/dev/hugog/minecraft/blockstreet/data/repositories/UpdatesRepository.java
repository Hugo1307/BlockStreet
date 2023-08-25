package dev.hugog.minecraft.blockstreet.data.repositories;

import dev.hugog.minecraft.blockstreet.BlockStreet;
import dev.hugog.minecraft.blockstreet.data.entities.PluginReleaseEntity;
import dev.hugog.minecraft.blockstreet.data.sources.DataSource;
import dev.hugog.minecraft.blockstreet.data.sources.api.PluginReleaseAPIDataSource;
import lombok.extern.log4j.Log4j2;
import org.apache.maven.artifact.versioning.ComparableVersion;

@Log4j2
public class UpdatesRepository extends RepositoryImpl {

    private final BlockStreet plugin;

    public UpdatesRepository(DataSource dataSource, BlockStreet plugin) {
        super(dataSource);
        this.plugin = plugin;
    }

    public PluginReleaseEntity getLatestRelease() {

        if (!(dataSource instanceof PluginReleaseAPIDataSource)) {
            log.warn("The data source is not an instance of PluginReleaseAPIDataSource.");
            return null;
        }

        PluginReleaseAPIDataSource pluginReleaseAPIDataSource = (PluginReleaseAPIDataSource) dataSource;
        PluginReleaseEntity pluginReleaseEntity = (PluginReleaseEntity) pluginReleaseAPIDataSource.parseData();

        return pluginReleaseEntity;

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

}
