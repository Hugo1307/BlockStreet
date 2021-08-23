package hugog.blockstreet.commands.implementation;

import hugog.blockstreet.commands.PluginCommand;
import hugog.blockstreet.others.Messages;
import org.bukkit.command.CommandSender;

public class NullCommand extends PluginCommand {

    public NullCommand(CommandSender sender, String[] args) {
        super(sender, args);
    }

    @Override
    public void execute() {
        final Messages messages = new Messages();
        sender.sendMessage(messages.getPluginPrefix() + messages.getUnknownCommand());
    }

}
