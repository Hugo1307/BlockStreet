package hugog.blockstreet.commands;

import hugog.blockstreet.Main;
import hugog.blockstreet.enums.ConfigurationFiles;
import hugog.blockstreet.others.ConfigAccessor;
import hugog.blockstreet.others.Messages;
import hugog.blockstreet.runnables.InterestRateRunnable;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadCommand {

	private final CommandSender sender;
	
	public ReloadCommand(CommandSender sender) {
		this.sender = sender;		
	}
	
	protected void runReloadCommand() {
		
		Player p = (Player) sender;
		
		Messages messages = new Messages(Main.getInstance().messagesConfig);
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
