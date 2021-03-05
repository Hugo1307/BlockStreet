package hugog.blockstreet.commands;

import hugog.blockstreet.Main;
import hugog.blockstreet.others.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdImplementer implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (!(sender instanceof Player)) {
        	sender.sendMessage("[BlockStreet] Player Command only.");
			return false;
		}
        
        if (args.length == 0) {
        	new MainCommand(sender).runMainCommand();
			return true;
        }else if (args[0].equalsIgnoreCase("companies")) {
        	new CompaniesCommand(args, sender).runCompaniesCommand();
        	return true;
        }else if (args[0].equalsIgnoreCase("company")) {
        	new CompanyCommand(args, sender).runCompanyCommand();
        	return true;
        }else if (args[0].equalsIgnoreCase("buy")) {
        	new BuyCommand(args, sender).runBuyCommand();
        	return true;
        }else if (args[0].equalsIgnoreCase("sell")) {
        	new SellCommand(args, sender).runSellCommand();
        	return true;
        }else if (args[0].equalsIgnoreCase("stocks")) {
        	new StocksCommand(sender).runStocksCommand();
        	return true;
        }else if (args[0].equalsIgnoreCase("create")) {
        	new CreateCompanyCommand(args, sender).runCreateCompanyCommand();
        	return true;
        }else if (args[0].equalsIgnoreCase("delete")) {
        	new DeleteCompanyCommand(args, sender).runDeleteCompanyCmd();
        	return true;
        }else if (args[0].equalsIgnoreCase("time")) {
        	new TimeLeftCommand(sender).runTimeLeftCommand();
        	return true;
        }else if (args[0].equalsIgnoreCase("reload")) {
        	new ReloadCommand(sender).runReloadCommand();
        	return true;
        }else if (args[0].equalsIgnoreCase("info")) {
        	new InfoCommand(sender).runInfoCommand();
        	return true;
        }else {       	
        	Messages messages = new Messages(Main.getInstance().messagesConfig);
        	sender.sendMessage(messages.getPluginPrefix() + ChatColor.RED + messages.getUnknownCommand());
        	return false;
        }

    }
}
