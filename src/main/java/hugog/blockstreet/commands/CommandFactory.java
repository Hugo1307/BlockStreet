package hugog.blockstreet.commands;

import hugog.blockstreet.commands.implementation.MainCommand;
import hugog.blockstreet.commands.implementation.NullCommand;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Constructor;

public class CommandFactory implements ICommandFactory {

    private final CommandSender sender;
    private final String[] args;

    public CommandFactory(CommandSender sender, String[] args) {
        this.sender = sender;
        this.args = args;
    }

    public IPluginCommand produce(ICommandRegister commandRegister) {

        Class<? extends PluginCommand> commandHandlerClass = commandRegister.getCommandExecutor();

        Constructor<? extends PluginCommand> constructor;
        try {
            constructor = commandHandlerClass.getDeclaredConstructor(CommandSender.class, String[].class);
            constructor.setAccessible(true);
            return constructor.newInstance(sender, args);
        } catch (ReflectiveOperationException e) {
            return new NullCommand(sender, args);
        }

    }

    public IPluginCommand produceMain() {
        return new MainCommand(sender, args);
    }

    @Override
    public IPluginCommand produceNull() {
        return new NullCommand(sender, args);
    }

}
