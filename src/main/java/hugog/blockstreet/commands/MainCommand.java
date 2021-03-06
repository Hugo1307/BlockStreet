package hugog.blockstreet.commands;

import hugog.blockstreet.Main;
import hugog.blockstreet.others.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCommand{

	private final CommandSender sender;
	
	public MainCommand(CommandSender sender) {
		this.sender = sender;
	}
	
	protected void runMainCommand() {
		
		Messages messages = new Messages(Main.getInstance().messagesConfig);
		Player p = (Player) sender;

		if (p.hasPermission("blockstreet.admin.*")) {

		    p.sendMessage(messages.getPluginHeader());
		    p.sendMessage("");
		    p.sendMessage(ChatColor.GREEN + " • /invest" + ChatColor.GRAY + " - " + messages.getMenuMainCmd());
		    p.sendMessage(ChatColor.GREEN + " • /invest companies" + ChatColor.GRAY + " - " + messages.getMenuCompaniesCmd());
		    p.sendMessage(ChatColor.GREEN + " • /invest company" + ChatColor.DARK_GRAY + " [Id]" + ChatColor.GRAY + " - " + messages.getMenuCompanyCmd());
		    p.sendMessage(ChatColor.GREEN + " • /invest buy" + ChatColor.DARK_GRAY + " [amount] [Id]" + ChatColor.GRAY + " - " + messages.getMenuBuyCmd());
		    p.sendMessage(ChatColor.GREEN + " • /invest sell" + ChatColor.DARK_GRAY + " [amount] [Id]" + ChatColor.GRAY + " - " + messages.getMenuSellCmd());
		    p.sendMessage(ChatColor.GREEN + " • /invest actions" + ChatColor.GRAY + " - " + messages.getMenuActionsCmd());
		    p.sendMessage(ChatColor.GREEN + " • /invest time" + ChatColor.GRAY + " - " + messages.getMenuTimeCmd());
		    p.sendMessage(ChatColor.GREEN + " • /invest create" + ChatColor.DARK_GRAY + " [name] [price] [risk(1-5)]" + ChatColor.GRAY + " - " + messages.getMenuCreateCompanyCmd());
		    p.sendMessage(ChatColor.GREEN + " • /invest delete" + ChatColor.DARK_GRAY + " [Id]" + ChatColor.GRAY + " - " + messages.getMenuDeleteCompanyCmd());
		    p.sendMessage(ChatColor.GREEN + " • /invest reload" + ChatColor.GRAY + " - " + messages.getMenuReloadCmd());
		    p.sendMessage("");
		    p.sendMessage(messages.getPluginFooter());

		}else if (p.hasPermission("blockstreet.command.main") || p.hasPermission("blockstreet.command.*")) {

		    p.sendMessage(messages.getPluginHeader());
		    p.sendMessage("");
		    p.sendMessage(ChatColor.GREEN + " • /invest" + ChatColor.GRAY + " - " + messages.getMenuMainCmd());
		    p.sendMessage(ChatColor.GREEN + " • /invest companies" + ChatColor.GRAY + " - " + messages.getMenuCompaniesCmd());
		    p.sendMessage(ChatColor.GREEN + " • /invest company" + ChatColor.DARK_GRAY + " [Id]" + ChatColor.GRAY + " - " + messages.getMenuCompanyCmd());
		    p.sendMessage(ChatColor.GREEN + " • /invest buy" + ChatColor.DARK_GRAY + " [amount] [Id]" + ChatColor.GRAY + " - " + messages.getMenuBuyCmd());
		    p.sendMessage(ChatColor.GREEN + " • /invest sell" + ChatColor.DARK_GRAY + " [amount] [Id]" + ChatColor.GRAY + " - " + messages.getMenuSellCmd());
		    p.sendMessage(ChatColor.GREEN + " • /invest stocks" + ChatColor.GRAY + " - " + messages.getMenuActionsCmd());
		    p.sendMessage(ChatColor.GREEN + " • /invest time" + ChatColor.GRAY + " - " + messages.getMenuTimeCmd());
		    p.sendMessage("");
		    p.sendMessage(messages.getPluginFooter());

		}else {			
			p.sendMessage(messages.getPluginPrefix() + ChatColor.RED + messages.getNoPermission());
		}
		
	}
	
}
