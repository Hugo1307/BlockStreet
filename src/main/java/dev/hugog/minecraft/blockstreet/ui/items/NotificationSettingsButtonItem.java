package dev.hugog.minecraft.blockstreet.ui.items;

import dev.hugog.minecraft.blockstreet.BlockStreet;
import dev.hugog.minecraft.blockstreet.data.services.PlayersService;
import dev.hugog.minecraft.blockstreet.ui.guis.NotificationsGui;
import dev.hugog.minecraft.blockstreet.utils.Messages;
import io.github.hugo1307.qubinventorylib.inventory.Gui;
import io.github.hugo1307.qubinventorylib.item.AbstractItem;
import io.github.hugo1307.qubinventorylib.manager.GuiManager;
import io.github.hugo1307.qubinventorylib.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

public class NotificationSettingsButtonItem extends AbstractItem {

    private final BlockStreet plugin;
    private final GuiManager guiManager;
    private final PlayersService playersService;
    private final Messages messages;

    public NotificationSettingsButtonItem(BlockStreet plugin, GuiManager guiManager, PlayersService playersService, Messages messages) {
        super(new ItemBuilder(Material.BELL)
                .setName(messages.getUiNotificationsItemTitle())
                .addLoreLine(messages.getUiNotificationsItemDescription())
                .build());
        this.plugin = plugin;
        this.guiManager = guiManager;
        this.playersService = playersService;
        this.messages = messages;
    }

    @Override
    public void onClick(ClickType clickType, Player player, Gui clickedGui, InventoryClickEvent event) {
        guiManager.navigateTo(player, new NotificationsGui(plugin, player, guiManager, playersService, messages));
    }

}
