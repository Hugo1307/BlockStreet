package dev.hugog.minecraft.blockstreet.ui.guis;

import dev.hugog.minecraft.blockstreet.BlockStreet;
import dev.hugog.minecraft.blockstreet.data.services.PlayersService;
import dev.hugog.minecraft.blockstreet.enums.NotificationType;
import dev.hugog.minecraft.blockstreet.ui.items.NavigateBackItem;
import dev.hugog.minecraft.blockstreet.ui.items.NotificationToggleItem;
import dev.hugog.minecraft.blockstreet.utils.Messages;
import io.github.hugo1307.qubinventorylib.inventory.AutoUpdatePagedGui;
import io.github.hugo1307.qubinventorylib.inventory.InventoryStructure;
import io.github.hugo1307.qubinventorylib.item.InventoryItem;
import io.github.hugo1307.qubinventorylib.item.NextPageButton;
import io.github.hugo1307.qubinventorylib.item.PreviousPageButton;
import io.github.hugo1307.qubinventorylib.item.SimpleItem;
import io.github.hugo1307.qubinventorylib.manager.GuiManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NotificationsGui extends AutoUpdatePagedGui {

    private final BlockStreet plugin;
    private final PlayersService playersService;
    private final Messages messages;
    private final Player player;

    public NotificationsGui(BlockStreet plugin, Player player, GuiManager guiManager, PlayersService playersService, Messages messages) {
        super(messages.getUiNotificationsTitle(), 4, player, plugin, 5 * 20L);

        this.plugin = plugin;
        this.player = player;
        this.playersService = playersService;
        this.messages = messages;

        setContent(buildContent());
        applyStructure(buildStructure(guiManager));
    }

    private List<InventoryItem> buildContent() {
        return Arrays.stream(NotificationType.values())
                .map(notificationType -> new NotificationToggleItem(plugin, playersService, messages, notificationType, player.getUniqueId()))
                .collect(Collectors.toList());
    }

    private InventoryStructure buildStructure(GuiManager guiManager) {
        return new InventoryStructure.Builder()
                .withStructure(
                        "# # # # # # # # #",
                        "# x x x x x x x #",
                        "# x x x x x x x #",
                        "# # # < - > # # #"
                )
                .withIngredient('#', new SimpleItem.Builder().withMaterial(Material.GRAY_STAINED_GLASS_PANE).build())
                .withListMarker('x')
                .withIngredient('-', new NavigateBackItem(guiManager, messages))
                .withIngredient('<', new PreviousPageButton(messages.getUiPreviousPage()))
                .withIngredient('>', new NextPageButton(messages.getUiNextPage()))
                .build();
    }

}
