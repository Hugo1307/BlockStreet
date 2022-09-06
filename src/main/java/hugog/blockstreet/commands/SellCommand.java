package hugog.blockstreet.commands;

import hugog.blockstreet.Main;
import hugog.blockstreet.others.*;
import me.hgsoft.minecraft.devcommand.annotations.Command;
import me.hgsoft.minecraft.devcommand.commands.BukkitDevCommand;
import me.hgsoft.minecraft.devcommand.commands.data.BukkitCommandData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

/**
 * Sell Command
 *
 * Command which allow players to sell stocks.
 *
 * Syntax: /invest sell [amount] [ID]
 *
 * @author Hugo1307
 * @since v1.0.0
 */
@Command(alias = "sell", permission = "blockstreet.command.sell")
public class SellCommand extends BukkitDevCommand {

	public SellCommand(BukkitCommandData commandData, CommandSender commandSender, String[] args) {
		super(commandData, commandSender, args);
	}

	@Override
	public void execute() {

		Player p = (Player) getCommandSender();
		Messages messages = new Messages();

		if (p.hasPermission("blockstreet.command.sell") || p.hasPermission("blockstreet.command.*")) {

			if (getArgs().length >= 3) {

				int sellingAmount, companyId;
				Company currentCompany;

				try{
					sellingAmount = Integer.parseInt(getArgs()[1]);
					companyId = Integer.parseInt(getArgs()[2]);
				}catch (NumberFormatException nfe){
					p.sendMessage(messages.getPluginPrefix() + messages.getWrongArguments());
					return;
				}

				currentCompany = new Company(companyId).load();

				if (currentCompany.exists()) {

					if (sellingAmount > 0) {

						Investor playerInvestorProfile = new Investor(p.getName());
						Investment currentInvestment = playerInvestorProfile.getInvestment(currentCompany.getId());

						if (currentInvestment != null) {

							int playerActions = currentInvestment.getStocksAmount();

							if (sellingAmount <= playerActions && playerActions != 0){

								double pricePerAction = currentCompany.getStocksPrice();

								playerInvestorProfile.addInvestment(new Investment(currentCompany.getId(), playerActions - sellingAmount));
								playerInvestorProfile.saveToYml();

								Main.getInstance().economy.depositPlayer(p, pricePerAction * sellingAmount);

								p.sendMessage(messages.getPluginPrefix() + MessageFormat.format(messages.getSoldActions().replace("'", "''"), sellingAmount, pricePerAction * sellingAmount));

							}else{
								p.sendMessage(messages.getPluginPrefix() + messages.getPlayerNoActions());
							}

						}else {
							p.sendMessage(messages.getPluginPrefix() + messages.getInsufficientActions());
						}

					}else {
						p.sendMessage(messages.getPluginPrefix() + messages.getWrongArguments());
					}

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
