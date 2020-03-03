package hugog.blockstreet.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import hugog.blockstreet.Main;
import hugog.blockstreet.others.ConfigAccessor;
import hugog.blockstreet.others.Messages;

public class MainCommand{

	private Main Main;
	private String[] args;
	private CommandSender sender;
	
	public MainCommand(Main main, String[] args, CommandSender sender) {
		this.Main = main;
		this.args = args;
		this.sender = sender;
	}
	
	
	protected void runMainCommand() {
		
		Messages messages = new Messages(Main.messagesConfig); 
		Player p = (Player) sender;
		
		if (p.hasPermission("blockstreet.command.main") || p.hasPermission("blockstreet.command.*")) {
			
			p.sendMessage(messages.getPluginHeader());
			p.sendMessage("");
            p.sendMessage(ChatColor.GREEN + "/invest" + ChatColor.GRAY + " - " + messages.getMenuMainCmd());
            p.sendMessage(ChatColor.GREEN + "/invest companies" + ChatColor.GRAY + " - " + messages.getMenuCompaniesCmd());
            p.sendMessage(ChatColor.GREEN + "/invest company" + ChatColor.GOLD + " [Id]" + ChatColor.GRAY + " - " + messages.getMenuCompanyCmd());
            p.sendMessage(ChatColor.GREEN + "/invest buy" + ChatColor.GOLD + " [amount] [Id]" + ChatColor.GRAY + " - " + messages.getMenuBuyCmd());
            p.sendMessage(ChatColor.GREEN + "/invest sell" + ChatColor.GOLD + " [amount] [Id]" + ChatColor.GRAY + " - " + messages.getMenuSellCmd());
            p.sendMessage(ChatColor.GREEN + "/invest actions" + ChatColor.GRAY + " - " + messages.getMenuActionsCmd());
            p.sendMessage(ChatColor.GREEN + "/invest create" + ChatColor.GOLD + " [name] [price] [risk(1-5)]" + ChatColor.GRAY + " - " + messages.getMenuCreateCompanyCmd());
            p.sendMessage("");
            p.sendMessage(messages.getPluginFooter());
            
		}else {
			
			p.sendMessage(messages.getPluginPrefix() + ChatColor.RED + messages.getNoPermission());
			
		}
		
	}
	
}
