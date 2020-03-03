package hugog.blockstreet.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import hugog.blockstreet.Main;
import hugog.blockstreet.others.Messages;

public class CompanyCommand {

	private Main Main;
	private String[] args;
	private CommandSender sender;
	
	public CompanyCommand (Main main, String[] args, CommandSender sender) {
		this.Main = main;
		this.args = args;
		this.sender = sender;
	}
	
	protected void runCompanyCommand () {
		
		Player p = (Player) sender;
		Messages messages = new Messages(Main.messagesConfig);
		int numberOfCompanies = Main.getConfig().getInt("BlockStreet.Companies.Count");	
		
		if (!p.hasPermission("blockstreet.command.company") || p.hasPermission("blockstreet.command.*")) {
			p.sendMessage(messages.getPluginPrefix() + messages.getNoPermission());
			return;
		}
		
		if (args.length == 1){
            p.sendMessage(messages.getPluginPrefix() + messages.getInvalidCmd());
            return;
        }

        for (int i = 1; i <= numberOfCompanies; i++) {
        	
            if(args[1].equalsIgnoreCase(String.valueOf(i))){
            	
                p.sendMessage(messages.getPluginHeader());
                p.sendMessage("");
                p.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD +
                        Main.getConfig().getString("BlockStreet.Companies." + i + ".Name"));
                p.sendMessage("");
                p.sendMessage(ChatColor.GRAY + "Id: " + ChatColor.GREEN + i);
                p.sendMessage(ChatColor.GRAY + messages.getPrice() + ": " + ChatColor.GREEN +
                        Main.getConfig().getString("BlockStreet.Companies." + i + ".Price"));
                p.sendMessage(ChatColor.GRAY + messages.getRisk() + ": " + ChatColor.GREEN +
                        Main.getConfig().getString("BlockStreet.Companies." + i + ".Risk"));
                if (Main.getConfig().getInt("BlockStreet.Companies." + i + ".AvailableActions") < 0){
                    p.sendMessage(ChatColor.GRAY + messages.getAvailableActions() + ": " + ChatColor.GREEN + "Unlimited");
                }else{
                    p.sendMessage(ChatColor.GRAY + messages.getAvailableActions() + ": " + ChatColor.GREEN +
                            Main.getConfig().getString("BlockStreet.Companies." + i + ".AvailableActions"));
                }

                p.sendMessage(ChatColor.GRAY + messages.getActionHistoric() + ": ");
                p.sendMessage("");
                for (String element : Main.getConfig().getStringList("BlockStreet.Companies." + i + ".Variations")){
                    if(element.contains("+")){
                        p.sendMessage(ChatColor.GREEN + "  " + element);
                    }else if (element.contains("-")){
                        p.sendMessage(ChatColor.RED + "  " + element);
                    }
                }
                p.sendMessage("");
                p.sendMessage(ChatColor.GREEN + "/invest buy {amount} " + i + ChatColor.GRAY +
                        " - " + messages.getBuyActionsCmd());
                p.sendMessage(messages.getPluginFooter());
                return;
            }
        }
        
        p.sendMessage(messages.getPluginPrefix() + messages.getInvalidCompany());
        return;
		
	}
	
}
