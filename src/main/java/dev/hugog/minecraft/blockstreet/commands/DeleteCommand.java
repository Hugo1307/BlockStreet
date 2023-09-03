package dev.hugog.minecraft.blockstreet.commands;

import dev.hugog.minecraft.blockstreet.data.dao.CompanyDao;
import dev.hugog.minecraft.blockstreet.data.services.CompaniesService;
import dev.hugog.minecraft.blockstreet.utils.Messages;
import dev.hugog.minecraft.dev_command.annotations.ArgsValidation;
import dev.hugog.minecraft.dev_command.annotations.Command;
import dev.hugog.minecraft.dev_command.annotations.Dependencies;
import dev.hugog.minecraft.dev_command.commands.BukkitDevCommand;
import dev.hugog.minecraft.dev_command.commands.data.BukkitCommandData;
import dev.hugog.minecraft.dev_command.validators.IntegerArgument;
import org.bukkit.command.CommandSender;

import java.text.MessageFormat;

@Command(alias = "delete", description = "Delete a company.", permission = "blockstreet.command.delete")
@Dependencies(dependencies = {Messages.class, CompaniesService.class})
@ArgsValidation(mandatoryArgs = {IntegerArgument.class})
public class DeleteCommand extends BukkitDevCommand {

    public DeleteCommand(BukkitCommandData commandData, CommandSender commandSender, String[] args) {
        super(commandData, commandSender, args);
    }

    @Override
    public void execute() {

        Messages messages = (Messages) getDependency(Messages.class);
        CompaniesService companiesService = (CompaniesService) getDependency(CompaniesService.class);

        if (!validateCommand()) {
            return;
        }

        long companyId = Long.parseLong(getArgs()[0]);

        if (!companiesService.companyExists(companyId)) {
            getCommandSender().sendMessage(messages.getPluginPrefix() + messages.getInvalidCompany());
            return;
        }

        CompanyDao companyToDelete = companiesService.getCompanyDaoById(companyId);
        companiesService.deleteCompany(companyId);

        getCommandSender().sendMessage(messages.getPluginPrefix() + MessageFormat.format(messages.getDeletedCompany(), companyToDelete.getName()));

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
