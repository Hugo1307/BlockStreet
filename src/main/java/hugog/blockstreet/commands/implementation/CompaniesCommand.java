package hugog.blockstreet.commands.implementation;

import hugog.blockstreet.Main;
import hugog.blockstreet.commands.PluginCommand;
import hugog.blockstreet.enums.ConfigurationFiles;
import hugog.blockstreet.others.Company;
import hugog.blockstreet.others.ConfigAccessor;
import hugog.blockstreet.others.Messages;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Companies command
 *
 * Command to check the available companies with stocks to sell.
 *
 * Syntax: /invest companies
 *
 * @author Hugo1307
 * @since v1.0.0
 */
public class CompaniesCommand extends PluginCommand {

	public CompaniesCommand(CommandSender sender, String[] args) {
		super(sender, args);
	}

	@Override
	public void execute() {

		Player p = (Player) sender;
		Messages messages = new Messages();
		ConfigAccessor companiesReg = new ConfigAccessor(Main.getInstance(), ConfigurationFiles.COMPANIES.getFileName());

		if (p.hasPermission("blockstreet.command.companies") || p.hasPermission("blockstreet.command.*")) {

			int numberOfCompanies = 0;

			if (companiesReg.getConfig().get("Companies") != null)
				numberOfCompanies = companiesReg.getConfig().getConfigurationSection("Companies").getKeys(false).size();

			if (numberOfCompanies > 0) {

				if (args.length <= 1) {

					p.sendMessage(messages.getPluginHeader());
					for (int companyIndex = 1; companyIndex <= 3 ; companyIndex++) {

						Company currentCompany = new Company(companyIndex).load();

						sendCompanyText(p, messages, currentCompany, companyIndex);

					}

					if(numberOfCompanies > 3)
						p.sendMessage(ChatColor.GREEN + "/invest companies 2" + ChatColor.GRAY + " - " + messages.getListNextPage());

					p.sendMessage(messages.getPluginFooter());

				}else {

					int pageNumber, firstCompanyOfPage, lastCompanyOfPage;

					try {
						pageNumber = Integer.parseInt(args[1]);
					} catch (NumberFormatException e) {
						p.sendMessage(messages.getPluginPrefix() + messages.getWrongArguments());
						return;
					}

					firstCompanyOfPage = (pageNumber-1)*3+1;
					lastCompanyOfPage = pageNumber*3;

					p.sendMessage(messages.getPluginHeader());
					for (int companyIndex = firstCompanyOfPage; companyIndex <= lastCompanyOfPage; companyIndex++) {

						Company currentCompany = new Company(companyIndex).load();

						sendCompanyText(p, messages, currentCompany, companyIndex);

					}

					if(numberOfCompanies > lastCompanyOfPage + 1)
						p.sendMessage(ChatColor.GREEN + "/invest companies " + pageNumber  + ChatColor.GRAY + " - " + messages.getListNextPage());

					p.sendMessage(messages.getPluginFooter());

				}

			}else {
				p.sendMessage(messages.getPluginPrefix() + messages.getNonExistentPage());
			}

		}else {
			p.sendMessage(messages.getPluginPrefix() + messages.getNoPermission());
		}

	}

	private void sendCompanyText(Player p,  Messages messages, Company currentCompany, int companyIndex) {

		TextComponent companyDetails = new TextComponent(ChatColor.GRAY + "[Details]");
		companyDetails.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
				new ComponentBuilder(ChatColor.GRAY + "Click to see company's details.").create()));
		companyDetails.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/invest company " + companyIndex));

		if(currentCompany.getName() != null) {
			p.sendMessage(ChatColor.GREEN + currentCompany.getName());
			p.sendMessage(ChatColor.GRAY + messages.getPrice() + ": " + currentCompany.getStocksPrice());
			p.sendMessage(ChatColor.GRAY + messages.getRisk() + ": " + ChatColor.GREEN + currentCompany.getRisk());
			p.spigot().sendMessage(companyDetails);
			p.sendMessage("");
		}

	}

}
