package hugog.blockstreet.commands;

import hugog.blockstreet.Main;
import hugog.blockstreet.others.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

public class SellCommand {

	private final String[] args;
	private final CommandSender sender;
	
	public SellCommand (String[] args, CommandSender sender) {
		this.args = args;
		this.sender = sender;
	}

	public void runSellCommand() {
		
		// /invest sell [amount] [Id] 
		
		Player p = (Player) sender;
		Messages messages = new Messages(Main.getInstance().messagesConfig);
		
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

	            currentCompany = new Company(companyId).load();
	            
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

		    	                Main.getInstance().economy.depositPlayer(p, pricePerAction * sellingAmount);
		    	                
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
