package dev.hugog.minecraft.blockstreet.commands;

import dev.hugog.minecraft.blockstreet.BlockStreet;
import dev.hugog.minecraft.blockstreet.commands.validators.PositiveIntegerArgumentParser;
import dev.hugog.minecraft.blockstreet.data.dao.CompanyDao;
import dev.hugog.minecraft.blockstreet.data.services.CompaniesService;
import dev.hugog.minecraft.blockstreet.data.services.PlayersService;
import dev.hugog.minecraft.blockstreet.utils.Messages;
import dev.hugog.minecraft.dev_command.annotations.*;
import dev.hugog.minecraft.dev_command.arguments.parsers.IntegerArgumentParser;
import dev.hugog.minecraft.dev_command.commands.BukkitDevCommand;
import dev.hugog.minecraft.dev_command.commands.data.BukkitCommandData;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Buy Command
 *
 * <p>Command that allow players to buy in-game stocks.
 * <p>Syntax: /invest buy [companyID] [amount]
 *
 * @author Hugo1307
 * @since v1.0.0
 */
@AutoValidation
@Command(alias = "buy", description = "buyCommandDescription", permission = "blockstreet.command.buy", isPlayerOnly = true)
@Dependencies(dependencies = {Messages.class, Economy.class, CompaniesService.class, PlayersService.class, BlockStreet.class})
@Arguments({
        @Argument(name = "companyID", description = "The ID of the company to buy shares from.", position = 0, parser = IntegerArgumentParser.class),
        @Argument(name = "amount", description = "The amount of shares to buy.", position = 1, parser = PositiveIntegerArgumentParser.class)
})
public class BuyCommand extends BukkitDevCommand {

    public BuyCommand(BukkitCommandData command, CommandSender commandSender, String[] args) {
        super(command, commandSender, args);
    }

    @Override
    public void execute() {

        Messages messages = getDependency(Messages.class);
        Economy vaultEconomy = getDependency(Economy.class);
        CompaniesService companiesService = getDependency(CompaniesService.class);
        PlayersService playersService = getDependency(PlayersService.class);
        BlockStreet plugin = getDependency(BlockStreet.class);
        Player player = (Player) getCommandSender();

        // Arguments will always be valid, since we are validating them with DevCommands.
        long companyId = Long.parseLong(getArgs()[0]);
        int numberOfSharesToBuy = Integer.parseInt(getArgs()[1]);

        if (!companiesService.companyExists(companyId)) {
            player.sendMessage(messages.getPluginPrefix() + messages.getInvalidCompany());
            return;
        }

        CompanyDao company = companiesService.getCompanyById(companyId);
        if (company.isBankrupt()) {
            player.sendMessage(messages.getPluginPrefix() + messages.getCannotBuyBankruptCompany());
            return;
        }

        if (!companiesService.hasEnoughShares(companyId, numberOfSharesToBuy)) {
            player.sendMessage(messages.getPluginPrefix() + messages.getInsufficientActions());
            return;
        }

        int sharesLimit = plugin.getConfig().getInt("BlockStreet.Limits.MaxSharesPerPlayer");
        long playerSharesCount = playersService.getTotalPlayerSharesCount(player.getUniqueId());
        if (sharesLimit > 0 && (playerSharesCount + numberOfSharesToBuy) > sharesLimit) {
            player.sendMessage(messages.getPluginPrefix() + MessageFormat.format(messages.getCannotOwnMoreThanMaxShares(), sharesLimit));
            return;
        }

        double playerMoney = vaultEconomy.getBalance(player);
        double investmentPrice = companiesService.getCompanyInvestmentValue(companyId, numberOfSharesToBuy);
        double minimumBalance = plugin.getConfig().getDouble("BlockStreet.Limits.MinBalance");
        double totalCost = investmentPrice + playersService.getTotalPortfolioValue(player.getUniqueId(), companiesService) * minimumBalance;
        if (playerMoney < totalCost) {
            player.sendMessage(messages.getPluginPrefix() + MessageFormat.format(messages.getInsufficientMoney(), totalCost));
            return;
        }

        // Remove money
        vaultEconomy.withdrawPlayer(player, investmentPrice);

        companiesService.removeSharesFromCompany(companyId, numberOfSharesToBuy);
        playersService.addSharesToPlayer(player.getUniqueId(), company, numberOfSharesToBuy);

        player.sendMessage(messages.getPluginPrefix() + MessageFormat.format(
                messages.getBoughtActions().replace("'", "''"),
                numberOfSharesToBuy
        ));

    }

    @Override
    public List<String> onTabComplete(String[] args) {
        if (args.length == 1) {
            // Return a list of company IDs
            CompaniesService companiesService = getDependency(CompaniesService.class);
            return companiesService.getAllCompanies().stream()
                    .map(company -> String.valueOf(company.getId()))
                    .collect(Collectors.toList());
        } else if (args.length == 2) {
            return List.of("1", "10", "100");
        }
        return List.of();
    }

}
