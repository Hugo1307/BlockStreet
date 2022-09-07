package hugog.blockstreet.commands.implementation;

import hugog.blockstreet.commands.CmdDependencyInjector;
import hugog.blockstreet.commands.PluginCommand;
import hugog.blockstreet.others.Company;
import hugog.blockstreet.others.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

/**
 * Delete Company Command
 *
 * Command to delete companies in game.
 *
 * Syntax: /invest delete [ID]
 *
 * @author Hugo1307
 * @version 1.0.0
 */
public class DeleteCompanyCommand extends PluginCommand {

	public DeleteCompanyCommand(CommandSender sender, String[] args, CmdDependencyInjector cmdDependencyInjector) {
		super(sender, args, cmdDependencyInjector);
	}

	@Override
	public void execute() {

		Player player = (Player) sender;
		Messages messages = new Messages();

		if (player.isOp() || player.hasPermission("blockstreet.admin.delete") || player.hasPermission("blockstreet.admin.*")) {

			if (args.length > 1) {

				int companyId;

				try {
					companyId = Integer.parseInt(args[1]);
				} catch (NumberFormatException e) {
					player.sendMessage(messages.getPluginPrefix() + messages.getWrongArguments());
					return;
				}

				Company companyToDelete = new Company(companyId).load();

				if (companyToDelete.exists()) {

					companyToDelete.delete(true);

					player.sendMessage(messages.getPluginPrefix() + MessageFormat.format(messages.getDeletedCompany(),
							ChatColor.GOLD + companyToDelete.getName() + ChatColor.GRAY));

				}else {
					player.sendMessage(messages.getPluginPrefix() + messages.getInvalidCompany());
				}

			}else {
				player.sendMessage(messages.getPluginPrefix() + messages.getMissingArguments());
			}

		}else {
			player.sendMessage(messages.getPluginPrefix() + messages.getNoPermission());
		}

	}

}
