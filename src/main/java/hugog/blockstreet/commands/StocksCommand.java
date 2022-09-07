package hugog.blockstreet.commands.implementation;

import hugog.blockstreet.commands.CmdDependencyInjector;
import hugog.blockstreet.commands.PluginCommand;
import hugog.blockstreet.others.*;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Stocks Command
 *
 * Command which allow players to check their stocks.
 *
 * Syntax: /invest stocks
 *
 * @author Hugo1307
 * @version v1.0.0
 */
public class StocksCommand extends PluginCommand {

	public StocksCommand(CommandSender sender, String[] args, CmdDependencyInjector cmdDependencyInjector) {
		super(sender, args, cmdDependencyInjector);
	}

	@Override
	public void execute() {

		Player player = (Player) sender;
		Messages messages = new Messages();

		if (player.isOp() || player.hasPermission("blockstreet.command.actions") || player.hasPermission("blockstreet.command.*")) {

			Investor playerInvestProfile = new Investor(player.getName());

			if (playerInvestProfile.getInvestments().size() > 0) {

				player.sendMessage(messages.getPluginHeader());
				player.sendMessage("");

				for (Investment investment : playerInvestProfile.getInvestments()) {

					Company investedCompany = new Company(investment.getId()).load();

					player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + investedCompany.getName());
					player.sendMessage("");
					player.sendMessage(ChatColor.GRAY + "Actions: " + ChatColor.GREEN + investment.getStocksAmount());
					player.sendMessage(ChatColor.GRAY + "Total value: " + ChatColor.GREEN + (investment.getStocksAmount()*investedCompany.getStocksPrice()));
					player.sendMessage("");
				}
				player.sendMessage(messages.getPluginFooter());

			}else {
				player.sendMessage(messages.getPluginPrefix() + messages.getPlayerAnyActions());
			}

		}else {
			player.sendMessage(messages.getPluginPrefix() + messages.getNoPermission());
		}

	}

}
