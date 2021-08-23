package hugog.blockstreet.commands.implementation;

import hugog.blockstreet.Main;
import hugog.blockstreet.commands.PluginCommand;
import hugog.blockstreet.enums.ConfigurationFiles;
import hugog.blockstreet.others.Company;
import hugog.blockstreet.others.ConfigAccessor;
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

	public CreateCompanyCommand(CommandSender sender, String[] args) {
		super(sender, args);
	}

	@Override
	public void execute() {

		Player p = (Player) sender;
		Messages messages = new Messages();

		if (p.hasPermission("blockstreet.admin.createcompany") || p.hasPermission("blockstreet.admin.*")) {

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
