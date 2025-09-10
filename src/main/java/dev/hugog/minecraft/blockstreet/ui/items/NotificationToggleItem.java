package dev.hugog.minecraft.blockstreet.ui.items;

import dev.hugog.minecraft.blockstreet.BlockStreet;
import dev.hugog.minecraft.blockstreet.commands.ToggleNotificationCommand;
import dev.hugog.minecraft.blockstreet.data.services.PlayersService;
import dev.hugog.minecraft.blockstreet.enums.NotificationType;
import dev.hugog.minecraft.blockstreet.utils.Messages;
import dev.hugog.minecraft.dev_command.DevCommand;
import dev.hugog.minecraft.dev_command.integration.Integration;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

import java.text.MessageFormat;
import java.util.UUID;

public class NotificationToggleItem extends AbstractItem {

    private final BlockStreet plugin;
    private final PlayersService playersService;
    private final Messages messages;
    private final NotificationType notificationType;
    private final UUID playerId;

    public NotificationToggleItem(BlockStreet plugin, PlayersService playersService, Messages messages, NotificationType notificationType, UUID playerId) {
        this.plugin = plugin;
        this.playersService = playersService;
        this.messages = messages;
        this.notificationType = notificationType;
        this.playerId = playerId;
    }

    @Override
    public ItemProvider getItemProvider() {
        boolean isEnabled = playersService.hasNotificationEnabled(playerId, notificationType);
        Material material = isEnabled ? Material.LIME_CONCRETE : Material.RED_CONCRETE;
        String status = isEnabled ? messages.getEnabledString() : messages.getDisabledString();

        return new ItemBuilder(material)
                .setDisplayName(ChatColor.GOLD + messages.getMessageByKey(notificationType.getMessageKey()))
                .addLoreLines(MessageFormat.format(messages.getUiNotificationsToggleStatus(), status));
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        // Execute the command to toggle the notification
        DevCommand devCommand = DevCommand.getOrCreateInstance();
        devCommand.getCommandHandler().executeCommand(Integration.createFromPlugin(plugin), player, ToggleNotificationCommand.class, String.valueOf(notificationType));

        // Update the item to reflect the new state
        notifyWindows();
    }
}
