package hugog.blockstreet.commands;

import java.text.MessageFormat;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import hugog.blockstreet.Main;
import hugog.blockstreet.others.Company;
import hugog.blockstreet.others.ConfigAccessor;
import hugog.blockstreet.others.Investment;
import hugog.blockstreet.others.Investor;
import hugog.blockstreet.others.Messages;

public class BuyCommand {

	private Main main;
	private String[] args;
	private CommandSender sender;
	
	public BuyCommand (Main main, String[] args, CommandSender sender) {
		this.main = main;
		this.args = args;
		this.sender = sender;
	}
	
	public void runBuyCommand () {
		
		// /invest buy [amount] [companyId]
		
		Player p = (Player) sender;
		Messages messages = new Messages(main.messagesConfig);
		ConfigAccessor companiesReg = new ConfigAccessor(main, "companies.yml");
		
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
	                
	            	Company currentCompany = new Company(companiesReg, companyId); 
	        		double playerMoney = main.economy.getBalance(p);
	            	
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
	                			
	                			main.economy.withdrawPlayer(p, amount * currentCompany.getStocksPrice());
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
