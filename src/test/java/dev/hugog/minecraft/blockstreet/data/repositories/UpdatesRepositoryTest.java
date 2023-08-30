package dev.hugog.minecraft.blockstreet.data.repositories;

import dev.hugog.minecraft.blockstreet.BlockStreet;
import dev.hugog.minecraft.blockstreet.api.entities.PluginReleaseEntity;
import dev.hugog.minecraft.blockstreet.data.repositories.implementations.UpdatesRepository;
import dev.hugog.minecraft.blockstreet.api.PluginReleaseAPIData;
import org.bukkit.plugin.PluginDescriptionFile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdatesRepositoryTest {

    @Mock
    private PluginReleaseAPIData pluginReleaseAPIDataSource;

    @Mock
    private BlockStreet plugin;

    @Mock
    private PluginDescriptionFile descriptionFile;

    @Test
    void getLatestRelease() {

        UpdatesRepository updatesRepository = new UpdatesRepository(pluginReleaseAPIDataSource, null);
        PluginReleaseEntity pluginReleaseEntity = PluginReleaseEntity.builder()
                .releaseVersion("v1.0.0")
                .build();

        when(pluginReleaseAPIDataSource.parseData()).thenReturn(pluginReleaseEntity);
        assertThat(updatesRepository.getLatestRelease()).isEqualTo(pluginReleaseEntity);

    }

    @Test
    void isUpdateAvailable() {

        UpdatesRepository updatesRepository = new UpdatesRepository(pluginReleaseAPIDataSource, plugin);
        PluginReleaseEntity pluginReleaseEntity = PluginReleaseEntity.builder()
                .releaseVersion("v1.0.0")
                .build();

        // When the plugin's version is 0.0.1 and the latest release is 1.0.0, an update is available.
        when(plugin.getDescription()).thenReturn(descriptionFile);
        when(descriptionFile.getVersion()).thenReturn("0.0.1");

        when(pluginReleaseAPIDataSource.parseData()).thenReturn(pluginReleaseEntity);
        assertThat(updatesRepository.isUpdateAvailable()).isTrue();

        // When the plugin's version is 1.0.1 and the latest release is 1.0.0, the update is not available.
        when(plugin.getDescription()).thenReturn(descriptionFile);
        when(descriptionFile.getVersion()).thenReturn("1.0.1");

        when(pluginReleaseAPIDataSource.parseData()).thenReturn(pluginReleaseEntity);
        assertThat(updatesRepository.isUpdateAvailable()).isFalse();

        // When the plugin's version is 1.1 and the latest release is 1.0.0, the update is not available.
        when(plugin.getDescription()).thenReturn(descriptionFile);
        when(descriptionFile.getVersion()).thenReturn("1.1");

        when(pluginReleaseAPIDataSource.parseData()).thenReturn(pluginReleaseEntity);
        assertThat(updatesRepository.isUpdateAvailable()).isFalse();

        // When the plugin's version is 1.0.0 and the latest release is 1.0.0-alpha, the update is not available.
        pluginReleaseEntity = PluginReleaseEntity.builder()
                .releaseVersion("v1.0.0-alpha")
                .build();

        when(plugin.getDescription()).thenReturn(descriptionFile);
        when(descriptionFile.getVersion()).thenReturn("1.0.0");

        when(pluginReleaseAPIDataSource.parseData()).thenReturn(pluginReleaseEntity);
        assertThat(updatesRepository.isUpdateAvailable()).isFalse();

    }

}