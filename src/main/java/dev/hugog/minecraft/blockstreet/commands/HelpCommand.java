package dev.hugog.minecraft.blockstreet.commands;

import dev.hugog.minecraft.blockstreet.utils.Messages;
import dev.hugog.minecraft.dev_command.annotations.Command;
import dev.hugog.minecraft.dev_command.annotations.Dependencies;
import dev.hugog.minecraft.dev_command.commands.BukkitDevCommand;
import dev.hugog.minecraft.dev_command.commands.data.BukkitCommandData;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Help Command
 *
 * <p>Command to provide information about the available commands.
 * <p>Syntax: /invest help
 *
 * @author Hugo1307
 * @since v1.0.0
 */
@Command(alias = "help", description = "Provides commands information.", permission = "blockstreet.command.help")
@Dependencies(dependencies = {Messages.class})
public class HelpCommand extends BukkitDevCommand {

    public HelpCommand(BukkitCommandData commandData, CommandSender commandSender, String[] args) {
        super(commandData, commandSender, args);
    }

    @Override
    public void execute() {

        Messages messages = getDependency(Messages.class);

        if (!hasPermissionToExecuteCommand()) {
            getCommandSender().sendMessage(messages.getPluginPrefix() + messages.getNoPermission());
            return;
        }

        getCommandSender().sendMessage(messages.getPluginHeader());
        getCommandSender().sendMessage("");

        getCommandSender().sendMessage(ChatColor.GREEN + "  • " + messages.getBuyCommandUsage());
        getCommandSender().sendMessage(ChatColor.GRAY + "    ╰ " + messages.getBuyCommandDescription());
        getCommandSender().sendMessage("");

        getCommandSender().sendMessage(ChatColor.GREEN + "  • " + messages.getSellCommandUsage());
        getCommandSender().sendMessage(ChatColor.GRAY + "    ╰ " + messages.getSellCommandDescription());
        getCommandSender().sendMessage("");

        getCommandSender().sendMessage(ChatColor.GREEN + "  • " + messages.getCompanyCommandUsage());
        getCommandSender().sendMessage(ChatColor.GRAY + "    ╰ " + messages.getCompanyCommandDescription());
        getCommandSender().sendMessage("");

        getCommandSender().sendMessage(ChatColor.GREEN + "  • " + messages.getCompaniesCommandUsage());
        getCommandSender().sendMessage(ChatColor.GRAY + "    ╰ " + messages.getCompaniesCommandDescription());
        getCommandSender().sendMessage("");

        getCommandSender().sendMessage(ChatColor.GREEN + "  • " + messages.getPortfolioCommandUsage());
        getCommandSender().sendMessage(ChatColor.GRAY + "    ╰ " + messages.getPortfolioCommandDescription());
        getCommandSender().sendMessage("");

        getCommandSender().sendMessage(ChatColor.GREEN + "  • " + messages.getInfoCommandUsage());
        getCommandSender().sendMessage(ChatColor.GRAY + "    ╰ " + messages.getInfoCommandDescription());
        getCommandSender().sendMessage("");

        getCommandSender().sendMessage(ChatColor.GREEN + "  • " + messages.getReloadCommandUsage());
        getCommandSender().sendMessage(ChatColor.GRAY + "    ╰ " + messages.getReloadCommandDescription());
        getCommandSender().sendMessage("");

        getCommandSender().sendMessage(ChatColor.GREEN + "  • " + messages.getHelpCommandUsage());
        getCommandSender().sendMessage(ChatColor.GRAY + "    ╰ " + messages.getHelpCommandDescription());
        getCommandSender().sendMessage("");

        getCommandSender().sendMessage(ChatColor.GREEN + "  • " + messages.getCreateCompanyCommandUsage());
        getCommandSender().sendMessage(ChatColor.GRAY + "    ╰ " + messages.getCreateCompanyCommandDescription());
        getCommandSender().sendMessage("");

        getCommandSender().sendMessage(ChatColor.GREEN + "  • " + messages.getDeleteCompanyCommandUsage());
        getCommandSender().sendMessage(ChatColor.GRAY + "    ╰ " + messages.getDeleteCompanyCommandDescription());
        getCommandSender().sendMessage("");

        getCommandSender().sendMessage(messages.getPluginFooter());

    }

    @Override
    public List<String> onTabComplete(String[] strings) {
        return List.of();
    }

}
