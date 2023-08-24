package dev.hugog.minecraft.blockstreet.update;

import dev.hugog.minecraft.blockstreet.Main;
import org.bukkit.plugin.PluginDescriptionFile;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateCheckerTest {

    private Main pluginMock;
    private PluginDescriptionFile pluginDescriptionFileMock;
    private UpdateChecker updateChecker;

    @Before
    public void setUp() {

        pluginMock = mock(Main.class);
        pluginDescriptionFileMock = mock(PluginDescriptionFile.class);
        updateChecker = spy(new UpdateChecker(pluginMock));

    }

    @Test
    @DisplayName("Test if check update is identifying version correctly.")
    public void testCheckForUpdates() {

        when(pluginMock.getDescription()).thenReturn(pluginDescriptionFileMock);
        when(pluginDescriptionFileMock.getVersion()).thenReturn("v1.0.0-beta");

        when(updateChecker.getRequestBody())
                .thenReturn("{\"tag_name\":\"v99.99.99\"}")
                .thenReturn("{\"tag_name\":\"v0.99.99\"}")
                .thenReturn("{\"tag_name\":\"v1.0.0-beta\"}")
                .thenReturn("{\"tag_name\":\"v1.0.0-alpha\"}")
                .thenReturn("{\"tag_name\":\"v1.0.0\"}");

        assertTrue(updateChecker.checkForUpdates());
        assertFalse(updateChecker.checkForUpdates());
        assertFalse(updateChecker.checkForUpdates());
        assertFalse(updateChecker.checkForUpdates());
        assertTrue(updateChecker.checkForUpdates());

    }

}