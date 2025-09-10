package dev.hugog.minecraft.blockstreet.ui.guis;

import dev.hugog.minecraft.blockstreet.enums.NotificationType;
import dev.hugog.minecraft.blockstreet.ui.AbstractPluginGui;
import dev.hugog.minecraft.blockstreet.ui.GuiManager;
import dev.hugog.minecraft.blockstreet.ui.items.NavigateBackItem;
import dev.hugog.minecraft.blockstreet.ui.items.NextPageItem;
import dev.hugog.minecraft.blockstreet.ui.items.NotificationToggleItem;
import dev.hugog.minecraft.blockstreet.ui.items.PreviousPageItem;
import dev.hugog.minecraft.blockstreet.utils.Messages;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NotificationsGui extends AbstractPluginGui {
    public NotificationsGui(Player player, GuiManager guiManager, Messages messages) {
        super(messages.getUiNotificationsTitle(), "blockstreet.ui.notifications", player, guiManager, messages);
    }

    @Override
    public Gui build() {
        List<Item> notificationItems = Arrays.stream(NotificationType.values())
                .map(notificationType -> new NotificationToggleItem(
                        guiManager.getPlugin(),
                        guiManager.getPlayersService(),
                        messages,
                        notificationType,
                        player.getUniqueId()
                ))
                .collect(Collectors.toList());

        return PagedGui.items()
                .setStructure(
                        "# # # # # # # # #",
                        "# x x x x x x x #",
                        "# x x x x x x x #",
                        "# # # < - > # # #")
                .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL) // where paged items should be put
                .addIngredient('#', new SimpleItem(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName("")))
                .addIngredient('-', new NavigateBackItem(guiManager, messages))
                .addIngredient('<', new PreviousPageItem(messages))
                .addIngredient('>', new NextPageItem(messages))
                .setContent(notificationItems)
                .build();
    }
}
