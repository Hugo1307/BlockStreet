package hugog.blockstreet.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import hugog.blockstreet.Main;
import hugog.blockstreet.others.Messages;
import hugog.blockstreet.update.AutoUpdate;
import net.md_5.bungee.api.ChatColor;

public class InfoCommand {

	private Main main;
	private CommandSender sender;
	
	public InfoCommand(Main main, CommandSender sender) {
		this.main = main;
		this.sender = sender;
	}
	
	void runInfoCommand() {
		
		Player p = (Player) sender;
		
		Messages messages = new Messages(main.messagesConfig);
		long totalMemUsed = (long) ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / Math.pow(2, 20));
		long memUsedByPlugin = (long) (main.usedMemOnStartup - totalMemUsed); 
		
		p.sendMessage(messages.getPluginHeader());
		p.sendMessage("");
		/*
		if (memUsedByPlugin <= 0) {
			p.sendMessage(ChatColor.GREEN + "Memory used by plugin: " + ChatColor.GRAY + "~0 MB");
		}else {
			p.sendMessage(ChatColor.GREEN + "Memory used by plugin: " + ChatColor.GRAY + memUsedByPlugin + " MB");
		}
		p.sendMessage("");
		*/
		p.sendMessage(ChatColor.GREEN + "Current Version: " + ChatColor.GRAY + AutoUpdate.getCurrentVersion());
		p.sendMessage(ChatColor.GREEN + "Last Version: " + ChatColor.GRAY + AutoUpdate.getLastVersion());
		p.sendMessage("");
		
		if (AutoUpdate.isNewVersionAvailable()) {
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
