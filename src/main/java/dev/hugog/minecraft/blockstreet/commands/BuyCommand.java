package dev.hugog.minecraft.blockstreet.commands;

import dev.hugog.minecraft.blockstreet.data.entities.CompanyEntity;
import dev.hugog.minecraft.blockstreet.data.repositories.implementations.CompaniesRepository;
import dev.hugog.minecraft.blockstreet.others.Messages;
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
import java.util.Optional;

/**
 * Buy Command
 * <p>
 * Command which allow players to buy in-game stocks.
 * <p>
 * Syntax: /invest buy [amount] [companyID]
 *
 * @author Hugo1307
 * @since v1.0.0
 */
@Command(alias = "buy", permission = "blockstreet.command.buy", isPlayerOnly = true)
@Dependencies(dependencies = {Messages.class, CompaniesRepository.class, Economy.class})
@ArgsValidation(argsTypes = {IntegerArgument.class, IntegerArgument.class}, mandatory = 2)
public class BuyCommand extends BukkitDevCommand {

	public BuyCommand(BukkitCommandData command, CommandSender commandSender, String[] args) {
		super(command, commandSender, args);
	}

	@Override
	public void execute() {

		Messages messages = (Messages) getDependency(Messages.class);
		CompaniesRepository companiesRepository = (CompaniesRepository) getDependency(CompaniesRepository.class);
		Economy vaultEconomy = (Economy) getDependency(Economy.class);

		if (!canSenderExecuteCommand()) {
			getCommandSender().sendMessage(messages.getPluginPrefix() + messages.getPlayerOnlyCommand());
			return;
		}

		Player player = (Player) getCommandSender();

		if (!hasPermissionToExecuteCommand()) {
			getCommandSender().sendMessage(messages.getPluginPrefix() + messages.getNoPermission());
			return;
		}

		if (!hasValidArgs()) {
			player.sendMessage(messages.getPluginPrefix() + messages.getWrongArguments());
			return;
		}

		// Arguments will always be valid, since we are validating them with DevCommands.
		int stocksAmount = Integer.parseInt(getArgs()[0]);
		int companyId = Integer.parseInt(getArgs()[1]);

		Optional<CompanyEntity> companyToInvestOptional = companiesRepository.getCompanyById(companyId);

		if (!companyToInvestOptional.isPresent()) {
			player.sendMessage(messages.getPluginPrefix() + messages.getInvalidCompany());
			return;
		}

		CompanyEntity companyToInvest = companyToInvestOptional.get();

		// If the company has unlimited stocks it will always be available.
		// Companies with unlimited stocks are marked with "-1" as available stocks.
		if (companyToInvest.getAvailableStocks() < stocksAmount  || companyToInvest.getAvailableStocks() == -1) {
			player.sendMessage(messages.getPluginPrefix() + messages.getInsufficientActions());
			return;
		}

		double playerMoney = vaultEconomy.getBalance(player);

		if (playerMoney < companyToInvest.getPricePerStock() * stocksAmount) {
			player.sendMessage(messages.getPluginPrefix() + messages.getInsufficientMoney());
			return;
		}

		vaultEconomy.withdrawPlayer(player, companyToInvest.getPricePerStock() * stocksAmount);
		companyToInvest.setAvailableStocks(companyToInvest.getAvailableStocks() - stocksAmount);

		// TODO: Add the stocks to the investor portfolio.

		companiesRepository.updateCompany(companyToInvest);

		player.sendMessage(messages.getPluginPrefix() + MessageFormat.format(
				messages.getBoughtActions().replace("'", "''"),
				stocksAmount
		));

	}

}
