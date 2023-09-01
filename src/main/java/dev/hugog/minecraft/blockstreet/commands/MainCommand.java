package dev.hugog.minecraft.blockstreet.commands;

import dev.hugog.minecraft.blockstreet.others.Messages;
import dev.hugog.minecraft.dev_command.annotations.Command;
import dev.hugog.minecraft.dev_command.annotations.Dependencies;
import dev.hugog.minecraft.dev_command.commands.BukkitDevCommand;
import dev.hugog.minecraft.dev_command.commands.data.BukkitCommandData;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

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

        TextComponent webPanelText = new TextComponent(ChatColor.BLUE + "[Web Interface]");
        webPanelText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder(ChatColor.GRAY + "Click here to open the Web Interface.").create()));
        webPanelText.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://blockstreet.com/"));

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
        getCommandSender().sendMessage(ChatColor.GRAY + "We recommend the usage of BlockStreet's Web Interface to manage your investments.");
        getCommandSender().sendMessage("");
        getCommandSender().spigot().sendMessage(webPanelText);
        getCommandSender().sendMessage("");
        getCommandSender().sendMessage(messages.getPluginFooter());


    }

}
