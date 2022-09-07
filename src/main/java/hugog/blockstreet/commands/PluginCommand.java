package hugog.blockstreet.commands;

import org.bukkit.command.CommandSender;

public abstract class PluginCommand implements IPluginCommand {

    protected final CommandSender sender;
    protected final String[] args;
    protected final CmdDependencyInjector cmdDependencyInjector;

    public PluginCommand(CommandSender sender, String[] args, CmdDependencyInjector cmdDependencyInjector) {
        this.sender = sender;
        this.args = args;
        this.cmdDependencyInjector = cmdDependencyInjector;
    }

    @Override
    public abstract void execute();

}
