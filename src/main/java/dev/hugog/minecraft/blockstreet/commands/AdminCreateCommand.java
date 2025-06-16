package dev.hugog.minecraft.blockstreet.commands;

import dev.hugog.minecraft.blockstreet.commands.validators.CompanyRiskArgumentParser;
import dev.hugog.minecraft.blockstreet.commands.validators.PositiveIntegerArgumentParser;
import dev.hugog.minecraft.blockstreet.commands.validators.SharePriceArgumentParser;
import dev.hugog.minecraft.blockstreet.data.services.CompaniesService;
import dev.hugog.minecraft.blockstreet.utils.Messages;
import dev.hugog.minecraft.dev_command.annotations.*;
import dev.hugog.minecraft.dev_command.arguments.parsers.StringArgumentParser;
import dev.hugog.minecraft.dev_command.commands.BukkitDevCommand;
import dev.hugog.minecraft.dev_command.commands.data.BukkitCommandData;
import org.bukkit.command.CommandSender;

import java.text.MessageFormat;
import java.util.List;

/**
 * Admin Create Company Command
 *
 * <p>Command that allow server administrators to create a new public-domain company.
 * <p>Syntax: /invest admin create [name] [risk] [shares_amount] [share_price]
 *
 * @author Hugo1307
 * @since v1.0.0
 */
@AutoValidation
@Command(alias = "admin create", description = "Allow admins to create new public company.", permission = "blockstreet.admin.command.create")
@Dependencies(dependencies = {Messages.class, CompaniesService.class})
@Arguments({
        @Argument(name = "name", description = "The name of the company to create.", position = 0, parser = StringArgumentParser.class),
        @Argument(name = "risk", description = "The risk of the company to create.", position = 1, parser = CompanyRiskArgumentParser.class),
        @Argument(name = "shares", description = "The amount of shares to create for the company.", position = 2, parser = PositiveIntegerArgumentParser.class),
        @Argument(name = "price", description = "The price of each share.", position = 3, parser = SharePriceArgumentParser.class)
})
public class AdminCreateCommand extends BukkitDevCommand {

    public AdminCreateCommand(BukkitCommandData commandData, CommandSender commandSender, String[] args) {
        super(commandData, commandSender, args);
    }

    @Override
    public void execute() {

        Messages messages = getDependency(Messages.class);
        CompaniesService companiesService = getDependency(CompaniesService.class);

        String companyName = getArgs()[0];
        int companyRisk = Integer.parseInt(getArgs()[1]);
        int companySharesAmount = Integer.parseInt(getArgs()[2]);
        double companySharePrice = Double.parseDouble(getArgs()[3]);

        companiesService.createAdminCompany(companyName, companyRisk, companySharesAmount, companySharePrice);
        getCommandSender().sendMessage(messages.getPluginPrefix() + MessageFormat.format(messages.getCreatedCompany(), companyName));

    }

    @Override
    public List<String> onTabComplete(String[] strings) {
        return List.of();
    }

}
