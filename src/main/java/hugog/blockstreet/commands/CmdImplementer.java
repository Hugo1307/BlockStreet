package hugog.blockstreet.commands;

import com.google.inject.Inject;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdImplementer implements CommandExecutor {

	private final CmdDependencyInjector cmdDependencyInjector;

	@Inject
	public CmdImplementer(CmdDependencyInjector cmdDependencyInjector) {
		this.cmdDependencyInjector = cmdDependencyInjector;
	}

	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (!(sender instanceof Player)) {
        	sender.sendMessage("[BlockStreet] Player Command only.");
			return false;
		}

		ICommandFactory commandFactory = new CommandFactory(cmdDependencyInjector, sender, args);

        if (args.length == 0)
        	commandFactory.produceMain().execute();
        else {
        	for (ICommandRegister registeredCommand : CommandRegister.values())
        		if (registeredCommand.getAlias().equalsIgnoreCase(args[0])) {
					commandFactory.produce(registeredCommand).execute();
					return true;
				}
        	commandFactory.produceNull().execute();
		}

        return true;

    }

}
