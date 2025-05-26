package dev.hugog.minecraft.blockstreet.ui;

import dev.hugog.minecraft.blockstreet.utils.Messages;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.window.Window;

@Getter
@RequiredArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public abstract class AbstractPluginGui {

    protected final String title;
    protected final String permission;
    protected final Player player;
    protected final GuiManager guiManager;
    protected final Messages messages;

    @Setter
    protected Runnable onCloseCallback;
    protected Window window;

    /**
     * Opens the GUI for the player
     */
    public void open() {
        if (!player.hasPermission(permission)) {
            player.sendMessage(messages.getNoPermission());
            return;
        }

        window = Window.single()
                .setTitle(title)
                .setViewer(player)
                .setGui(this::build)
                .build();

        if (onCloseCallback != null) {
            window.addCloseHandler(onCloseCallback);
        }
        window.open();
    }

    public void close() {
        window.close();
    }

    /**
     * Builds the GUI
     *
     * @return the GUI
     */
    public abstract Gui build();

}


