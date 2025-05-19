package dev.hugog.minecraft.blockstreet.commands;

import dev.hugog.minecraft.blockstreet.data.dao.CompanyDao;
import dev.hugog.minecraft.blockstreet.data.services.CompaniesService;
import dev.hugog.minecraft.blockstreet.utils.Messages;
import dev.hugog.minecraft.dev_command.annotations.*;
import dev.hugog.minecraft.dev_command.arguments.parsers.IntegerArgumentParser;
import dev.hugog.minecraft.dev_command.commands.BukkitDevCommand;
import dev.hugog.minecraft.dev_command.commands.data.BukkitCommandData;
import org.bukkit.command.CommandSender;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Delete Company Command
 *
 * <p>Command that allow players to delete a company.
 * <p>Syntax: /invest delete [ID]
 *
 * @author Hugo1307
 * @since v1.0.0
 */
@AutoValidation
@Command(alias = "delete", description = "Delete a company.", permission = "blockstreet.command.delete")
@Dependencies(dependencies = {Messages.class, CompaniesService.class})
@Arguments({
        @Argument(name = "companyId", description = "The ID of the company to delete.", position = 0, parser = IntegerArgumentParser.class)
})
public class DeleteCommand extends BukkitDevCommand {

    private final CompaniesService companiesService;

    public DeleteCommand(BukkitCommandData commandData, CommandSender commandSender, String[] args) {
        super(commandData, commandSender, args);
        this.companiesService = getDependency(CompaniesService.class);
    }

    @Override
    public void execute() {

        Messages messages = getDependency(Messages.class);
        long companyId = Long.parseLong(getArgs()[0]);

        if (!companiesService.companyExists(companyId)) {
            getCommandSender().sendMessage(messages.getPluginPrefix() + messages.getInvalidCompany());
            return;
        }

        CompanyDao companyToDelete = companiesService.getCompanyDaoById(companyId);
        companiesService.deleteCompany(companyId);

        getCommandSender().sendMessage(messages.getPluginPrefix() + MessageFormat.format(messages.getDeletedCompany(), companyToDelete.getName()));

    }

    @Override
    public List<String> onTabComplete(String[] args) {
        if (args.length == 1) {
            return companiesService.getAllCompanies().stream()
                    .map(CompanyDao::getId)
                    .map(String::valueOf)
                    .collect(Collectors.toList());
        }
        return List.of();
    }
}
