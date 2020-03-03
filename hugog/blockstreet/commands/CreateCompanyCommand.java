package hugog.blockstreet.commands;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mysql.fabric.xmlrpc.base.Array;

import hugog.blockstreet.Main;
import hugog.blockstreet.others.Messages;

public class CreateCompanyCommand {

	private Main Main;
	private String[] args;
	private CommandSender sender;
	
	public CreateCompanyCommand(Main main, String[] args, CommandSender sender) {
		this.Main = main;
		this.args = args;
		this.sender = sender;
	}
	
	public void runCreateCompanyCommand() {
		
		// /invest create {companyName} {price} {risk}
		
		Player p = (Player) sender;
		Messages messages = new Messages(Main.messagesConfig);
		int numberOfCompanies = Main.getConfig().getInt("BlockStreet.Companies.Count");	
		
		if (p.hasPermission("blockstreet.admin.*") || p.hasPermission("blockstreet.admin.createcompany")) {
			
			if (args.length <= 1) {
				p.sendMessage(messages.getPluginPrefix() + messages.getMissingArguments());
				
			}else {
			
				String companyName = args[1];
				int actionsPrice;
				int companyRisk;
				int availableActions = -1;
				List<String> Variations = new ArrayList<String>(Arrays.asList("+ " + 2.3 + "%", "+ " + 4.8 + "%",
						"- " + 2.4 + "%", "- " + 0.9 + "%", "+ " + 1.1 + "%"));
				List<String> companiesNames = new ArrayList<String>();
				
				if (args[2] != null && args[3] != null) {
					
					try {
						actionsPrice = Integer.parseInt(args[2]);
						companyRisk = Integer.parseInt(args[3]);
					}catch (Exception e) {
						p.sendMessage(messages.getPluginPrefix() + messages.getWrongArguments());
						return;
					}
					
					for (int i = 0; i <= numberOfCompanies; i++) {
						companiesNames.add(Main.getConfig().getString("BlockStreet.Companies." + i + ".Name"));
					}
					
					if (!companiesNames.contains(companyName)) {
						
						if (0 < companyRisk && companyRisk <= 5) {
							Main.getConfig().set("BlockStreet.Companies.Count", ++numberOfCompanies);
							
							Main.getConfig().addDefault("BlockStreet.Companies." + numberOfCompanies + ".Name", companyName);
							Main.getConfig().addDefault("BlockStreet.Companies." + numberOfCompanies + ".Price", actionsPrice);
							Main.getConfig().addDefault("BlockStreet.Companies." + numberOfCompanies + ".Risk", companyRisk);
							Main.getConfig().addDefault("BlockStreet.Companies." + numberOfCompanies + ".AvailableActions", availableActions);
							Main.getConfig().addDefault("BlockStreet.Companies." + numberOfCompanies + ".Variations", Variations);
							
							Main.saveConfig();
							
							p.sendMessage(messages.getPluginPrefix() + MessageFormat.format(messages.getCreatedCompany().replace("'", "''"), companyName));
						
						}else {
							
							p.sendMessage(messages.getPluginPrefix() + messages.getWrongArguments());
							
						}
						
					}else {
						
						p.sendMessage(messages.getPluginPrefix() + messages.getCompanyAlreadyExists());
						
					}
							
				} else {
					
					p.sendMessage(messages.getPluginPrefix() + messages.getMissingArguments());
					
				}
				
			}
			
		}else {
			p.sendMessage(messages.getPluginPrefix() + messages.getNoPermission());
		}	
			
	}
	
}
