package hugog.blockstreet.commands;

import hugog.blockstreet.Main;
import hugog.blockstreet.enums.ConfigurationFiles;
import hugog.blockstreet.others.Company;
import hugog.blockstreet.others.ConfigAccessor;
import hugog.blockstreet.others.Messages;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CompanyCommand {

	private final String[] args;
	private final CommandSender sender;
	
	public CompanyCommand (String[] args, CommandSender sender) {
		this.args = args;
		this.sender = sender;
	}
	
	protected void runCompanyCommand () {
		
		Player p = (Player) sender;
		Messages messages = new Messages(Main.getInstance().messagesConfig);
		ConfigAccessor companiesReg = new ConfigAccessor(Main.getInstance(), ConfigurationFiles.COMPANIES.getFileName());
		
		if (p.hasPermission("blockstreet.command.company") || p.hasPermission("blockstreet.command.*")) {
			
			if (args.length > 1) {
				
				int numberOfCompanies = 0;
				
				if (companiesReg.getConfig().get("Companies") != null) 
					numberOfCompanies = companiesReg.getConfig().getConfigurationSection("Companies").getKeys(false).size();
				
				if (numberOfCompanies > 0) {
					
					for (int i = 1; i <= numberOfCompanies; i++) {
			        	
			            if(args[1].equals(String.valueOf(i))){
			            	
			            	Company currentCompany = new Company(i).load();
			            	
			            	TextComponent buyStocks = new TextComponent(ChatColor.GRAY + "[Buy Stocks]");
			        		buyStocks.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, 
			        				new ComponentBuilder(ChatColor.GRAY + "Click to see company's details.").create()));
			        		buyStocks.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/invest buy 1 " + i));
			            	
			                p.sendMessage(messages.getPluginHeader());
			                p.sendMessage("");
			                p.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + currentCompany.getName());
			                p.sendMessage("");
			                p.sendMessage(ChatColor.GRAY + "Id: " + ChatColor.GREEN + currentCompany.getId());
			                p.sendMessage(ChatColor.GRAY + messages.getPrice() + ": " + ChatColor.GREEN + currentCompany.getStocksPrice());
			                p.sendMessage(ChatColor.GRAY + messages.getRisk() + ": " + ChatColor.GREEN + currentCompany.getRisk());
			                
			                if (currentCompany.getAvailableStocks() < 0)
			                    p.sendMessage(ChatColor.GRAY + messages.getAvailableActions() + ": " + ChatColor.GREEN + "Unlimited");
			                else
			                    p.sendMessage(ChatColor.GRAY + messages.getAvailableActions() + ": " + ChatColor.GREEN + currentCompany.getAvailableStocks());

			                p.sendMessage(ChatColor.GRAY + messages.getActionHistoric() + ": ");
			                p.sendMessage("");
			                
			                for (String element : currentCompany.getCompanyHistoric()){
			                    if (element.contains("+")) p.sendMessage(ChatColor.GREEN + "  " + element);
			                    else if (element.contains("-")) p.sendMessage(ChatColor.RED + "  " + element);
			                }
			                
			                p.sendMessage("");
			                p.spigot().sendMessage(buyStocks);
			                p.sendMessage(messages.getPluginFooter());
			                
			            }
			            
			        }
					
				}else {
					p.sendMessage(messages.getPluginPrefix() + messages.getInvalidCompany());
				}
			
			}else {
				p.sendMessage(messages.getPluginPrefix() + messages.getInvalidCmd());
			}

		}else {
			p.sendMessage(messages.getPluginPrefix() + messages.getNoPermission());
		}
		
	}
	
}
