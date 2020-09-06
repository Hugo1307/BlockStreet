package hugog.blockstreet.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import hugog.blockstreet.Main;
import hugog.blockstreet.others.Company;
import hugog.blockstreet.others.ConfigAccessor;
import hugog.blockstreet.others.Investment;
import hugog.blockstreet.others.Investor;
import hugog.blockstreet.others.Messages;

public class StocksCommand {

	private Main main;
	private CommandSender sender;
	
	public StocksCommand(Main main, CommandSender sender) {		
		this.main = main;
		this.sender = sender;		
	}
	
	public void runActionsCommand() {
		
		Player p = (Player) sender;
		Messages messages = new Messages(main.messagesConfig);
		ConfigAccessor companiesReg = new ConfigAccessor(main, "companies.yml");
		
		if (p.hasPermission("blockstreet.command.actions") || p.hasPermission("blockstreet.command.*")) {
			
			Investor playerInvestProfile = new Investor(p.getName());
			
			if (playerInvestProfile.getInvestments().size() > 0) {
				
				p.sendMessage(messages.getPluginHeader());
	            p.sendMessage("");
	            
	            for (Investment investment : playerInvestProfile.getInvestments()) {
	            	
	            	Company investedCompany = new Company(companiesReg, investment.getId());
	            	
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
