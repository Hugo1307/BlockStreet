package hugog.blockstreet.commands;

import hugog.blockstreet.Main;
import hugog.blockstreet.others.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

public class BuyCommand {

	private final String[] args;
	private final CommandSender sender;
	
	public BuyCommand (String[] args, CommandSender sender) {
		this.args = args;
		this.sender = sender;
	}
	
	public void runBuyCommand () {
		
		// /invest buy [amount] [companyId]
		
		Player p = (Player) sender;
		Messages messages = new Messages(Main.getInstance().messagesConfig);
		ConfigAccessor companiesReg = new ConfigAccessor(Main.getInstance(), "companies.yml");
		
		if (p.hasPermission("blockstreet.command.buy") || p.hasPermission("blockstreet.command.*")) {
			
			if (args.length > 1) {
				
				int amount, companyId;
				int numberOfCompanies = 0;
	        	 
				if (companiesReg.getConfig().get("Companies") != null) 
					numberOfCompanies = companiesReg.getConfig().getConfigurationSection("Companies").getKeys(false).size();				
	             
	            try{
	                 amount = Integer.parseInt(args[1]);
	                 companyId = Integer.parseInt(args[2]);
	            }catch (NumberFormatException nfe){
	                 p.sendMessage(messages.getPluginPrefix() + messages.getWrongArguments());
	                 return;
	            }

	            if (companyId <= numberOfCompanies){
	                
	            	Company currentCompany = new Company(companyId).load();
	        		double playerMoney = Main.getInstance().economy.getBalance(p);
	            	
	                if (amount > 0) {
	                	
	                	if (currentCompany.getAvailableStocks() > amount || currentCompany.getAvailableStocks() == -1) {
	                		
	                		if (playerMoney >= amount * currentCompany.getStocksPrice()) {
	                			
	                			Investor playerInvestorProfile = new Investor(p.getName());
	                			Investment currentInvestment;
	                			
	                			if (playerInvestorProfile.contains(currentCompany.getId())) {
	                				currentInvestment = playerInvestorProfile.getInvestment(currentCompany.getId());
	                				currentInvestment.setStocksAmount(currentInvestment.getStocksAmount() + amount);
	                			}else {
	                				currentInvestment = new Investment(currentCompany.getId(), amount);
	                			}

								Main.getInstance().economy.withdrawPlayer(p, amount * currentCompany.getStocksPrice());
	                			currentCompany.setAvailableStocks(currentCompany.getAvailableStocks() - amount);
	                			
	                			playerInvestorProfile.addInvestment(currentInvestment);
	                			playerInvestorProfile.saveToYml();
	                			
	                			 p.sendMessage(messages.getPluginPrefix() + MessageFormat.format(messages.getBoughtActions().replace("'", "''"), amount));
	                			
	                		}else {
	                			p.sendMessage(messages.getPluginPrefix() + messages.getInsufficientMoney());
	                		}
	                		
	                	}else {
	                		p.sendMessage(messages.getPluginPrefix() + messages.getInsufficientActions());
	                	}
	                	
	                }else {
	                	p.sendMessage(messages.getPluginPrefix() + messages.getWrongArguments());
	                }
	                
	            }else {
	            	p.sendMessage(messages.getPluginPrefix() + messages.getInvalidCompany());
	            }
				
			}else {
				p.sendMessage(messages.getPluginPrefix() + messages.getMissingArguments());
			}
			
		}else {
			p.sendMessage(messages.getPluginPrefix() + messages.getNoPermission());
		}
		
	}
	
}
