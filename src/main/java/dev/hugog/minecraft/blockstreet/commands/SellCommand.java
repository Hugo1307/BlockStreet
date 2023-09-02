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
 * Sell Command
 * <p>
 * Command which allow players to sell stocks.
 * <p>
 * Syntax: /invest sell [companyID] [amount]
 *
 * @author Hugo1307
 * @since v1.0.0
 */
@Command(alias = "sell", description = "Sell company shares", permission = "blockstreet.command.sell", isPlayerOnly = true)
@ArgsValidation(mandatoryArgs = {IntegerArgument.class, PositiveIntegerArgument.class})
@Dependencies(dependencies = {Messages.class, PlayersService.class, CompaniesService.class, Economy.class})
public class SellCommand extends BukkitDevCommand {

	public SellCommand(BukkitCommandData commandData, CommandSender sender, String[] args) {
		super(commandData, sender, args);
	}

	@Override
	public void execute() {

		Messages messages = (Messages) getDependency(Messages.class);
		CompaniesService companiesService = (CompaniesService) getDependency(CompaniesService.class);
		PlayersService playersService = (PlayersService) getDependency(PlayersService.class);
		Economy economy = (Economy) getDependency(Economy.class);

		if (!validateCommand()) {
			return;
		}

		Player player = (Player) getCommandSender();

		long companyId = Long.parseLong(getArgs()[0]);
		int sellingAmount = Integer.parseInt(getArgs()[1]);

		if (!companiesService.companyExists(companyId)) {
			player.sendMessage(messages.getPluginPrefix() + messages.getInvalidCompany());
			return;
		}

		// If the player doesn't have at least "sellingAmount" shares in the company, then we don't allow him to sell.
		if (!playersService.hasSharesInCompany(player.getUniqueId(), companyId, sellingAmount)) {
			player.sendMessage(messages.getPluginPrefix() + messages.getPlayerNoActions());
			return;
		}

		double sharesValue = companiesService.getCompanyInvestmentValue(companyId, sellingAmount);

		// We remove the shares from the player and give him the money.
		economy.depositPlayer(player, sharesValue);
		playersService.removeSharesFromPlayer(player.getUniqueId(), companyId, sellingAmount);
		companiesService.addSharesToCompany(companyId, sellingAmount);

		player.sendMessage(messages.getPluginPrefix() + MessageFormat.format(messages.getSoldActions(), String.valueOf(sellingAmount), String.valueOf(sharesValue)));

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