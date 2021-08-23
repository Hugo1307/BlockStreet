package hugog.blockstreet.commands.implementation;

import hugog.blockstreet.Main;
import hugog.blockstreet.commands.PluginCommand;
import hugog.blockstreet.enums.ConfigurationFiles;
import hugog.blockstreet.others.ConfigAccessor;
import hugog.blockstreet.others.Messages;
import hugog.blockstreet.runnables.InterestRateRunnable;
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

	public ReloadCommand(CommandSender sender, String[] args) {
		super(sender, args);
	}

	@Override
	public void execute() {

		Player p = (Player) sender;

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