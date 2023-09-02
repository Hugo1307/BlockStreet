package dev.hugog.minecraft.blockstreet.commands;

import dev.hugog.minecraft.blockstreet.data.dao.CompanyDao;
import dev.hugog.minecraft.blockstreet.data.services.CompaniesService;
import dev.hugog.minecraft.blockstreet.data.services.PlayersService;
import dev.hugog.minecraft.blockstreet.utils.Messages;
import dev.hugog.minecraft.dev_command.annotations.Command;
import dev.hugog.minecraft.dev_command.annotations.Dependencies;
import dev.hugog.minecraft.dev_command.commands.BukkitDevCommand;
import dev.hugog.minecraft.dev_command.commands.data.BukkitCommandData;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

@Command(alias = "shares", description = "Check your investments and shares.", permission = "blockstreet.command.shares")
@Dependencies(dependencies = {Messages.class, PlayersService.class, CompaniesService.class})
public class SharesCommand extends BukkitDevCommand {

    public SharesCommand(BukkitCommandData commandData, CommandSender commandSender, String[] args) {
        super(commandData, commandSender, args);
    }

    @Override
    public void execute() {

        Messages messages = (Messages) getDependency(Messages.class);
        PlayersService playersService = (PlayersService) getDependency(PlayersService.class);
        CompaniesService companiesService = (CompaniesService) getDependency(CompaniesService.class);

        if (!validateCommand()) return;

        Player player = (Player) getCommandSender();

        if (!playersService.hasAnyInvestments(player.getUniqueId())) {
            player.sendMessage(messages.getPluginPrefix() + messages.getPlayerAnyActions());
            return;
        }

        player.sendMessage(messages.getPluginHeader());
        playersService.getInvestments(player.getUniqueId())
                .forEach(investment ->  {

                    CompanyDao investedCompany = companiesService.getCompanyDaoById(investment.getCompanyId());

                    if (investedCompany == null) return;

                    DecimalFormat df = new DecimalFormat("#.###");

                    player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + investedCompany.getName());
                    player.sendMessage("");
                    player.sendMessage(ChatColor.GRAY + messages.getShares() + ": " + ChatColor.GREEN + investment.getSharesAmount());
                    player.sendMessage(ChatColor.GRAY + messages.getTotalSharesValue() + ": " + ChatColor.GREEN + df.format(companiesService.getCompanyInvestmentValue(investment.getCompanyId(), investment.getSharesAmount())));
                    player.sendMessage("");

                });
        player.sendMessage(messages.getPluginFooter());

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

        return true;

    }

}
