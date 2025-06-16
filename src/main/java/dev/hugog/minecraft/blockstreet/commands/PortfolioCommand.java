package dev.hugog.minecraft.blockstreet.commands;

import dev.hugog.minecraft.blockstreet.data.dao.CompanyDao;
import dev.hugog.minecraft.blockstreet.data.services.CompaniesService;
import dev.hugog.minecraft.blockstreet.data.services.PlayersService;
import dev.hugog.minecraft.blockstreet.utils.Messages;
import dev.hugog.minecraft.dev_command.annotations.AutoValidation;
import dev.hugog.minecraft.dev_command.annotations.Command;
import dev.hugog.minecraft.dev_command.annotations.Dependencies;
import dev.hugog.minecraft.dev_command.commands.BukkitDevCommand;
import dev.hugog.minecraft.dev_command.commands.data.BukkitCommandData;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Portfolio Command
 *
 * <p>Command that allows players to check their investments and shares.
 * <p>Syntax: /invest portfolio
 *
 * @author Hugo1307
 * @since v1.0.0
 */
@AutoValidation
@Command(alias = "portfolio", description = "Check your investments and shares.", permission = "blockstreet.command.portfolio", isPlayerOnly = true)
@Dependencies(dependencies = {Messages.class, PlayersService.class, CompaniesService.class})
public class PortfolioCommand extends BukkitDevCommand {

    public PortfolioCommand(BukkitCommandData commandData, CommandSender commandSender, String[] args) {
        super(commandData, commandSender, args);
    }

    @Override
    public void execute() {

        Messages messages = getDependency(Messages.class);
        PlayersService playersService = getDependency(PlayersService.class);
        CompaniesService companiesService = getDependency(CompaniesService.class);
        Player player = (Player) getCommandSender();

        if (!playersService.hasAnyInvestments(player.getUniqueId())) {
            player.sendMessage(messages.getPluginPrefix() + messages.getPlayerAnyActions());
            return;
        }

        player.sendMessage(messages.getPluginHeader());
        playersService.getInvestments(player.getUniqueId())
                .forEach(investment -> {

                    CompanyDao investedCompany = companiesService.getCompanyById(investment.getCompanyId());

                    if (investedCompany == null) return;

                    TextComponent clickableCompanyDetails = new TextComponent(ChatColor.GREEN + "[Details]");
                    clickableCompanyDetails.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            new ComponentBuilder(ChatColor.GRAY + "Click to see company's details.").create()));
                    clickableCompanyDetails.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/invest company info " + investedCompany.getId()));

                    DecimalFormat df = new DecimalFormat("#.##");

                    player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + investedCompany.getName());
                    player.sendMessage("");
                    player.sendMessage(ChatColor.GRAY + messages.getShares() + ": " + ChatColor.GREEN + investment.getSharesAmount());
                    player.sendMessage(ChatColor.GRAY + messages.getTotalSharesValue() + ": " + ChatColor.GREEN + df.format(companiesService.getCompanyInvestmentValue(investment.getCompanyId(), investment.getSharesAmount())));
                    player.spigot().sendMessage(clickableCompanyDetails);
                    player.sendMessage("");

                });
        player.sendMessage(messages.getPluginFooter());

    }

    @Override
    public List<String> onTabComplete(String[] strings) {
        return List.of();
    }

}
