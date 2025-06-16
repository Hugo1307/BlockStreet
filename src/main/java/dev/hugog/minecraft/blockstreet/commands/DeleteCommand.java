package dev.hugog.minecraft.blockstreet.commands;

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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Delete Company Command
 *
 * <p>Command that allows players to delete a company.
 * <p>Syntax: /invest delete [ID]
 *
 * @author Hugo1307
 * @since v1.0.0
 */
@AutoValidation
@Command(alias = "company delete", description = "Allow players to delete their company.", permission = "blockstreet.command.delete", isPlayerOnly = true)
@Dependencies(dependencies = {Messages.class, CompaniesService.class})
@Arguments({
        @Argument(name = "id", description = "The id of the company to delete.", position = 0, parser = IntegerArgumentParser.class)
})
public class DeleteCommand extends BukkitDevCommand {

    private final Messages messages;
    private final CompaniesService companiesService;
    private final PlayersService playersService;
    private final Economy vaultEconomy;

    public DeleteCommand(BukkitCommandData commandData, CommandSender commandSender, String[] args) {
        super(commandData, commandSender, args);
        this.messages = getDependency(Messages.class);
        this.companiesService = getDependency(CompaniesService.class);
        this.playersService = getDependency(PlayersService.class);
        this.vaultEconomy = getDependency(Economy.class);
    }

    @Override
    public void execute() {

        Player player = (Player) getCommandSender();
        long companyId = (int) getArgumentParser(0).parse().orElseThrow();

        if (!companiesService.companyExists(companyId)) {
            getCommandSender().sendMessage(messages.getPluginPrefix() + messages.getInvalidCompany());
            return;
        }

        CompanyDao companyToDelete = companiesService.getCompanyById(companyId);
        if (!playersService.hasSharesInCompany(player.getUniqueId(), companyToDelete.getId(), companyToDelete.getTotalShares())) {
            getCommandSender().sendMessage(messages.getPluginPrefix() + messages.getCannotDeleteNotOwnedCompany());
            return;
        }

        double companyValuation = companyToDelete.getTotalShares() * companyToDelete.getCurrentSharePrice();

        playersService.removeSharesFromPlayer(player.getUniqueId(), companyToDelete.getId(), companyToDelete.getTotalShares());
        vaultEconomy.depositPlayer(player, companyValuation);
        companiesService.deleteCompany(companyId);

        getCommandSender().sendMessage(messages.getPluginPrefix() + MessageFormat.format(messages.getSoldActions(), companyToDelete.getTotalShares(), companyValuation));
        getCommandSender().sendMessage(messages.getPluginPrefix() + MessageFormat.format(messages.getDeletedCompany(), companyToDelete.getName()));

    }

    @Override
    public List<String> onTabComplete(String[] args) {
        Player player = (Player) getCommandSender();
        if (args.length == 1) {
            return playersService.getInvestments(player.getUniqueId()).stream()
                    .filter(investment -> companiesService.companyExists(investment.getCompanyId()) && companiesService.getCompanyById(investment.getCompanyId()).getTotalShares() <= investment.getSharesAmount())
                    .map(investment -> String.valueOf(investment.getCompanyId()))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
