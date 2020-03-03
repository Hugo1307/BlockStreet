package hugog.blockstreet.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import hugog.blockstreet.Main;
import hugog.blockstreet.others.Messages;

public class CompaniesCommand{

	private Main Main;
	private String[] args;
	private CommandSender sender;
	
	public CompaniesCommand(Main main, String[] args, CommandSender sender) {
		this.Main = main;
		this.args = args;
		this.sender = sender;
	}
	
	protected void runCompaniesCommand() {
		
		Player p = (Player) sender;
		Messages messages = new Messages(Main.messagesConfig);
		int numberOfCompanies = Main.getConfig().getInt("BlockStreet.Companies.Count");	
		
		if (!p.hasPermission("blockstreet.command.companies") || p.hasPermission("blockstreet.command.*")) {
			p.sendMessage(messages.getPluginPrefix() + messages.getNoPermission());
			return;
		}
		
		if(numberOfCompanies <= 0){
            p.sendMessage(messages.getPluginPrefix() + messages.getNonExistantPage());
            return;
        }
		
		if (args.length == 1) {
			
            p.sendMessage(messages.getPluginHeader());
            for (int companyIndex = 1; companyIndex <= 3 ; companyIndex++) {
                if(Main.getConfig().getString("BlockStreet.Companies." + companyIndex + ".Name") != null) {
                    p.sendMessage(ChatColor.GREEN +
                            Main.getConfig().getString("BlockStreet.Companies." + companyIndex + ".Name"));
                    p.sendMessage(ChatColor.GRAY + messages.getPrice() + ": " + ChatColor.GREEN +
                            Main.getConfig().getString("BlockStreet.Companies." + companyIndex + ".Price"));
                    p.sendMessage(ChatColor.GRAY + messages.getRisk() + ": " + ChatColor.GREEN +
                            Main.getConfig().getString("BlockStreet.Companies." + companyIndex + ".Risk"));
                    p.sendMessage(ChatColor.GRAY + messages.getDetails() + ": " + ChatColor.GREEN +
                            "/invest company " + companyIndex);
                    p.sendMessage("");
                }
            }
            
            if(numberOfCompanies > 3){
                p.sendMessage(ChatColor.GREEN + "/invest companies 1" + ChatColor.GRAY + " - " + messages.getListNextPage());
            }
            p.sendMessage(messages.getPluginFooter());
            return;
		
		}else if (args.length > 1) {
			
			for (int i = 0; i <= (int)(numberOfCompanies/3); i++) {
				
				if (args[1].equalsIgnoreCase(String.valueOf(i))) {
					
					p.sendMessage(messages.getPluginHeader());
	                for (int companyIndex = i * 3 + 1; companyIndex <= i * 3 + 3; companyIndex++) {
	                    if(Main.getConfig().getString("BlockStreet.Companies." + companyIndex + ".Name") != null) {
	                        p.sendMessage(ChatColor.GREEN +
	                                Main.getConfig().getString("BlockStreet.Companies." + companyIndex + ".Name"));
	                        p.sendMessage(ChatColor.GRAY + messages.getPrice() + ": " + ChatColor.GREEN +
	                                Main.getConfig().getString("BlockStreet.Companies." + companyIndex + ".Price"));
	                        p.sendMessage(ChatColor.GRAY + messages.getRisk() + ": " + ChatColor.GREEN +
	                                Main.getConfig().getString("BlockStreet.Companies." + companyIndex + ".Risk"));
	                        p.sendMessage(ChatColor.GRAY + messages.getDetails() + ": " + ChatColor.GREEN +
	                                "/invest company " + companyIndex);
	                        p.sendMessage("");
	                    }
	                }
	                p.sendMessage(ChatColor.GREEN + "/invest companies " + (i + 1) + ChatColor.GRAY + " - " + messages.getListNextPage());
	                p.sendMessage(messages.getPluginFooter());
	                return;
					
				}
				
			}
			
			p.sendMessage(messages.getPluginPrefix() + messages.getNonExistantPage());
			return;
			
		}
		
	}
	
}
