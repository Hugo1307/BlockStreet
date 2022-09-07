package hugog.blockstreet.commands.implementation;

import hugog.blockstreet.Main;
import hugog.blockstreet.commands.CmdDependencyInjector;
import hugog.blockstreet.commands.PluginCommand;
import hugog.blockstreet.enums.ConfigurationFiles;
import hugog.blockstreet.others.ConfigAccessor;
import hugog.blockstreet.others.Messages;
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
public class ReloadCommand extends PluginCommand {

	public ReloadCommand(CommandSender sender, String[] args, CmdDependencyInjector cmdDependencyInjector) {
		super(sender, args, cmdDependencyInjector);
	}

	@Override
	public void execute() {

		Player player = (Player) sender;

		Messages messages = new Messages();
		ConfigAccessor playersReg = new ConfigAccessor(Main.getInstance(), ConfigurationFiles.PLAYERS.getFileName());
		ConfigAccessor companiesReg = new ConfigAccessor(Main.getInstance(), ConfigurationFiles.COMPANIES.getFileName());

		if (player.isOp() || player.hasPermission("blockstreet.admin.*") || player.hasPermission("blockstreet.admin.reload")) {

			playersReg.reloadConfig();
			companiesReg.reloadConfig();
			Main.getInstance().reloadConfig();

			Main.getInstance().stopRunnables();
			Main.getInstance().registerRunnables();

			player.sendMessage(messages.getPluginPrefix() + ChatColor.GREEN + messages.getPluginReload());

		}else {
			player.sendMessage(messages.getPluginPrefix() + messages.getNoPermission());
		}

	}

}
