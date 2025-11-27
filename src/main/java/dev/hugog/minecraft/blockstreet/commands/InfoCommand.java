package dev.hugog.minecraft.blockstreet.commands;

import dev.hugog.minecraft.blockstreet.api.services.AutoUpdateService;
import dev.hugog.minecraft.blockstreet.utils.Messages;
import dev.hugog.minecraft.dev_command.annotations.Command;
import dev.hugog.minecraft.dev_command.annotations.Dependencies;
import dev.hugog.minecraft.dev_command.commands.BukkitDevCommand;
import dev.hugog.minecraft.dev_command.commands.data.BukkitCommandData;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Info Command
 * <p>
 * Command to display information about the plugin.
 * <p>
 * Syntax: /invest info
 *
 * @author Hugo1307
 * @version 1.0.0
 */
@Command(alias = "info", description = "infoCommandDescription", permission = "*", isPlayerOnly = true)
@Dependencies(dependencies = {Messages.class, AutoUpdateService.class})
public class InfoCommand extends BukkitDevCommand {

    public InfoCommand(BukkitCommandData commandData, CommandSender commandSender, String[] args) {
        super(commandData, commandSender, args);
    }

    @Override
    public void execute() {

        // Obtain dependencies
        Messages messages = getDependency(Messages.class);
        AutoUpdateService autoUpdateService = getDependency(AutoUpdateService.class);

        if (!canSenderExecuteCommand()) {
            getCommandSender().sendMessage(messages.getPluginPrefix() + messages.getPlayerOnlyCommand());
            return;
        }

        Player player = (Player) getCommandSender();

        autoUpdateService.getLatestRelease().thenAcceptAsync(latestReleaseEntity -> {

            boolean isUpdateAvailable = autoUpdateService.isUpdateAvailable().join();

            String currentVersion = autoUpdateService.getCurrentVersion();

            player.sendMessage(messages.getPluginHeader());
            player.sendMessage("");
            player.sendMessage(ChatColor.GREEN + "Current Version: " + ChatColor.GRAY + currentVersion);
            player.sendMessage(ChatColor.GREEN + "Last Version: " + ChatColor.GRAY + latestReleaseEntity.getReleaseVersion());
            player.sendMessage("");

            if (isUpdateAvailable) {
                player.sendMessage(ChatColor.GRAY + "New version available!");
                player.sendMessage(ChatColor.GRAY + "Download it on: https://modrinth.com/plugin/blockstreet");
            } else {
                player.sendMessage(ChatColor.GRAY + "Your plugin is up to date.");
            }

            player.sendMessage("");
            player.sendMessage(ChatColor.GRAY + "Plugin created by: " + ChatColor.GREEN + "Hugo1307");
            player.sendMessage(messages.getPluginFooter());

        });

    }

    @Override
    public List<String> onTabComplete(String[] strings) {
        return List.of();
    }

}
