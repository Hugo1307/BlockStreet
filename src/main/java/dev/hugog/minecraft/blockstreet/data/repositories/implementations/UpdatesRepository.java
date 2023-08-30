package dev.hugog.minecraft.blockstreet.data.repositories.implementations;

import dev.hugog.minecraft.blockstreet.BlockStreet;
import dev.hugog.minecraft.blockstreet.api.ApiData;
import dev.hugog.minecraft.blockstreet.api.entities.PluginReleaseEntity;
import lombok.extern.log4j.Log4j2;
import org.apache.maven.artifact.versioning.ComparableVersion;

@Log4j2
public class UpdatesRepository {

    private final BlockStreet plugin;

    public UpdatesRepository(ApiData apiData, BlockStreet plugin) {
        this.plugin = plugin;
    }

    public PluginReleaseEntity getLatestRelease() {

        // TODO: Fix this

//        PluginReleaseAPIData pluginReleaseAPIDataSource = (PluginReleaseAPIData) plugin.getApiData();
//        return (PluginReleaseEntity) pluginReleaseAPIDataSource.parseData();

        return null;

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

}
