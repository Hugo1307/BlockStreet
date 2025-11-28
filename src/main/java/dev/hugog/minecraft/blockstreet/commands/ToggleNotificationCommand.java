package dev.hugog.minecraft.blockstreet.commands;

import dev.hugog.minecraft.blockstreet.commands.validators.NotificationTypeArgumentParser;
import dev.hugog.minecraft.blockstreet.data.services.CompaniesService;
import dev.hugog.minecraft.blockstreet.data.services.PlayersService;
import dev.hugog.minecraft.blockstreet.enums.NotificationType;
import dev.hugog.minecraft.blockstreet.utils.Messages;
import dev.hugog.minecraft.dev_command.annotations.*;
import dev.hugog.minecraft.dev_command.commands.BukkitDevCommand;
import dev.hugog.minecraft.dev_command.commands.data.BukkitCommandData;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Toggle Notifications Command
 *
 * <p>Command that allow players to toggle their notifications on/off for a certain notification type.
 * <p>Syntax: /invest notifications [notificationType]
 *
 * @author Hugo1307
 * @since v2.8.0
 */
@AutoValidation
@Command(alias = "notification", description = "toggleNotificationsCommand.description", permission = "blockstreet.command.notification", isPlayerOnly = true)
@Dependencies(dependencies = {Messages.class, PlayersService.class, CompaniesService.class, Economy.class})
@Arguments({
        @Argument(name = "notificationType", description = "toggleNotificationsCommand.notificationTypeArg", position = 0, parser = NotificationTypeArgumentParser.class),
})
public class ToggleNotificationCommand extends BukkitDevCommand {

    private final Messages messages;
    private final PlayersService playersService;

    public ToggleNotificationCommand(BukkitCommandData commandData, CommandSender commandSender, String[] args) {
        super(commandData, commandSender, args);
        this.messages = getDependency(Messages.class);
        this.playersService = getDependency(PlayersService.class);
    }

    @Override
    public void execute() {
        Player player = (Player) getCommandSender();
        NotificationType notificationType = ((NotificationTypeArgumentParser) getArgumentParser(0)).parse().orElseThrow();

        playersService.toggleNotification(player.getUniqueId(), notificationType);

        String status = playersService.hasNotificationEnabled(player.getUniqueId(), notificationType) ? messages.getEnabledString() : messages.getDisabledString();
        String notificationName = messages.getMessageByKey(notificationType.getMessageKey());
        player.sendMessage(messages.getPluginPrefix() + MessageFormat.format(messages.getNotificationToggled(), status, notificationName));
    }

    @Override
    public List<String> onTabComplete(String[] args) {
        if (args.length == 1) {
            return Arrays.stream(NotificationType.values())
                    .map(Enum::name)
                    .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return List.of();
    }
}
