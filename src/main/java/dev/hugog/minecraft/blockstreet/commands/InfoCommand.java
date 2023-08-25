package dev.hugog.minecraft.blockstreet.commands;

import dev.hugog.minecraft.blockstreet.data.entities.PluginReleaseEntity;
import dev.hugog.minecraft.blockstreet.data.repositories.UpdatesRepository;
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
 *
 * Command to display information about the plugin.
 *
 * Syntax: /invest info
 *
 * @author Hugo1307
 * @version 1.0.0
 */
@Command(alias = "info")
@Dependencies(dependencies = {UpdatesRepository.class})
public class InfoCommand extends BukkitDevCommand {

	public InfoCommand(BukkitCommandData commandData, CommandSender commandSender, String[] args) {
		super(commandData, commandSender, args);
	}

	@Override
	public void execute() {

		Player p = (Player) getCommandSender();
		Messages messages = new Messages();

		UpdatesRepository updatesRepository = (UpdatesRepository) getDependencies().get(0);
		PluginReleaseEntity latestReleaseEntity = updatesRepository.getLatestRelease();
		String currentVersion = updatesRepository.getCurrentVersion();

		p.sendMessage(messages.getPluginHeader());
		p.sendMessage("");
		p.sendMessage(ChatColor.GREEN + "Current Version: " + ChatColor.GRAY + currentVersion);
		p.sendMessage(ChatColor.GREEN + "Last Version: " + ChatColor.GRAY + latestReleaseEntity.getReleaseVersion());
		p.sendMessage("");

		if (updatesRepository.isUpdateAvailable()) {
			p.sendMessage(ChatColor.GRAY + "New version available!");
			p.sendMessage(ChatColor.GRAY + "Download it on: https://www.spigotmc.org/resources/blockstreet.75712/");
		}else {
			p.sendMessage(ChatColor.GRAY + "Your plugin is up to date.");
		}

		p.sendMessage("");
		p.sendMessage(ChatColor.GRAY + "Plugin created by: " + ChatColor.GREEN + "Hugo1307");
		p.sendMessage(messages.getPluginFooter());


	}

}
