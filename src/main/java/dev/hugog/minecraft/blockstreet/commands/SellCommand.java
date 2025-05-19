package dev.hugog.minecraft.blockstreet.commands;

import dev.hugog.minecraft.blockstreet.commands.validators.PositiveIntegerArgumentParser;
import dev.hugog.minecraft.blockstreet.data.dao.CompanyDao;
import dev.hugog.minecraft.blockstreet.data.dao.InvestmentDao;
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
 * Sell Command
 *
 * <p>Command which allow players to sell stocks.
 * <p>Syntax: /invest sell [companyID] [amount]
 *
 * @author Hugo1307
 * @since v1.0.0
 */
@AutoValidation
@Command(alias = "sell", description = "Sell company shares", permission = "blockstreet.command.sell", isPlayerOnly = true)
@Dependencies(dependencies = {Messages.class, PlayersService.class, CompaniesService.class, Economy.class})
@Arguments({
        @Argument(name = "companyID", description = "The ID of the company to sell shares from.", position = 0, parser = IntegerArgumentParser.class),
        @Argument(name = "amount", description = "The amount of shares to sell.", position = 1, parser = PositiveIntegerArgumentParser.class)
})
public class SellCommand extends BukkitDevCommand {

    public SellCommand(BukkitCommandData commandData, CommandSender sender, String[] args) {
        super(commandData, sender, args);
    }

    @Override
    public void execute() {

        Messages messages = getDependency(Messages.class);
        CompaniesService companiesService = getDependency(CompaniesService.class);
        PlayersService playersService = getDependency(PlayersService.class);
        Economy economy = getDependency(Economy.class);
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

        player.sendMessage(messages.getPluginPrefix() + MessageFormat.format(messages.getSoldActions(), String.valueOf(sellingAmount), String.format("%.2f", sharesValue)));

    }

    @Override
    public List<String> onTabComplete(String[] args) {
        if (args.length == 1) {
            CompaniesService companiesService = getDependency(CompaniesService.class);
            return companiesService.getAllCompanies().stream()
                    .map(CompanyDao::getId)
                    .map(String::valueOf)
                    .collect(Collectors.toList());
        } else if (args.length == 2) {
            PlayersService playersService = getDependency(PlayersService.class);
            return playersService.getInvestments(((Player) getCommandSender()).getUniqueId()).stream()
                    .filter(investment -> investment.getCompanyId() == Long.parseLong(args[0]))
                    .map(InvestmentDao::getSharesAmount)
                    .map(String::valueOf)
                    .collect(Collectors.toList());
        }
        return List.of();
    }

}