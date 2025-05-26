package dev.hugog.minecraft.blockstreet.ui.items;

import dev.hugog.minecraft.blockstreet.ui.GuiManager;
import dev.hugog.minecraft.blockstreet.ui.PluginGuiType;
import dev.hugog.minecraft.blockstreet.utils.Messages;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

@RequiredArgsConstructor
public class PortfolioButtonItem extends AbstractItem {

    private final GuiManager guiManager;
    private final Messages messages;

    @Override
    public ItemProvider getItemProvider() {
        return new ItemBuilder(Material.DIAMOND)
                .setDisplayName(messages.getUiCheckPortfolioButtonTitle())
                .addLoreLines(messages.getUiCheckPortfolioButtonDescription());
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        guiManager.navigate(player, PluginGuiType.PORTFOLIO_GUI);
    }
}
