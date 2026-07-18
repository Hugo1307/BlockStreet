package dev.hugog.minecraft.blockstreet.ui.items;

import dev.hugog.minecraft.blockstreet.utils.Messages;
import io.github.hugo1307.qubinventorylib.inventory.Gui;
import io.github.hugo1307.qubinventorylib.item.AbstractItem;
import io.github.hugo1307.qubinventorylib.manager.GuiManager;
import io.github.hugo1307.qubinventorylib.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

public class NavigateBackItem extends AbstractItem {

    private final GuiManager guiManager;

    public NavigateBackItem(GuiManager guiManager, Messages messages) {
        super(new ItemBuilder(Material.REDSTONE_TORCH).setName(messages.getUiNavigateBack()).build());
        this.guiManager = guiManager;
    }

    @Override
    public void onClick(ClickType clickType, Player player, Gui clickedGui, InventoryClickEvent event) {
        guiManager.navigateBack(player);
    }

}
