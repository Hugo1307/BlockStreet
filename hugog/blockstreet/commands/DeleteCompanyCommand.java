package hugog.blockstreet.commands;

import java.text.MessageFormat;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import hugog.blockstreet.Main;
import hugog.blockstreet.others.Company;
import hugog.blockstreet.others.ConfigAccessor;
import hugog.blockstreet.others.Messages;

public class DeleteCompanyCommand {

	private Main main;
	private String[] args;
	private CommandSender sender;
	
	public DeleteCompanyCommand(Main main, String[] args, CommandSender sender) {
		this.main = main;
		this.args = args;
		this.sender = sender;
	}
	
	void runDeleteCompanyCmd() {
		
		Player p = (Player) sender;
		Messages messages = new Messages(main.messagesConfig);
		ConfigAccessor companiesReg = new ConfigAccessor(main, "companies.yml");
		
		if (p.hasPermission("blockstreet.admin.delete") || p.hasPermission("blockstreet.admin.*")) {
			
			if (args.length > 1) {
				
				int companyId;
				
				try {
					companyId = Integer.parseInt(args[1]);
				} catch (NumberFormatException e) {
					p.sendMessage(messages.getPluginPrefix() + messages.getWrongArguments());
					return;
				}
				
				Company companyToDelete = new Company(companiesReg, companyId);
				
				if (companyToDelete.exists()) {
					
					companyToDelete.delete(true);
					
					p.sendMessage(messages.getPluginPrefix() + MessageFormat.format(messages.getDeletedCompany(), 
							ChatColor.GOLD + companyToDelete.getName() + ChatColor.GRAY));
					
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
