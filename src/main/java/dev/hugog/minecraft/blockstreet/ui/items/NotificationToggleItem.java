package dev.hugog.minecraft.blockstreet.ui.items;

import dev.hugog.minecraft.blockstreet.BlockStreet;
import dev.hugog.minecraft.blockstreet.commands.ToggleNotificationCommand;
import dev.hugog.minecraft.blockstreet.data.services.PlayersService;
import dev.hugog.minecraft.blockstreet.enums.NotificationType;
import dev.hugog.minecraft.blockstreet.utils.Messages;
import dev.hugog.minecraft.dev_command.DevCommand;
import dev.hugog.minecraft.dev_command.integration.Integration;
import io.github.hugo1307.qubinventorylib.inventory.Gui;
import io.github.hugo1307.qubinventorylib.item.RefreshableItem;
import io.github.hugo1307.qubinventorylib.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.text.MessageFormat;
import java.util.UUID;

public class NotificationToggleItem extends RefreshableItem {

    private final BlockStreet plugin;
    private final NotificationType notificationType;

    public NotificationToggleItem(BlockStreet plugin, PlayersService playersService, Messages messages, NotificationType notificationType, UUID playerId) {
        super(() -> getItem(playersService, messages, notificationType, playerId));
        this.plugin = plugin;
        this.notificationType = notificationType;
    }

    private static ItemStack getItem(PlayersService playersService, Messages messages, NotificationType notificationType, UUID playerId) {
        boolean isEnabled = playersService.hasNotificationEnabled(playerId, notificationType);
        Material material = isEnabled ? Material.LIME_CONCRETE : Material.RED_CONCRETE;
        String status = isEnabled ? messages.getEnabledString() : messages.getDisabledString();

        return new ItemBuilder(material)
                .setName(ChatColor.GOLD + messages.getMessageByKey(notificationType.getMessageKey()))
                .addLoreLine(MessageFormat.format(messages.getUiNotificationsToggleStatus(), status))
                .build();
    }

    @Override
    public void onClick(ClickType clickType, Player player, Gui clickedGui, InventoryClickEvent event) {
        DevCommand devCommand = DevCommand.getOrCreateInstance();
        devCommand.getCommandHandler().executeCommand(Integration.createFromPlugin(plugin), player, ToggleNotificationCommand.class, String.valueOf(notificationType));
        clickedGui.update();
    }

}
