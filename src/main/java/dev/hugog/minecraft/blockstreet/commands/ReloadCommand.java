package dev.hugog.minecraft.blockstreet.commands;

import dev.hugog.minecraft.blockstreet.BlockStreet;
import dev.hugog.minecraft.blockstreet.enums.ConfigurationFiles;
import dev.hugog.minecraft.blockstreet.others.ConfigAccessor;
import dev.hugog.minecraft.blockstreet.others.Messages;
import dev.hugog.minecraft.dev_command.annotations.Command;
import dev.hugog.minecraft.dev_command.commands.BukkitDevCommand;
import dev.hugog.minecraft.dev_command.commands.data.BukkitCommandData;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Reload Command
 * <p>
 * Command to reload the plugin.
 * <p>
 * Syntax: /invest reload
 *
 * @author Hugo1307
 * @since v1.0.0
 */
@Command(alias = "reload", description = "Reloads the plugin.", permission = "blockstreet.admin.reload")
public class ReloadCommand extends BukkitDevCommand {

	public ReloadCommand(BukkitCommandData commandData, CommandSender commandSender, String[] args) {
		super(commandData, commandSender, args);
	}

	@Override
	public void execute() {

		Player p = (Player) getCommandSender();

		Messages messages = new Messages();
		ConfigAccessor playersReg = new ConfigAccessor(BlockStreet.getInstance(), ConfigurationFiles.PLAYERS.getFileName());
		ConfigAccessor companiesReg = new ConfigAccessor(BlockStreet.getInstance(), ConfigurationFiles.COMPANIES.getFileName());

		if (p.hasPermission("blockstreet.admin.*") || p.hasPermission("blockstreet.admin.reload")) {

			playersReg.reloadConfig();
			companiesReg.reloadConfig();
			BlockStreet.getInstance().reloadConfig();

			BlockStreet.getInstance().stopRunnables();
			BlockStreet.getInstance().registerRunnables();

			p.sendMessage(messages.getPluginPrefix() + ChatColor.GREEN + messages.getPluginReload());

		}else {
			p.sendMessage(messages.getPluginPrefix() + messages.getNoPermission());
		}

	}

}
