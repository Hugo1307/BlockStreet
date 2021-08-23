package hugog.blockstreet.commands;

public interface ICommandRegister {
    String getAlias();
    Class<? extends PluginCommand> getCommandExecutor();
}
