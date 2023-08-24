package dev.hugog.minecraft.blockstreet.commands;

import dev.hugog.minecraft.blockstreet.others.*;
import dev.hugog.minecraft.blockstreet.Main;
import hugog.blockstreet.others.*;
import me.hgsoft.minecraft.devcommand.annotations.Command;
import me.hgsoft.minecraft.devcommand.commands.BukkitDevCommand;
import me.hgsoft.minecraft.devcommand.commands.data.BukkitCommandData;
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
@Command(alias = "buy", permission = "blockstreet.command.buy")
public class BuyCommand extends BukkitDevCommand {

	public BuyCommand(BukkitCommandData command, CommandSender commandSender, String[] args) {
		super(command, commandSender, args);
	}

	@Override
	public void execute() {

		Player p = (Player) getCommandSender();
		Messages messages = new Messages();
		ConfigAccessor companiesReg = new ConfigAccessor(Main.getInstance(), "companies.yml");

		if (p.hasPermission("blockstreet.command.buy") || p.hasPermission("blockstreet.command.*")) {

			if (getArgs().length > 1) {

				int amount, companyId;
				int numberOfCompanies = 0;

				if (companiesReg.getConfig().get("Companies") != null)
					numberOfCompanies = companiesReg.getConfig().getConfigurationSection("Companies").getKeys(false).size();

				try{
					amount = Integer.parseInt(getArgs()[1]);
					companyId = Integer.parseInt(getArgs()[2]);
				}catch (NumberFormatException nfe){
					p.sendMessage(messages.getPluginPrefix() + messages.getWrongArguments());
					return;
				}

				if (companyId <= numberOfCompanies){

					Company currentCompany = new Company(companyId).load();
					double playerMoney = Main.getInstance().economy.getBalance(p);

					if (amount > 0) {

						if (currentCompany.getAvailableStocks() > amount || currentCompany.getAvailableStocks() == -1) {

							if (playerMoney >= amount * currentCompany.getStocksPrice()) {

								Investor playerInvestorProfile = new Investor(p.getName());
								Investment currentInvestment;

								if (playerInvestorProfile.contains(currentCompany.getId())) {
									currentInvestment = playerInvestorProfile.getInvestment(currentCompany.getId());
									currentInvestment.setStocksAmount(currentInvestment.getStocksAmount() + amount);
								}else {
									currentInvestment = new Investment(currentCompany.getId(), amount);
								}

								Main.getInstance().economy.withdrawPlayer(p, amount * currentCompany.getStocksPrice());
								currentCompany.setAvailableStocks(currentCompany.getAvailableStocks() - amount);

								playerInvestorProfile.addInvestment(currentInvestment);
								playerInvestorProfile.saveToYml();

								p.sendMessage(messages.getPluginPrefix() + MessageFormat.format(messages.getBoughtActions().replace("'", "''"), amount));

							}else {
								p.sendMessage(messages.getPluginPrefix() + messages.getInsufficientMoney());
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
