package hugog.blockstreet.commands;

import hugog.blockstreet.others.Company;
import hugog.blockstreet.others.Messages;
import me.hgsoft.minecraft.devcommand.annotations.Command;
import me.hgsoft.minecraft.devcommand.commands.BukkitDevCommand;
import me.hgsoft.minecraft.devcommand.commands.data.BukkitCommandData;
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
@Command(alias = "delete", permission = "blockstreet.admin.company_delete")
public class DeleteCompanyCommand extends BukkitDevCommand {

	public DeleteCompanyCommand(BukkitCommandData commandData, CommandSender commandSender, String[] args) {
		super(commandData, commandSender, args);
	}

	@Override
	public void execute() {

		Player p = (Player) getCommandSender();
		Messages messages = new Messages();

		if (p.hasPermission("blockstreet.admin.delete") || p.hasPermission("blockstreet.admin.*")) {

			if (getArgs().length > 1) {

				int companyId;

				try {
					companyId = Integer.parseInt(getArgs()[1]);
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
