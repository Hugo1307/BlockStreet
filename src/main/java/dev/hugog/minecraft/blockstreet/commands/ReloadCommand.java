package dev.hugog.minecraft.blockstreet.commands;

import dev.hugog.minecraft.blockstreet.Main;
import dev.hugog.minecraft.blockstreet.enums.ConfigurationFiles;
import dev.hugog.minecraft.blockstreet.others.ConfigAccessor;
import dev.hugog.minecraft.blockstreet.others.Messages;
import me.hgsoft.minecraft.devcommand.annotations.Command;
import me.hgsoft.minecraft.devcommand.commands.BukkitDevCommand;
import me.hgsoft.minecraft.devcommand.commands.data.BukkitCommandData;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Reload Command
 *
 * Command to reload the plugin.
 *
 * Syntax: /invest reload
 *
 * @author Hugo1307
 * @since v1.0.0
 */
@Command(alias = "reload", permission = "blockstreet.admin.reload")
public class ReloadCommand extends BukkitDevCommand {

	public ReloadCommand(BukkitCommandData commandData, CommandSender commandSender, String[] args) {
		super(commandData, commandSender, args);
	}

	@Override
	public void execute() {

		Player p = (Player) getCommandSender();

		Messages messages = new Messages();
		ConfigAccessor playersReg = new ConfigAccessor(Main.getInstance(), ConfigurationFiles.PLAYERS.getFileName());
		ConfigAccessor companiesReg = new ConfigAccessor(Main.getInstance(), ConfigurationFiles.COMPANIES.getFileName());

		if (p.hasPermission("blockstreet.admin.*") || p.hasPermission("blockstreet.admin.reload")) {

			playersReg.reloadConfig();
			companiesReg.reloadConfig();
			Main.getInstance().reloadConfig();

			Main.getInstance().stopRunnables();
			Main.getInstance().registerRunnables();

			p.sendMessage(messages.getPluginPrefix() + ChatColor.GREEN + messages.getPluginReload());

		}else {
			p.sendMessage(messages.getPluginPrefix() + messages.getNoPermission());
		}

	}

}
