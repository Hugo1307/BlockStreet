package hugog.blockstreet.commands;

import org.bukkit.command.CommandSender;

public abstract class PluginCommand implements IPluginCommand {

    protected final CommandSender sender;
    protected final String[] args;

    public PluginCommand(CommandSender sender, String[] args) {
        this.sender = sender;
        this.args = args;
    }

    @Override
    public abstract void execute();

}
