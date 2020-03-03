package hugog.blockstreet.commands;

import java.text.MessageFormat;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import hugog.blockstreet.Main;
import hugog.blockstreet.others.ConfigAccessor;
import hugog.blockstreet.others.Messages;

public class BuyCommand {

	private Main Main;
	private String[] args;
	private CommandSender sender;
	
	public BuyCommand (Main main, String[] args, CommandSender sender) {
		this.Main = main;
		this.args = args;
		this.sender = sender;
	}
	
	public void runBuyCommand () {
		
		Player p = (Player) sender;
		Messages messages = new Messages(Main.messagesConfig);
		ConfigAccessor playerReg = new ConfigAccessor(Main, "players.yml");
		int numberOfCompanies = Main.getConfig().getInt("BlockStreet.Companies.Count");	
    	
		if (!p.hasPermission("blockstreet.command.buy") || p.hasPermission("blockstreet.command.*")) {
			p.sendMessage(messages.getPluginPrefix() + messages.getNoPermission());
			return;
		}
		
        if (args.length == 1 || args.length == 2){
        	
            p.sendMessage(messages.getPluginPrefix() + messages.getMissingArguments());
            
        } else {
        	
        	 int amount, company;
             
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

             double price = Main.getConfig().getDouble("BlockStreet.Companies." + company +".Price");
             double playerMoney = Main.economy.getBalance(p.getName());
             int availableActions = Main.getConfig().getInt("BlockStreet.Companies." + company +".AvailableActions");
             int playerCompanyActionsAmount = playerReg.getConfig().getInt("Players." + p.getName() + ".Companies." + company + ".Amount");
             int playerCompanyActionsValue = playerReg.getConfig().getInt("Players." + p.getName() + ".Companies." + company + ".Value");
             
             
             if (availableActions != -1){
                 if (availableActions >= amount){
                     if (playerMoney >= amount*price){
                    	 
                    	 if (playerReg.getConfig().getInt("Players." + p.getName() + ".Companies." + company + ".Amount") != 0) {
                    		 amount += playerReg.getConfig().getInt("Players." + p.getName() + ".Companies." + company + ".Amount");
                    	 }
                    	 
                         Main.economy.withdrawPlayer(p.getName(), amount * price);
                         playerReg.getConfig().set("Players." + p.getName() + ".Companies." + company + ".Amount", amount + playerCompanyActionsAmount);
                         playerReg.getConfig().set("Players." + p.getName() + ".Companies." + company +
                                 ".Value", amount * price + playerCompanyActionsValue);
                         Main.getConfig().set("BlockStreet.Companies." + company +".AvailableActions", Main.getConfig().getInt("BlockStreet.Companies." + company +".AvailableActions") - amount);
                         playerReg.saveConfig();
                         Main.saveConfig();
                         playerReg.reloadConfig();
                         Main.reloadConfig();
                         p.sendMessage(messages.getPluginPrefix() + MessageFormat.format(messages.getBoughtActions().replace("'", "''"), amount));

                     }else{
                         p.sendMessage(messages.getPluginPrefix() + messages.getInsufficientMoney());
                     }
                 }else{
                     p.sendMessage(messages.getPluginPrefix() + messages.getInsufficientActions());
                 }
             }else{
                 if (playerMoney >= amount*price){
                     Main.economy.withdrawPlayer(p.getName(), amount * price);
                     playerReg.getConfig().set("Players." + p.getName() + ".Companies." + company + ".Amount", amount + playerCompanyActionsAmount);
                     playerReg.getConfig().set("Players." + p.getName() + ".Companies." + company +
                             ".Value", amount * price + playerCompanyActionsValue);
                     playerReg.saveConfig();
                     playerReg.reloadConfig();
                     p.sendMessage(messages.getPluginPrefix() + MessageFormat.format(messages.getBoughtActions().replace("'", "''"), amount));

                 }else{
                     p.sendMessage(messages.getPluginPrefix() + messages.getInsufficientMoney());
                 }
             }
        	
        }
		
	}
	
}
