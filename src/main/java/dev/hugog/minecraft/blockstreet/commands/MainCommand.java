package dev.hugog.minecraft.blockstreet.commands;

import dev.hugog.minecraft.blockstreet.utils.Messages;
import dev.hugog.minecraft.dev_command.annotations.Command;
import dev.hugog.minecraft.dev_command.annotations.Dependencies;
import dev.hugog.minecraft.dev_command.commands.BukkitDevCommand;
import dev.hugog.minecraft.dev_command.commands.data.BukkitCommandData;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

@Command(alias = "", description = "Plugin's root command.", permission = "blockstreet.command.main")
@Dependencies(dependencies = {Messages.class})
public class MainCommand extends BukkitDevCommand {

    public MainCommand(BukkitCommandData commandData, CommandSender commandSender, String[] args) {
        super(commandData, commandSender, args);
    }

    @Override
    public void execute() {

        Messages messages = (Messages) getDependency(Messages.class);

        if (!hasPermissionToExecuteCommand()) {
            getCommandSender().sendMessage(messages.getPluginPrefix() + messages.getNoPermission());
            return;
        }

        getCommandSender().sendMessage(messages.getPluginHeader());
        getCommandSender().sendMessage("");
        getCommandSender().sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "BlockStreet");
        getCommandSender().sendMessage("");
        getCommandSender().sendMessage(ChatColor.GRAY + "BlockStreet is a plugin that allows players to invest in companies and earn in-game money from their investments.");
        getCommandSender().sendMessage("");
        getCommandSender().sendMessage(ChatColor.GRAY + "Please use " + ChatColor.GREEN + "/invest help" + ChatColor.GRAY + " to see the available commands.");
        getCommandSender().sendMessage("");
        getCommandSender().sendMessage(ChatColor.GRAY + "You can also use " + ChatColor.GREEN + "/invest info" + ChatColor.GRAY + " to see the plugin's information.");
        getCommandSender().sendMessage("");
        getCommandSender().sendMessage(messages.getPluginFooter());


    }

    @Override
    public List<String> onTabComplete(String[] strings) {
        return List.of();
    }

}
