package hugog.blockstreet.commands.implementation;

import hugog.blockstreet.Main;
import hugog.blockstreet.commands.CmdDependencyInjector;
import hugog.blockstreet.commands.PluginCommand;
import hugog.blockstreet.others.*;
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
public class SellCommand extends PluginCommand {

	public SellCommand(CommandSender sender, String[] args, CmdDependencyInjector cmdDependencyInjector) {
		super(sender, args, cmdDependencyInjector);
	}

	@Override
	public void execute() {

		Player player = (Player) sender;
		Messages messages = new Messages();

		if (player.isOp() || player.hasPermission("blockstreet.command.sell") || player.hasPermission("blockstreet.command.*")) {

			if (args.length >= 3) {

				int sellingAmount, companyId;
				Company currentCompany;

				try{
					sellingAmount = Integer.parseInt(args[1]);
					companyId = Integer.parseInt(args[2]);
				}catch (NumberFormatException nfe){
					player.sendMessage(messages.getPluginPrefix() + messages.getWrongArguments());
					return;
				}

				currentCompany = new Company(companyId).load();

				if (currentCompany.exists()) {

					if (sellingAmount > 0) {

						Investor playerInvestorProfile = new Investor(player.getName());
						Investment currentInvestment = playerInvestorProfile.getInvestment(currentCompany.getId());

						if (currentInvestment != null) {

							int playerActions = currentInvestment.getStocksAmount();

							if (sellingAmount <= playerActions && playerActions != 0){

								double pricePerAction = currentCompany.getStocksPrice();

								playerInvestorProfile.addInvestment(new Investment(currentCompany.getId(), playerActions - sellingAmount));
								playerInvestorProfile.saveToYml();

								cmdDependencyInjector.getMain().getEconomy().depositPlayer(player, pricePerAction * sellingAmount);

								player.sendMessage(messages.getPluginPrefix() + MessageFormat.format(messages.getSoldActions().replace("'", "''"), sellingAmount, pricePerAction * sellingAmount));

							}else{
								player.sendMessage(messages.getPluginPrefix() + messages.getPlayerNoActions());
							}

						}else {
							player.sendMessage(messages.getPluginPrefix() + messages.getInsufficientActions());
						}

					}else {
						player.sendMessage(messages.getPluginPrefix() + messages.getWrongArguments());
					}

				}else {
					player.sendMessage(messages.getPluginPrefix() + messages.getInvalidCompany());
				}

			}else {
				player.sendMessage(messages.getPluginPrefix() + messages.getMissingArguments());
			}

		}else {
			player.sendMessage(messages.getPluginPrefix() + messages.getNoPermission());
		}

	}

}
