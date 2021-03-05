package hugog.blockstreet.commands;

import hugog.blockstreet.Main;
import hugog.blockstreet.others.Messages;
import hugog.blockstreet.update.AutoUpdate;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InfoCommand {

	private final CommandSender sender;
	
	public InfoCommand(CommandSender sender) {
		this.sender = sender;
	}
	
	void runInfoCommand() {
		
		Player p = (Player) sender;
		
		Messages messages = new Messages(Main.getInstance().messagesConfig);
		
		p.sendMessage(messages.getPluginHeader());
		p.sendMessage("");

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
