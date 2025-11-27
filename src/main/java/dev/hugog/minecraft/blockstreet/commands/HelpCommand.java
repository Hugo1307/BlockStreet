package dev.hugog.minecraft.blockstreet.commands;

import dev.hugog.minecraft.blockstreet.BlockStreet;
import dev.hugog.minecraft.blockstreet.utils.Messages;
import dev.hugog.minecraft.dev_command.DevCommand;
import dev.hugog.minecraft.dev_command.annotations.AutoValidation;
import dev.hugog.minecraft.dev_command.annotations.Command;
import dev.hugog.minecraft.dev_command.annotations.Dependencies;
import dev.hugog.minecraft.dev_command.commands.BukkitDevCommand;
import dev.hugog.minecraft.dev_command.commands.data.AbstractCommandData;
import dev.hugog.minecraft.dev_command.commands.data.BukkitCommandData;
import dev.hugog.minecraft.dev_command.integration.Integration;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Help Command
 *
 * <p>Command to provide information about the available commands.
 * <p>Syntax: /invest help
 *
 * @author Hugo1307
 * @since v1.0.0
 */
@AutoValidation
@Command(alias = "help", description = "helpCommandDescription", permission = "blockstreet.command.help")
@Dependencies(dependencies = {BlockStreet.class, Messages.class})
public class HelpCommand extends BukkitDevCommand {

    private final BlockStreet plugin;
    private final Messages messages;

    public HelpCommand(BukkitCommandData commandData, CommandSender commandSender, String[] args) {
        super(commandData, commandSender, args);
        this.plugin = getDependency(BlockStreet.class);
        this.messages = getDependency(Messages.class);
    }

    @Override
    public void execute() {

        List<Class<? extends BukkitDevCommand>> commandsCustomOrder = List.of(MainCommand.class, BuyCommand.class, SellCommand.class,
                PortfolioCommand.class, CompaniesCommand.class, CompanyCommand.class, CreateCommand.class,
                DeleteCommand.class, InfoCommand.class, HelpCommand.class, AdminCreateCommand.class,
                AdminDeleteCommand.class, ReloadCommand.class);

        List<AbstractCommandData> allCommands = DevCommand.getOrCreateInstance().getCommandHandler().getRegisteredCommands(Integration.createFromPlugin(plugin)).stream()
                .map(BukkitCommandData.class::cast)
                .filter(commandData -> getCommandSender().hasPermission(commandData.getPermission()))
                .sorted(Comparator.comparingInt(cmd -> commandsCustomOrder.indexOf(cmd.getExecutor())))
                .collect(Collectors.toUnmodifiableList());

        getCommandSender().sendMessage(messages.getPluginHeader());
        getCommandSender().sendMessage("");

        allCommands.forEach(commandData -> {
            String commandText = String.format("/%s %s %s", "invest", commandData.getAlias(), getCommandArgumentsString((BukkitCommandData) commandData));
            TextComponent commandComponent = new TextComponent(String.format("  • /%s %s", "invest", commandData.getAlias()));
            commandComponent.addExtra(getCommandArgumentsComponent((BukkitCommandData) commandData));
            commandComponent.setColor(net.md_5.bungee.api.ChatColor.GREEN);
            commandComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, commandText));
            commandComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to suggest command")));
            getCommandSender().spigot().sendMessage(commandComponent);
            getCommandSender().sendMessage(ChatColor.GREEN + "      ╰ " + ChatColor.GRAY + messages.getMessageByKey(commandData.getDescription()));
            getCommandSender().sendMessage("");
        });

        getCommandSender().sendMessage(messages.getPluginFooter());

    }

    private String getCommandArgumentsString(BukkitCommandData commandData) {

        StringBuilder commandArgumentsBuilder = new StringBuilder();
        if (commandData.getArguments() == null) {
            return commandArgumentsBuilder.toString();
        }

        Arrays.stream(commandData.getArguments()).forEach(commandArgument -> {
            if (commandArgument.optional()) {
                commandArgumentsBuilder.append(String.format("[%s] ", commandArgument.name()));
            } else {
                commandArgumentsBuilder.append(String.format("<%s> ", commandArgument.name()));
            }
        });
        return commandArgumentsBuilder.toString();

    }

    private BaseComponent getCommandArgumentsComponent(BukkitCommandData commandData) {

        BaseComponent commandArgumentsBuilder = new TextComponent();
        if (commandData.getArguments() == null) {
            return commandArgumentsBuilder;
        }

        Arrays.stream(commandData.getArguments()).forEach(commandArgument -> {
            TextComponent argumentComponent;
            if (commandArgument.optional()) {
                argumentComponent = new TextComponent(" [" + commandArgument.name() + "]");
            } else {
                argumentComponent = new TextComponent(" <" + commandArgument.name() + ">");
            }
            argumentComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(commandArgument.description())));
            commandArgumentsBuilder.addExtra(argumentComponent);
        });
        return commandArgumentsBuilder;

    }

    @Override
    public List<String> onTabComplete(String[] strings) {
        return List.of();
    }

}
