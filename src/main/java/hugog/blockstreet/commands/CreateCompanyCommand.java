package hugog.blockstreet.commands;

import hugog.blockstreet.Main;
import hugog.blockstreet.enums.ConfigurationFiles;
import hugog.blockstreet.others.Company;
import hugog.blockstreet.others.ConfigAccessor;
import hugog.blockstreet.others.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

public class CreateCompanyCommand {

	private final String[] args;
	private final CommandSender sender;
	
	public CreateCompanyCommand(String[] args, CommandSender sender) {
		this.args = args;
		this.sender = sender;
	}
	
	public void runCreateCompanyCommand() {
		
		// invest create {companyName} {price} {risk}
		
		Player p = (Player) sender;
		Messages messages = new Messages(Main.getInstance().messagesConfig);
		
		if (p.hasPermission("blockstreet.admin.createcompany") || p.hasPermission("blockstreet.admin.*")) {
			
			if (args.length >= 4) {
				
				String companyName = args[1];
				int stocksPrice;
				int companyRisk;
				Company newCompany;
				
				try {
					stocksPrice = Integer.parseInt(args[2]);
					companyRisk = Integer.parseInt(args[3]);
					newCompany = new Company(companyName, companyRisk, stocksPrice);
				}catch (Exception e) {
					p.sendMessage(messages.getPluginPrefix() + messages.getWrongArguments());
					return;
				}

				newCompany.save();
				p.sendMessage(messages.getPluginPrefix() + MessageFormat.format(messages.getCreatedCompany(), newCompany.getName()));

			}else {
				p.sendMessage(messages.getPluginPrefix() + messages.getMissingArguments());				
			}
			
		}else {
			p.sendMessage(messages.getPluginPrefix() + messages.getNoPermission());
		}	
			
	}
	
}
