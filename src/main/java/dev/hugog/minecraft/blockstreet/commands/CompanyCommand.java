package dev.hugog.minecraft.blockstreet.commands;

import com.google.common.collect.Lists;
import dev.hugog.minecraft.blockstreet.data.dao.CompanyDao;
import dev.hugog.minecraft.blockstreet.data.dao.QuoteDao;
import dev.hugog.minecraft.blockstreet.data.services.CompaniesService;
import dev.hugog.minecraft.blockstreet.utils.FormattingUtils;
import dev.hugog.minecraft.blockstreet.utils.Messages;
import dev.hugog.minecraft.dev_command.annotations.*;
import dev.hugog.minecraft.dev_command.arguments.parsers.IntegerArgumentParser;
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
import java.util.stream.Collectors;

/**
 * Company Command
 * <p>
 * Command which allow players to get details about a company.
 * <p>
 * Syntax: /invest company [ID]
 *
 * @author Hugo1307
 * @since v1.0.0
 */
@AutoValidation
@Command(alias = "company info", description = "Get details about a company", permission = "blockstreet.command.company", isPlayerOnly = true)
@Arguments(
        @Argument(name = "companyId", description = "The ID of the company to get details from.", position = 0, parser = IntegerArgumentParser.class)
)
@Dependencies(dependencies = {Messages.class, CompaniesService.class})
public class CompanyCommand extends BukkitDevCommand {

    public CompanyCommand(BukkitCommandData command, CommandSender commandSender, String[] args) {
        super(command, commandSender, args);
    }

    @Override
    public void execute() {

        Messages messages = getDependency(Messages.class);
        CompaniesService companiesService = getDependency(CompaniesService.class);
        Player player = (Player) getCommandSender();

        long companyId = Long.parseLong(getArgs()[0]);
        CompanyDao companyDao = companiesService.getCompanyById(companyId);

        if (companyDao == null) {
            player.sendMessage(messages.getPluginPrefix() + messages.getInvalidCompany());
            return;
        }

        printCompanyDetails(companyDao, player, messages);

    }

    @Override
    public List<String> onTabComplete(String[] args) {
        if (args.length == 1) {
            CompaniesService companiesService = getDependency(CompaniesService.class);
            return companiesService.getAllCompanies().stream()
                    .map(CompanyDao::getId)
                    .map(String::valueOf)
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    private void printCompanyDetails(CompanyDao currentCompany, Player player, Messages messages) {


        TextComponent buyStocks = new TextComponent(ChatColor.GRAY + "[Buy Stocks]");
        buyStocks.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder(ChatColor.GRAY + "Click to see company's details.").create()));
        buyStocks.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/invest buy " + currentCompany.getId() + " 1"));

        player.sendMessage(messages.getPluginHeader());
        player.sendMessage("");
        player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + currentCompany.getName());
        player.sendMessage("");
        player.sendMessage(ChatColor.GRAY + "Id: " + ChatColor.GREEN + currentCompany.getId());
        player.sendMessage(ChatColor.GRAY + messages.getCompanyStatus() + ": " + (currentCompany.isBankrupt() ? messages.getCompanyStatusBankrupt() : messages.getCompanyStatusTrading()));
        player.sendMessage(ChatColor.GRAY + messages.getPrice() + ": " + ChatColor.GREEN + FormattingUtils.formatDouble(currentCompany.getCurrentSharePrice()));
        player.sendMessage(ChatColor.GRAY + messages.getRisk() + ": " + ChatColor.GREEN + currentCompany.getRisk());

        if (currentCompany.getAvailableShares() < 0)
            player.sendMessage(ChatColor.GRAY + messages.getAvailableActions() + ": " + ChatColor.GREEN + "Unlimited");
        else
            player.sendMessage(ChatColor.GRAY + messages.getAvailableActions() + ": " + ChatColor.GREEN + currentCompany.getAvailableShares());

        player.sendMessage(ChatColor.GRAY + messages.getActionHistoric() + ": ");
        player.sendMessage("");

        List<QuoteDao> quotesToPresent = Lists.reverse(currentCompany.getHistoric().toList()
                .subList(Math.max(0, currentCompany.getHistoric().size() - 5), currentCompany.getHistoric().size()));
        for (QuoteDao quote : quotesToPresent) {

            DecimalFormat df = new DecimalFormat("###.###%");

            if (quote.getVariation() > 0) {
                player.sendMessage(ChatColor.GREEN + "  +" + df.format(quote.getVariation()));
            } else {
                player.sendMessage(ChatColor.RED + "  " + df.format(quote.getVariation()));
            }

        }

        player.sendMessage("");
        player.spigot().sendMessage(buyStocks);
        player.sendMessage(messages.getPluginFooter());

    }

}