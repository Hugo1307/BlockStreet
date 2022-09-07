package hugog.blockstreet.commands.implementation;

import hugog.blockstreet.commands.CmdDependencyInjector;
import hugog.blockstreet.commands.PluginCommand;
import hugog.blockstreet.others.Company;
import hugog.blockstreet.others.Messages;
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
public class CreateCompanyCommand extends PluginCommand {

	public CreateCompanyCommand(CommandSender sender, String[] args, CmdDependencyInjector cmdDependencyInjector) {
		super(sender, args, cmdDependencyInjector);
	}

	@Override
	public void execute() {

		Player player = (Player) sender;
		Messages messages = new Messages();

		if (player.isOp() || player.hasPermission("blockstreet.admin.createcompany") || player.hasPermission("blockstreet.admin.*")) {

			if (args.length >= 4) {

				String companyName = args[1];
				int stocksPrice;
				int companyRisk;
				Company newCompany;

				try {
					stocksPrice = Integer.parseInt(args[2]);
					companyRisk = Integer.parseInt(args[3]);
					newCompany = new Company(companyName, companyRisk, stocksPrice);
				}catch (Exception e) {
					player.sendMessage(messages.getPluginPrefix() + messages.getWrongArguments());
					return;
				}

				newCompany.save();
				player.sendMessage(messages.getPluginPrefix() + MessageFormat.format(messages.getCreatedCompany(), newCompany.getName()));

			}else {
				player.sendMessage(messages.getPluginPrefix() + messages.getMissingArguments());
			}

		}else {
			player.sendMessage(messages.getPluginPrefix() + messages.getNoPermission());
		}

	}

}
