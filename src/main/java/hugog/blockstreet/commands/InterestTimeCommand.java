package hugog.blockstreet.commands.implementation;

import hugog.blockstreet.Main;
import hugog.blockstreet.commands.CmdDependencyInjector;
import hugog.blockstreet.commands.PluginCommand;
import hugog.blockstreet.runnables.InterestRateRunnable;
import hugog.blockstreet.others.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

/**
 * Interest Time Command
 *
 * Command to check how much time is left till the interests are paid.
 *
 * Syntax: /invest time
 *
 * @author Hugo1307
 * @since v1.0.0
 */
public class InterestTimeCommand extends PluginCommand {

	public InterestTimeCommand(CommandSender sender, String[] args, CmdDependencyInjector cmdDependencyInjector) {
		super(sender, args, cmdDependencyInjector);
	}

	@Override
	public void execute() {

		Player player = (Player) sender;
		Messages messages = new Messages();

		if (player.isOp() || player.hasPermission("blockstreet.command.time") || player.hasPermission("blockstreet.command.*")) {

			int totalMinutes = Main.getInstance().getConfig().getInt("BlockStreet.Timer");
			int timeElapsed = InterestRateRunnable.getMinutesCounter();
			int timeLeft = totalMinutes - timeElapsed;

			player.sendMessage(messages.getPluginPrefix() + MessageFormat.format(messages.getInterestRateTimeLeft(), timeLeft));

		}else {
			player.sendMessage(messages.getPluginPrefix() + messages.getNoPermission());
		}

	}

}
