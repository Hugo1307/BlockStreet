package hugog.blockstreet.commands;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import hugog.blockstreet.Main;
import hugog.blockstreet.others.ConfigAccessor;
import hugog.blockstreet.others.Messages;

public class ActionsCommand {

	private Main Main;
	private CommandSender sender;
	
	public ActionsCommand(Main main, CommandSender sender) {
		
		this.Main = main;
		this.sender = sender;
		
	}
	
	public void runActionsCommand() {
		
		Player p = (Player) sender;
		Messages messages = new Messages(Main.messagesConfig);
		ConfigAccessor playerReg = new ConfigAccessor(Main, "players.yml");
		
		Set<String> playerCompanies_Id = new HashSet<String>();
		
		if (!p.hasPermission("blockstreet.command.actions") && !p.hasPermission("blockstreet.command.*")) {
			p.sendMessage(messages.getPluginPrefix() + messages.getNoPermission());
			return;
		}
		
		try {
			playerCompanies_Id = playerReg.getConfig().getConfigurationSection("Players." + p.getName() + ".Companies").getKeys(false); 
		} catch (Exception e) {
			p.sendMessage(messages.getPluginPrefix() + messages.getPlayerNoActions());
			return;
		}

		if (playerCompanies_Id.size() != 0){
		    p.sendMessage(messages.getPluginHeader());
		    p.sendMessage("");
		    for (String company : playerCompanies_Id){
			p.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD +
				Main.getConfig().getString("BlockStreet.Companies." + company + ".Name"));
			p.sendMessage("");
			p.sendMessage(ChatColor.GRAY + "Actions: " + ChatColor.GREEN +
				playerReg.getConfig().getInt("Players." + p.getName() + ".Companies." + company + ".Amount"));
			p.sendMessage(ChatColor.GRAY + "Total value: " + ChatColor.GREEN +
				playerReg.getConfig().getDouble("Players." + p.getName() + ".Companies." + company + ".Value"));
			p.sendMessage("");
		    }
		    p.sendMessage(messages.getPluginFooter());

		}else{
		    p.sendMessage(messages.getPluginPrefix() + messages.getPlayerNoActions());

		}
		
	}
	
}
