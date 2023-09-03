package dev.hugog.minecraft.blockstreet.commands;

import dev.hugog.minecraft.blockstreet.commands.validators.CompanyRiskArgument;
import dev.hugog.minecraft.blockstreet.data.services.CompaniesService;
import dev.hugog.minecraft.blockstreet.utils.Messages;
import dev.hugog.minecraft.dev_command.annotations.ArgsValidation;
import dev.hugog.minecraft.dev_command.annotations.Command;
import dev.hugog.minecraft.dev_command.annotations.Dependencies;
import dev.hugog.minecraft.dev_command.commands.BukkitDevCommand;
import dev.hugog.minecraft.dev_command.commands.data.BukkitCommandData;
import dev.hugog.minecraft.dev_command.validators.DoubleArgument;
import dev.hugog.minecraft.dev_command.validators.IntegerArgument;
import dev.hugog.minecraft.dev_command.validators.StringArgument;
import org.bukkit.command.CommandSender;

import java.text.MessageFormat;

/**
 * Create Company Command
 * <p>
 * Command which allow players to create a new company.
 * <p>
 * Syntax: /invest create [name] [risk] [shares_amount] [share_price]
 *
 * @author Hugo1307
 * @since v1.0.0
 */
@Command(alias = "create", description = "Create a new company.", permission = "blockstreet.command.create")
@Dependencies(dependencies = {Messages.class, CompaniesService.class})
@ArgsValidation(mandatoryArgs = {StringArgument.class, CompanyRiskArgument.class, IntegerArgument.class, DoubleArgument.class})
public class CreateCommand extends BukkitDevCommand {

    public CreateCommand(BukkitCommandData commandData, CommandSender commandSender, String[] args) {
        super(commandData, commandSender, args);
    }

    @Override
    public void execute() {

        Messages messages = (Messages) getDependency(Messages.class);
        CompaniesService companiesService = (CompaniesService) getDependency(CompaniesService.class);

        if (!validateCommand()) {
            return;
        }

        String companyName = getArgs()[0];
        int companyRisk = Integer.parseInt(getArgs()[1]);
        int companySharesAmount = Integer.parseInt(getArgs()[2]);
        double companySharePrice = Double.parseDouble(getArgs()[3]);

        companiesService.createCompany(companyName, companyRisk, companySharesAmount, companySharePrice);

        getCommandSender().sendMessage(messages.getPluginPrefix() + MessageFormat.format(messages.getCreatedCompany(), companyName));

    }

    private boolean validateCommand() {

        Messages messages = (Messages) getDependency(Messages.class);

        if (!hasPermissionToExecuteCommand()) {
            getCommandSender().sendMessage(messages.getPluginPrefix() + messages.getNoPermission());
            return false;
        }

        if (!hasValidArgs()) {
            getCommandSender().sendMessage(messages.getPluginPrefix() + messages.getWrongArguments());
            return false;
        }

        return true;

    }

}
