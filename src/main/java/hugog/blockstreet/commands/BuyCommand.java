package hugog.blockstreet.commands.implementation;

import hugog.blockstreet.Main;
import hugog.blockstreet.commands.CmdDependencyInjector;
import hugog.blockstreet.commands.PluginCommand;
import hugog.blockstreet.others.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

/**
 * Buy Command
 *
 * Command which allow players to buy in-game stocks.
 *
 * Syntax: /invest buy [amount] [companyID]
 *
 * @author Hugo1307
 * @since v1.0.0
 */
public class BuyCommand extends PluginCommand {

	public BuyCommand(CommandSender sender, String[] args, CmdDependencyInjector cmdDependencyInjector) {
		super(sender, args, cmdDependencyInjector);
	}

	@Override
	public void execute() {

		Player player = (Player) sender;
		Messages messages = new Messages();
		ConfigAccessor companiesReg = new ConfigAccessor(Main.getInstance(), "companies.yml");

		if (player.isOp() || player.hasPermission("blockstreet.command.buy") || player.hasPermission("blockstreet.command.*")) {

			if (args.length > 2) {

				int amount, companyId;
				int numberOfCompanies = 0;

				if (companiesReg.getConfig().get("Companies") != null)
					numberOfCompanies = companiesReg.getConfig().getConfigurationSection("Companies").getKeys(false).size();

				try{
					amount = Integer.parseInt(args[1]);
					companyId = Integer.parseInt(args[2]);
				}catch (NumberFormatException nfe){
					player.sendMessage(messages.getPluginPrefix() + messages.getWrongArguments());
					return;
				}

				if (companyId <= numberOfCompanies){

					Company currentCompany = new Company(companyId).load();
					double playerMoney = 5000d;

					if (!cmdDependencyInjector.getMain().isInTestMode())
						cmdDependencyInjector.getMain().getEconomy().getBalance(player);

					if (amount > 0) {

						if (currentCompany.getAvailableStocks() > amount || currentCompany.getAvailableStocks() == -1) {

							if (playerMoney >= amount * currentCompany.getStocksPrice()) {

								Investor playerInvestorProfile = new Investor(player.getName());
								Investment currentInvestment;

								if (playerInvestorProfile.contains(currentCompany.getId())) {
									currentInvestment = playerInvestorProfile.getInvestment(currentCompany.getId());
									currentInvestment.setStocksAmount(currentInvestment.getStocksAmount() + amount);
								}else {
									currentInvestment = new Investment(currentCompany.getId(), amount);
								}

								if (!cmdDependencyInjector.getMain().isInTestMode())
									cmdDependencyInjector.getMain().getEconomy().withdrawPlayer(player, amount * currentCompany.getStocksPrice());

								currentCompany.setAvailableStocks(currentCompany.getAvailableStocks() - amount);

								playerInvestorProfile.addInvestment(currentInvestment);
								playerInvestorProfile.saveToYml();

								player.sendMessage(messages.getPluginPrefix() + MessageFormat.format(messages.getBoughtActions().replace("'", "''"), amount));

							}else {
								player.sendMessage(messages.getPluginPrefix() + messages.getInsufficientMoney());
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
