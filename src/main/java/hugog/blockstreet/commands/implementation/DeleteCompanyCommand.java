package hugog.blockstreet.commands.implementation;

import hugog.blockstreet.Main;
import hugog.blockstreet.commands.PluginCommand;
import hugog.blockstreet.others.Company;
import hugog.blockstreet.others.ConfigAccessor;
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

	public DeleteCompanyCommand(CommandSender sender, String[] args) {
		super(sender, args);
	}

	@Override
	public void execute() {

		Player p = (Player) sender;
		Messages messages = new Messages();

		if (p.hasPermission("blockstreet.admin.delete") || p.hasPermission("blockstreet.admin.*")) {

			if (args.length > 1) {

				int companyId;

				try {
					companyId = Integer.parseInt(args[1]);
				} catch (NumberFormatException e) {
					p.sendMessage(messages.getPluginPrefix() + messages.getWrongArguments());
					return;
				}

				Company companyToDelete = new Company(companyId).load();

				if (companyToDelete.exists()) {

					companyToDelete.delete(true);

					p.sendMessage(messages.getPluginPrefix() + MessageFormat.format(messages.getDeletedCompany(),
							ChatColor.GOLD + companyToDelete.getName() + ChatColor.GRAY));

				}else {
					p.sendMessage(messages.getPluginPrefix() + messages.getInvalidCompany());
				}

			}else {
				p.sendMessage(messages.getPluginPrefix() + messages.getMissingArguments());
			}

		}else {
			p.sendMessage(messages.getPluginPrefix() + messages.getNoPermission());
		}

	}

}
