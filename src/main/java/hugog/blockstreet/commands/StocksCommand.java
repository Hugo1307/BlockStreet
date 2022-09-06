package hugog.blockstreet.commands;

import hugog.blockstreet.others.*;
import me.hgsoft.minecraft.devcommand.annotations.Command;
import me.hgsoft.minecraft.devcommand.commands.BukkitDevCommand;
import me.hgsoft.minecraft.devcommand.commands.data.BukkitCommandData;
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
@Command(alias = "stocks", permission = "blockstreet.command.actions")
public class StocksCommand extends BukkitDevCommand {

	public StocksCommand(BukkitCommandData commandData, CommandSender commandSender, String[] args) {
		super(commandData, commandSender, args);
	}

	@Override
	public void execute() {

		Player p = (Player) getCommandSender();
		Messages messages = new Messages();

		if (p.hasPermission("blockstreet.command.actions") || p.hasPermission("blockstreet.command.*")) {

			Investor playerInvestProfile = new Investor(p.getName());

			if (playerInvestProfile.getInvestments().size() > 0) {

				p.sendMessage(messages.getPluginHeader());
				p.sendMessage("");

				for (Investment investment : playerInvestProfile.getInvestments()) {

					Company investedCompany = new Company(investment.getId()).load();

					p.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + investedCompany.getName());
					p.sendMessage("");
					p.sendMessage(ChatColor.GRAY + "Actions: " + ChatColor.GREEN + investment.getStocksAmount());
					p.sendMessage(ChatColor.GRAY + "Total value: " + ChatColor.GREEN + (investment.getStocksAmount()*investedCompany.getStocksPrice()));
					p.sendMessage("");
				}
				p.sendMessage(messages.getPluginFooter());

			}else {
				p.sendMessage(messages.getPluginPrefix() + messages.getPlayerAnyActions());
			}

		}else {
			p.sendMessage(messages.getPluginPrefix() + messages.getNoPermission());
		}

	}

}
