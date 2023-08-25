package dev.hugog.minecraft.blockstreet.commands;

import dev.hugog.minecraft.blockstreet.data.entities.PluginReleaseEntity;
import dev.hugog.minecraft.blockstreet.data.repositories.implementations.UpdatesRepository;
import dev.hugog.minecraft.blockstreet.others.Messages;
import dev.hugog.minecraft.dev_command.annotations.Command;
import dev.hugog.minecraft.dev_command.annotations.Dependencies;
import dev.hugog.minecraft.dev_command.commands.BukkitDevCommand;
import dev.hugog.minecraft.dev_command.commands.data.BukkitCommandData;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
@Command(alias = "info", isPlayerOnly = true)
@Dependencies(dependencies = {Messages.class, UpdatesRepository.class})
public class InfoCommand extends BukkitDevCommand {

	public InfoCommand(BukkitCommandData commandData, CommandSender commandSender, String[] args) {
		super(commandData, commandSender, args);
	}

	@Override
	public void execute() {

		// Obtain dependencies
		Messages messages = (Messages) getDependency(Messages.class);
		UpdatesRepository updatesRepository = (UpdatesRepository) getDependency(UpdatesRepository.class);

		if (!canSenderExecuteCommand()) {
			getCommandSender().sendMessage(messages.getPluginPrefix() + messages.getPlayerOnlyCommand());
			return;
		}

		Player player = (Player) getCommandSender();

		PluginReleaseEntity latestReleaseEntity = updatesRepository.getLatestRelease();
		String currentVersion = updatesRepository.getCurrentVersion();

		player.sendMessage(messages.getPluginHeader());
		player.sendMessage("");
		player.sendMessage(ChatColor.GREEN + "Current Version: " + ChatColor.GRAY + currentVersion);
		player.sendMessage(ChatColor.GREEN + "Last Version: " + ChatColor.GRAY + latestReleaseEntity.getReleaseVersion());
		player.sendMessage("");

		if (updatesRepository.isUpdateAvailable()) {
			player.sendMessage(ChatColor.GRAY + "New version available!");
			player.sendMessage(ChatColor.GRAY + "Download it on: https://www.spigotmc.org/resources/blockstreet.75712/");
		}else {
			player.sendMessage(ChatColor.GRAY + "Your plugin is up to date.");
		}

		player.sendMessage("");
		player.sendMessage(ChatColor.GRAY + "Plugin created by: " + ChatColor.GREEN + "Hugo1307");
		player.sendMessage(messages.getPluginFooter());

	}

}
