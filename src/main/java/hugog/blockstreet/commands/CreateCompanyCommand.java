package hugog.blockstreet.commands;

import hugog.blockstreet.others.Company;
import hugog.blockstreet.others.Messages;
import me.hgsoft.minecraft.devcommand.annotations.Command;
import me.hgsoft.minecraft.devcommand.commands.BukkitDevCommand;
import me.hgsoft.minecraft.devcommand.commands.data.BukkitCommandData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

/**
 * Create Company Command
 *
 * Command which allow players to create companies while in game.
 *
 * Syntax: /invest create [CompanyName] [StocksPrice] [Risk]
 *
 * @author Hugo1307
 * @since v1.0.0
 */
@Command(alias = "create", permission = "blockstreet.admin.create_company")
public class CreateCompanyCommand extends BukkitDevCommand {

	public CreateCompanyCommand(BukkitCommandData command, CommandSender commandSender, String[] args) {
		super(command, commandSender, args);
	}

	@Override
	public void execute() {

		Player p = (Player) getCommandSender();
		Messages messages = new Messages();

		if (p.hasPermission("blockstreet.admin.createcompany") || p.hasPermission("blockstreet.admin.*")) {

			if (getArgs().length >= 4) {

				String companyName = getArgs()[1];
				int stocksPrice;
				int companyRisk;
				Company newCompany;

				try {
					stocksPrice = Integer.parseInt(getArgs()[2]);
					companyRisk = Integer.parseInt(getArgs()[3]);
					newCompany = new Company(companyName, companyRisk, stocksPrice);
				}catch (Exception e) {
					p.sendMessage(messages.getPluginPrefix() + messages.getWrongArguments());
					return;
				}

				newCompany.save();
				p.sendMessage(messages.getPluginPrefix() + MessageFormat.format(messages.getCreatedCompany(), newCompany.getName()));

			}else {
				p.sendMessage(messages.getPluginPrefix() + messages.getMissingArguments());
			}

		}else {
			p.sendMessage(messages.getPluginPrefix() + messages.getNoPermission());
		}

	}

}
