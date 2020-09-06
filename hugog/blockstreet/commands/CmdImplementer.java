package hugog.blockstreet.commands;

import hugog.blockstreet.Main;
import hugog.blockstreet.others.Messages;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdImplementer implements CommandExecutor {

    public Main plugin;
    public CmdImplementer(Main plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (!(sender instanceof Player)) {
        	sender.sendMessage("[BlockStreet] Player Command only.");
		return false;
	}
        
        if (args.length == 0) {
        	
        	MainCommand mainCmd = new MainCommand(plugin, sender);
             	mainCmd.runMainCommand();
             	return true;
  
        }else if (args[0].equalsIgnoreCase("companies")) {
        	
        	CompaniesCommand companiesCmd = new CompaniesCommand(plugin, args, sender);
        	companiesCmd.runCompaniesCommand();
        	return true;
        	
        }else if (args[0].equalsIgnoreCase("company")) {
        	
        	CompanyCommand companyCmd = new CompanyCommand(plugin, args, sender);
        	companyCmd.runCompanyCommand();
        	return true;
        	
        }else if (args[0].equalsIgnoreCase("buy")) {
        	
        	BuyCommand buyCmd = new BuyCommand(plugin, args, sender);
        	buyCmd.runBuyCommand();
        	return true;
        	
        }else if (args[0].equalsIgnoreCase("sell")) {
        	
        	SellCommand sellCmd = new SellCommand(plugin, args, sender);
        	sellCmd.runSellCommand();
        	return true;
        	
        }else if (args[0].equalsIgnoreCase("stocks")) {
        	
        	StocksCommand actionsCmd = new StocksCommand(plugin, sender);
        	actionsCmd.runActionsCommand();
        	return true;
        	
        }else if (args[0].equalsIgnoreCase("create")) {
        	
        	CreateCompanyCommand createCmd = new CreateCompanyCommand(plugin, args, sender);
        	createCmd.runCreateCompanyCommand();
        	return true;
        	
        }else if (args[0].equalsIgnoreCase("delete")) {
        	
        	DeleteCompanyCommand deleteCmd = new DeleteCompanyCommand(plugin, args, sender);
        	deleteCmd.runDeleteCompanyCmd();
        	return true;
        	
        }else if (args[0].equalsIgnoreCase("time")) {
        	
        	TimeLeftCommand timeLeftCmd = new TimeLeftCommand(plugin, sender);
        	timeLeftCmd.runTimeLeftCommand();
        	return true;
        	
        }else if (args[0].equalsIgnoreCase("reload")) {
        	
        	ReloadCommand reloadCmd = new ReloadCommand(plugin, sender);
        	reloadCmd.runReloadCommand();
        	return true;
        	
        }else if (args[0].equalsIgnoreCase("info")) {
        	
        	InfoCommand infoCmd = new InfoCommand(plugin, sender);
        	infoCmd.runInfoCommand();
        	return true;
        	
        }else {       	
        	Messages messages = new Messages(plugin.messagesConfig);        	
        	((Player) sender).sendMessage(messages.getPluginPrefix() + ChatColor.RED + messages.getUnknownCommand());
        	return false;
        }

    }
}
