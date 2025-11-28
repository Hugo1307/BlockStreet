package dev.hugog.minecraft.blockstreet.commands;

import dev.hugog.minecraft.blockstreet.data.dao.CompanyDao;
import dev.hugog.minecraft.blockstreet.data.services.CompaniesService;
import dev.hugog.minecraft.blockstreet.data.services.PlayersService;
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
 * Admin Delete Company Command
 *
 * <p>Command that allow players to delete a company.
 * <p>Syntax: /invest delete [ID]
 *
 * @author Hugo1307
 * @since v1.0.0
 */
@AutoValidation
@Command(alias = "admin delete", description = "adminDeleteCommand.description", permission = "blockstreet.admin.command.delete")
@Dependencies(dependencies = {Messages.class, CompaniesService.class})
@Arguments({
        @Argument(name = "companyId", description = "adminDeleteCommand.companyIdArg", position = 0, parser = IntegerArgumentParser.class)
})
public class AdminDeleteCommand extends BukkitDevCommand {

    private final CompaniesService companiesService;
    private final PlayersService playersService;

    public AdminDeleteCommand(BukkitCommandData commandData, CommandSender commandSender, String[] args) {
        super(commandData, commandSender, args);
        this.companiesService = getDependency(CompaniesService.class);
        this.playersService = getDependency(PlayersService.class);
    }

    @Override
    public void execute() {
        Messages messages = getDependency(Messages.class);
        long companyId = Long.parseLong(getArgs()[0]);

        if (!companiesService.companyExists(companyId)) {
            getCommandSender().sendMessage(messages.getPluginPrefix() + messages.getInvalidCompany());
            return;
        }

        CompanyDao companyToDelete = companiesService.getCompanyById(companyId);
        companiesService.deleteCompany(companyId);
        playersService.cleanUpInvestmentsForOnlinePlayers(companiesService.getAllCompanies());

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
