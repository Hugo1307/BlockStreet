package dev.hugog.minecraft.blockstreet.commands;

import dev.hugog.minecraft.blockstreet.commands.validators.CompanyRiskArgumentParser;
import dev.hugog.minecraft.blockstreet.commands.validators.MaterialArgumentParser;
import dev.hugog.minecraft.blockstreet.commands.validators.PositiveIntegerArgumentParser;
import dev.hugog.minecraft.blockstreet.commands.validators.SharePriceArgumentParser;
import dev.hugog.minecraft.blockstreet.data.dao.CompanyDao;
import dev.hugog.minecraft.blockstreet.data.services.CompaniesService;
import dev.hugog.minecraft.blockstreet.data.services.PlayersService;
import dev.hugog.minecraft.blockstreet.utils.Messages;
import dev.hugog.minecraft.dev_command.annotations.*;
import dev.hugog.minecraft.dev_command.arguments.parsers.StringArgumentParser;
import dev.hugog.minecraft.dev_command.commands.BukkitDevCommand;
import dev.hugog.minecraft.dev_command.commands.data.BukkitCommandData;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Create Company Command
 *
 * <p>Command that allow players to create their own companies.
 * <p>Syntax: /invest company create [name] [risk] [shares_amount] [share_price] [icon]
 *
 * @author Hugo1307
 * @since v1.0.0
 */
@AutoValidation
@Command(alias = "company create", description = "createCommandDescription", permission = "blockstreet.command.create", isPlayerOnly = true)
@Dependencies(dependencies = {Messages.class, CompaniesService.class})
@Arguments({
        @Argument(name = "name", description = "The name of the company to create.", position = 0, parser = StringArgumentParser.class),
        @Argument(name = "risk", description = "The risk of the company to create.", position = 1, parser = CompanyRiskArgumentParser.class),
        @Argument(name = "shares", description = "The amount of shares to create for the company.", position = 2, parser = PositiveIntegerArgumentParser.class),
        @Argument(name = "price", description = "The price of each share.", position = 3, parser = SharePriceArgumentParser.class),
        @Argument(name = "icon", description = "The icon of the company (optional).", position = 4, parser = MaterialArgumentParser.class, optional = true)
})
public class CreateCommand extends BukkitDevCommand {

    public CreateCommand(BukkitCommandData commandData, CommandSender commandSender, String[] args) {
        super(commandData, commandSender, args);
    }

    @Override
    public void execute() {

        Messages messages = getDependency(Messages.class);
        CompaniesService companiesService = getDependency(CompaniesService.class);
        PlayersService playersService = getDependency(PlayersService.class);
        Economy vaultEconomy = getDependency(Economy.class);
        Player player = (Player) getCommandSender();

        String companyName = getArgs()[0];
        int companyRisk = Integer.parseInt(getArgs()[1]);
        int companySharesAmount = Integer.parseInt(getArgs()[2]);
        double companySharePrice = Double.parseDouble(getArgs()[3]);
        Material companyIcon = getOptionalArgumentParser(4)
                .flatMap(parser -> ((MaterialArgumentParser) parser).parse()).orElse(null);

        double companyCreationTax = companiesService.getCompanyCreationTax(companySharesAmount, companySharePrice, companyRisk);
        if (!vaultEconomy.has(player, companyCreationTax)) {
            getCommandSender().sendMessage(messages.getPluginPrefix() + MessageFormat.format(messages.getInsufficientMoney(), companyCreationTax));
            return;
        }

        // Withdraw the company creation tax from the player
        vaultEconomy.withdrawPlayer(player, companyCreationTax);

        // Create the company and move the shares to the player
        CompanyDao createdCompany = companiesService.createPlayerCompany(companyName, companyRisk, companySharesAmount, companySharePrice, companyIcon);
        companiesService.removeSharesFromCompany(createdCompany.getId(), companySharesAmount);
        playersService.addSharesToPlayer(player.getUniqueId(), createdCompany, companySharesAmount);

        getCommandSender().sendMessage(messages.getPluginPrefix() + MessageFormat.format(messages.getCreatedCompany(), companyName));

    }

    @Override
    public List<String> onTabComplete(String[] args) {
        if (args.length == 2) {
            return IntStream.range(1, 6).mapToObj(String::valueOf).collect(Collectors.toList());
        } else if (args.length == 5) {
            return Arrays.stream(Material.values())
                    .map(Material::toString)
                    .filter(name -> name.toLowerCase().startsWith(args[4].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return List.of();
    }
}
