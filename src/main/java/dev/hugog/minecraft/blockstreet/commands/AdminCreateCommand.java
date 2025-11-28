package dev.hugog.minecraft.blockstreet.commands;

import dev.hugog.minecraft.blockstreet.commands.validators.CompanyRiskArgumentParser;
import dev.hugog.minecraft.blockstreet.commands.validators.MaterialArgumentParser;
import dev.hugog.minecraft.blockstreet.commands.validators.PositiveIntegerArgumentParser;
import dev.hugog.minecraft.blockstreet.commands.validators.SharePriceArgumentParser;
import dev.hugog.minecraft.blockstreet.data.services.CompaniesService;
import dev.hugog.minecraft.blockstreet.utils.Messages;
import dev.hugog.minecraft.dev_command.annotations.*;
import dev.hugog.minecraft.dev_command.arguments.parsers.StringArgumentParser;
import dev.hugog.minecraft.dev_command.commands.BukkitDevCommand;
import dev.hugog.minecraft.dev_command.commands.data.BukkitCommandData;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Admin Create Company Command
 *
 * <p>Command that allow server administrators to create a new public-domain company.
 * <p>Syntax: /invest admin create [name] [risk] [shares_amount] [share_price] [icon]
 *
 * @author Hugo1307
 * @since v1.0.0
 */
@AutoValidation
@Command(alias = "admin create", description = "adminCreateCommand.description", permission = "blockstreet.admin.command.create")
@Dependencies(dependencies = {Messages.class, CompaniesService.class})
@Arguments({
        @Argument(name = "name", description = "adminCreateCommand.nameArg", position = 0, parser = StringArgumentParser.class),
        @Argument(name = "risk", description = "adminCreateCommand.riskArg", position = 1, parser = CompanyRiskArgumentParser.class),
        @Argument(name = "shares", description = "adminCreateCommand.sharesArg", position = 2, parser = PositiveIntegerArgumentParser.class),
        @Argument(name = "price", description = "adminCreateCommand.priceArg", position = 3, parser = SharePriceArgumentParser.class),
        @Argument(name = "icon", description = "adminCreateCommand.iconArg", position = 4, parser = MaterialArgumentParser.class, optional = true)
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
        Material companyIcon = getOptionalArgumentParser(4)
                .flatMap(parser -> ((MaterialArgumentParser) parser).parse()).orElse(null);

        companiesService.createAdminCompany(companyName, companyRisk, companySharesAmount, companySharePrice, companyIcon);
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
