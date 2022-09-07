package hugog.blockstreet.commands;

import hugog.blockstreet.commands.implementation.MainCommand;
import hugog.blockstreet.commands.implementation.NullCommand;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Constructor;

public class CommandFactory implements ICommandFactory {

    private final CmdDependencyInjector cmdDependencyInjector;
    private final CommandSender sender;
    private final String[] args;

    public CommandFactory(CmdDependencyInjector cmdDependencyInjector, CommandSender sender, String[] args) {
        this.cmdDependencyInjector = cmdDependencyInjector;
        this.sender = sender;
        this.args = args;
    }

    public IPluginCommand produce(ICommandRegister commandRegister) {

        Class<? extends PluginCommand> commandHandlerClass = commandRegister.getCommandExecutor();

        Constructor<? extends PluginCommand> constructor;
        try {
            constructor = commandHandlerClass.getDeclaredConstructor(CommandSender.class, String[].class, CmdDependencyInjector.class);
            constructor.setAccessible(true);
            return constructor.newInstance(sender, args, cmdDependencyInjector);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            return new NullCommand(sender, args, cmdDependencyInjector);
        }

    }

    public IPluginCommand produceMain() {
        return new MainCommand(sender, args, cmdDependencyInjector);
    }

    @Override
    public IPluginCommand produceNull() {
        return new NullCommand(sender, args, cmdDependencyInjector);
    }

}
