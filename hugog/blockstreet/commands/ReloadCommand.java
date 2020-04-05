package hugog.blockstreet.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import hugog.blockstreet.Main;
import hugog.blockstreet.others.ConfigAccessor;
import hugog.blockstreet.others.Messages;

public class ReloadCommand {

	private Main main;
	private CommandSender sender;
	
	public ReloadCommand(Main main, CommandSender sender) {
		
		this.main = main;
		this.sender = sender;
		
	}
	
	protected void runReloadCommand() {
		
		Player p = (Player) sender;
		
		Messages messages = new Messages(main.messagesConfig);
		ConfigAccessor playersConfig = new ConfigAccessor(main, "players.yml");
		
		if (p.hasPermission("blockstreet.admin.*") || p.hasPermission("blockstreet.admin.reload")) {
			
			playersConfig.saveConfig();
			playersConfig.reloadConfig();
			
			main.saveConfig();
			main.reloadConfig();
			
			p.sendMessage(messages.getPluginPrefix() + ChatColor.GREEN + messages.getPluginReload());
			
		}else {
			
			p.sendMessage(messages.getPluginPrefix() + messages.getNoPermission());
			
		}
		
	}
	
}
