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

public class SellCommand {

	private Main main;
	private String[] args;
	private CommandSender sender;
	
	public SellCommand (Main main, String[] args, CommandSender sender) {
		this.main = main;
		this.args = args;
		this.sender = sender;
	}

	public void runSellCommand() {
		
		// /invest sell [amount] [Id] 
		
		Player p = (Player) sender;
		Messages messages = new Messages(main.messagesConfig);
		ConfigAccessor companiesReg = new ConfigAccessor(main, "companies.yml");
		
		if (p.hasPermission("blockstreet.command.sell") || p.hasPermission("blockstreet.command.*")) {
			
			if (args.length >= 3) {
				
				int sellingAmount, companyId;
				Company currentCompany;

	            try{
	                sellingAmount = Integer.parseInt(args[1]);
	                companyId = Integer.parseInt(args[2]);
	            }catch (NumberFormatException nfe){
	                p.sendMessage(messages.getPluginPrefix() + messages.getWrongArguments());
	                return;
	            }

	            currentCompany = new Company(companiesReg, companyId);
	            
	            if (currentCompany.exists()) {
	            	
	            	if (sellingAmount > 0) {
	            		
	            		Investor playerInvestorProfile = new Investor(p.getName());
	            		Investment currentInvestment = playerInvestorProfile.getInvestment(currentCompany.getId());
	            		
	            		if (currentInvestment != null) {
	            			
	            			int playerActions = currentInvestment.getStocksAmount();
	            			
	            			if (sellingAmount <= playerActions && playerActions != 0){
	            				         				
		    	                double pricePerAction = currentCompany.getStocksPrice();
		    	                
		    	                playerInvestorProfile.addInvestment(new Investment(currentCompany.getId(), playerActions - sellingAmount));		    	                
		    	                playerInvestorProfile.saveToYml();

		    	                main.economy.depositPlayer(p, pricePerAction * sellingAmount);
		    	                
		    	                p.sendMessage(messages.getPluginPrefix() + MessageFormat.format(messages.getSoldActions().replace("'", "''"), sellingAmount, pricePerAction * sellingAmount));		    	                
		    	                
		    	            }else{
		    	                p.sendMessage(messages.getPluginPrefix() + messages.getPlayerNoActions());	    	                
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
