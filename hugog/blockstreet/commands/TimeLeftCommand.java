package hugog.blockstreet.commands;

import java.text.MessageFormat;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import hugog.blockstreet.Main;
import hugog.blockstreet.others.Interest_Rate;
import hugog.blockstreet.others.Messages;

public class TimeLeftCommand {

	private Main main;
	private CommandSender sender;
	
	public TimeLeftCommand(Main main, CommandSender sender) {
		
		this.main = main;
		this.sender = sender;
		
	}
	
	void runTimeLeftCommand() {
		
		Player p = (Player) sender;
		Messages messages = new Messages(main.messagesConfig);
		
		if (p.hasPermission("blockstreet.command.time") || p.hasPermission("blockstreet.command.*")) {
			
			int totalMinutes = main.getConfig().getInt("BlockStreet.Timer");
			int timeElapsed = Interest_Rate.minutesCounter;
			int timeLeft = totalMinutes - timeElapsed;
			
			p.sendMessage(messages.getPluginPrefix() + MessageFormat.format(messages.getInterestRateTimeLeft(), timeLeft));
			
		}else {
			
			p.sendMessage(messages.getPluginPrefix() + messages.getNoPermission());
			
		}
		
	}
	
}
