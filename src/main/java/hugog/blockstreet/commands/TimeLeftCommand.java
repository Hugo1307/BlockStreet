package hugog.blockstreet.commands;

import hugog.blockstreet.Main;
import hugog.blockstreet.runnables.InterestRateRunnable;
import hugog.blockstreet.others.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

public class TimeLeftCommand {

	private final CommandSender sender;
	
	public TimeLeftCommand(CommandSender sender) {
		this.sender = sender;
	}
	
	void runTimeLeftCommand() {
		
		Player p = (Player) sender;
		Messages messages = new Messages(Main.getInstance().messagesConfig);
		
		if (p.hasPermission("blockstreet.command.time") || p.hasPermission("blockstreet.command.*")) {
			
			int totalMinutes = Main.getInstance().getConfig().getInt("BlockStreet.Timer");
			int timeElapsed = InterestRateRunnable.getMinutesCounter();
			int timeLeft = totalMinutes - timeElapsed;
			
			p.sendMessage(messages.getPluginPrefix() + MessageFormat.format(messages.getInterestRateTimeLeft(), timeLeft));
			
		}else {			
			p.sendMessage(messages.getPluginPrefix() + messages.getNoPermission());			
		}
		
	}
	
}
