package hugog.blockstreet.commands;

import java.text.MessageFormat;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import hugog.blockstreet.Main;
import hugog.blockstreet.others.Company;
import hugog.blockstreet.others.ConfigAccessor;
import hugog.blockstreet.others.Messages;

public class CreateCompanyCommand {

	private Main main;
	private String[] args;
	private CommandSender sender;
	
	public CreateCompanyCommand(Main main, String[] args, CommandSender sender) {
		this.main = main;
		this.args = args;
		this.sender = sender;
	}
	
	public void runCreateCompanyCommand() {
		
		// invest create {companyName} {price} {risk}
		
		Player p = (Player) sender;
		Messages messages = new Messages(main.messagesConfig);
		ConfigAccessor companyReg = new ConfigAccessor(main, "companies.yml");
		
		if (p.hasPermission("blockstreet.admin.createcompany") || p.hasPermission("blockstreet.admin.*")) {
			
			if (args.length >= 4) {
				
				String companyName = args[1];
				int stocksPrice;
				int companyRisk;
				Company newCompany;
				
				try {
					stocksPrice = Integer.parseInt(args[2]);
					companyRisk = Integer.parseInt(args[3]);
					newCompany = new Company(companyReg, companyName, companyRisk, stocksPrice);
				}catch (Exception e) {
					p.sendMessage(messages.getPluginPrefix() + messages.getWrongArguments());
					return;
				}

				newCompany.saveToYML();
				
				p.sendMessage(messages.getPluginPrefix() + MessageFormat.format(messages.getCreatedCompany(), newCompany.getName()));

			}else {
				p.sendMessage(messages.getPluginPrefix() + messages.getMissingArguments());				
			}
			
		}else {
			p.sendMessage(messages.getPluginPrefix() + messages.getNoPermission());
		}	
			
	}
	
}
