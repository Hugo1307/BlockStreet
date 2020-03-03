package hugog.blockstreet.commands;

import java.text.MessageFormat;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import hugog.blockstreet.Main;
import hugog.blockstreet.others.ConfigAccessor;
import hugog.blockstreet.others.Messages;

public class SellCommand {

	private Main Main;
	private String[] args;
	private CommandSender sender;
	
	public SellCommand (Main main, String[] args, CommandSender sender) {
		this.Main = main;
		this.args = args;
		this.sender = sender;
	}

	public void runSellCommand() {
		
		Player p = (Player) sender;
		Messages messages = new Messages(Main.messagesConfig);
		ConfigAccessor playerReg = new ConfigAccessor(Main, "players.yml");
		int numberOfCompanies = Main.getConfig().getInt("BlockStreet.Companies.Count");	
		
		if (!p.hasPermission("blockstreet.command.sell") || p.hasPermission("blockstreet.command.*")) {
			p.sendMessage(messages.getPluginPrefix() + messages.getNoPermission());
			return;
		}
		
		
		if (args.length == 1 || args.length == 2){
			
            p.sendMessage(messages.getPluginPrefix() + messages.getMissingArguments());
            
        } else {
        	
        	int amount;
            int company;
            try{
                amount = Integer.parseInt(args[1]);
                company = Integer.parseInt(args[2]);
            }catch (NumberFormatException nfe){
                p.sendMessage(messages.getPluginPrefix() + messages.getWrongArguments());
                return;
            }

            if (company > numberOfCompanies){
                p.sendMessage(messages.getPluginPrefix() + messages.getInvalidCompany());
                return;
            }
            
            if (amount == 0) {
           	 p.sendMessage(messages.getPluginPrefix() + messages.getWrongArguments());
           	 return;
            }

            int playerActions = 0;
            try{
                playerActions = playerReg.getConfig().getInt("Players." + p.getName() + ".Companies." + company + ".Amount");
            }catch (Exception ignored){
                p.sendMessage(messages.getPluginPrefix() + messages.getInsufficientActions());
                return;
            }

            if (amount <= playerActions && playerActions != 0){

                double pricePerAction = (double) (playerReg.getConfig().getInt("Players." + p.getName() +
                        ".Companies." + company + ".Value") / playerActions);
                playerActions -= amount;
                playerReg.getConfig().set("Players." + p.getName() + ".Companies." + company + ".Amount", playerActions);
                playerReg.getConfig().set("Players." + p.getName() + ".Companies." + company + ".Value",
                        playerReg.getConfig().getInt("Players." + p.getName() + ".Companies." + company
                                + ".Value") - pricePerAction * amount);
                Main.economy.depositPlayer(p.getName(), pricePerAction * amount);
                playerReg.saveConfig();
                playerReg.reloadConfig();
                p.sendMessage(messages.getPluginPrefix() + MessageFormat.format(messages.getSoldActions().replace("'", "''"), amount, pricePerAction * amount));
                
                if (playerReg.getConfig().getInt("Players." + p.getName() + ".Companies." + company + ".Amount") == 0) {
                	playerReg.getConfig().set("Players." + p.getName() + ".Companies." + company, null);
                	playerReg.saveConfig();
                	playerReg.reloadConfig();
                }
                
            }else{
                p.sendMessage(messages.getPluginPrefix() + messages.getPlayerNoActions());
                
            }
        	
        }

        
	}
	
}
