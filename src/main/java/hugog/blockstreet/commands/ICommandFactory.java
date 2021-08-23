package hugog.blockstreet.commands;

public interface ICommandFactory {
    IPluginCommand produce(ICommandRegister commandRegister);
    IPluginCommand produceMain();
    IPluginCommand produceNull();
}
