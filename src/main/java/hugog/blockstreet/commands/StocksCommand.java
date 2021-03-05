package hugog.blockstreet.commands;

import hugog.blockstreet.Main;
import hugog.blockstreet.others.*;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StocksCommand {

	private final CommandSender sender;
	
	public StocksCommand(CommandSender sender) {
		this.sender = sender;		
	}
	
	public void runStocksCommand() {
		
		Player p = (Player) sender;
		Messages messages = new Messages(Main.getInstance().messagesConfig);
		
		if (p.hasPermission("blockstreet.command.actions") || p.hasPermission("blockstreet.command.*")) {
			
			Investor playerInvestProfile = new Investor(p.getName());
			
			if (playerInvestProfile.getInvestments().size() > 0) {
				
				p.sendMessage(messages.getPluginHeader());
	            p.sendMessage("");
	            
	            for (Investment investment : playerInvestProfile.getInvestments()) {
	            	
	            	Company investedCompany = new Company(investment.getId()).load();
	            	
	                p.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + investedCompany.getName());
	                p.sendMessage("");
	                p.sendMessage(ChatColor.GRAY + "Actions: " + ChatColor.GREEN + investment.getStocksAmount());
	                p.sendMessage(ChatColor.GRAY + "Total value: " + ChatColor.GREEN + (investment.getStocksAmount()*investedCompany.getStocksPrice()));
	                p.sendMessage("");
	            }
	            p.sendMessage(messages.getPluginFooter());
				
			}else {
				 p.sendMessage(messages.getPluginPrefix() + messages.getPlayerAnyActions());
			}
			
		}else {
			p.sendMessage(messages.getPluginPrefix() + messages.getNoPermission());
		}
	
	}
	
}
