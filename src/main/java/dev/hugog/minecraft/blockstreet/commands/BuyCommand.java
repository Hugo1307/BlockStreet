package dev.hugog.minecraft.blockstreet.commands;

import dev.hugog.minecraft.blockstreet.commands.validators.PositiveIntegerArgument;
import dev.hugog.minecraft.blockstreet.data.services.CompaniesService;
import dev.hugog.minecraft.blockstreet.data.services.PlayersService;
import dev.hugog.minecraft.blockstreet.utils.Messages;
import dev.hugog.minecraft.dev_command.annotations.ArgsValidation;
import dev.hugog.minecraft.dev_command.annotations.Command;
import dev.hugog.minecraft.dev_command.annotations.Dependencies;
import dev.hugog.minecraft.dev_command.commands.BukkitDevCommand;
import dev.hugog.minecraft.dev_command.commands.data.BukkitCommandData;
import dev.hugog.minecraft.dev_command.validators.IntegerArgument;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

/**
 * Buy Command
 * <p>
 * Command which allow players to buy in-game stocks.
 * <p>
 * Syntax: /invest buy [companyID] [amount]
 *
 * @author Hugo1307
 * @since v1.0.0
 */
@Command(alias = "buy", description = "Buy company shares.", permission = "blockstreet.command.buy", isPlayerOnly = true)
@Dependencies(dependencies = {Messages.class, Economy.class, CompaniesService.class, PlayersService.class})
@ArgsValidation(mandatoryArgs = {IntegerArgument.class, PositiveIntegerArgument.class})
public class BuyCommand extends BukkitDevCommand {

	public BuyCommand(BukkitCommandData command, CommandSender commandSender, String[] args) {
		super(command, commandSender, args);
	}

	@Override
	public void execute() {

		Messages messages = (Messages) getDependency(Messages.class);
		Economy vaultEconomy = (Economy) getDependency(Economy.class);

		CompaniesService companiesService = (CompaniesService) getDependency(CompaniesService.class);
		PlayersService playersService = (PlayersService) getDependency(PlayersService.class);

		if (!validateCommand()) {
			return;
		}

		Player player = (Player) getCommandSender();

		// Arguments will always be valid, since we are validating them with DevCommands.
		long companyId = Long.parseLong(getArgs()[0]);
		int numberOfSharesToBuy = Integer.parseInt(getArgs()[1]);

		if (!companiesService.companyExists(companyId)) {
			player.sendMessage(messages.getPluginPrefix() + messages.getInvalidCompany());
			return;
		}

		if (!companiesService.hasEnoughShares(companyId, numberOfSharesToBuy)) {
			player.sendMessage(messages.getPluginPrefix() + messages.getInsufficientActions());
			return;
		}

		double playerMoney = vaultEconomy.getBalance(player);
		double investmentPrice = companiesService.getCompanyInvestmentValue(companyId, numberOfSharesToBuy);

		if (playerMoney < investmentPrice) {
			player.sendMessage(messages.getPluginPrefix() + messages.getInsufficientMoney());
			return;
		}

		// Remove money
		vaultEconomy.withdrawPlayer(player, investmentPrice);

		companiesService.removeSharesFromCompany(companyId, numberOfSharesToBuy);
		playersService.addSharesToPlayer(player.getUniqueId(), companyId, numberOfSharesToBuy);

		player.sendMessage(messages.getPluginPrefix() + MessageFormat.format(
				messages.getBoughtActions().replace("'", "''"),
				numberOfSharesToBuy
		));

	}

	private boolean validateCommand() {

		Messages messages = (Messages) getDependency(Messages.class);

		if (!canSenderExecuteCommand()) {
			getCommandSender().sendMessage(messages.getPluginPrefix() + messages.getPlayerOnlyCommand());
			return false;
		}

		if (!hasPermissionToExecuteCommand()) {
			getCommandSender().sendMessage(messages.getPluginPrefix() + messages.getNoPermission());
			return false;
		}

		if (!hasValidArgs()) {
			getCommandSender().sendMessage(messages.getPluginPrefix() + messages.getWrongArguments());
			return false;
		}

		return true;

	}

}
