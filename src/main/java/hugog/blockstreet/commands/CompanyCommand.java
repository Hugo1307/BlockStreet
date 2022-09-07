package hugog.blockstreet.commands.implementation;

import hugog.blockstreet.Main;
import hugog.blockstreet.commands.CmdDependencyInjector;
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
 * Company Command
 *
 * Command which allow players to get details about a company.
 *
 * Syntax: /invest company [ID]
 *
 * @author Hugo1307
 * @since v1.0.0
 */
public class CompanyCommand extends PluginCommand {

	public CompanyCommand(CommandSender sender, String[] args, CmdDependencyInjector cmdDependencyInjector) {
		super(sender, args, cmdDependencyInjector);
	}

	@Override
	public void execute() {

		Player player = (Player) sender;
		Messages messages = new Messages();
		ConfigAccessor companiesReg = new ConfigAccessor(Main.getInstance(), ConfigurationFiles.COMPANIES.getFileName());

		if (player.isOp() || player.hasPermission("blockstreet.command.company") || player.hasPermission("blockstreet.command.*")) {

			if (args.length > 1) {

				int numberOfCompanies = 0;

				if (companiesReg.getConfig().get("Companies") != null)
					numberOfCompanies = companiesReg.getConfig().getConfigurationSection("Companies").getKeys(false).size();

				if (numberOfCompanies > 0) {

					for (int i = 1; i <= numberOfCompanies; i++) {

						if(args[1].equals(String.valueOf(i))){

							Company currentCompany = new Company(i).load();

							TextComponent buyStocks = new TextComponent(ChatColor.GRAY + "[Buy Stocks]");
							buyStocks.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
									new ComponentBuilder(ChatColor.GRAY + "Click to see company's details.").create()));
							buyStocks.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/invest buy 1 " + i));

							player.sendMessage(messages.getPluginHeader());
							player.sendMessage("");
							player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + currentCompany.getName());
							player.sendMessage("");
							player.sendMessage(ChatColor.GRAY + "Id: " + ChatColor.GREEN + currentCompany.getId());
							player.sendMessage(ChatColor.GRAY + messages.getPrice() + ": " + ChatColor.GREEN + currentCompany.getStocksPrice());
							player.sendMessage(ChatColor.GRAY + messages.getRisk() + ": " + ChatColor.GREEN + currentCompany.getRisk());

							if (currentCompany.getAvailableStocks() < 0)
								player.sendMessage(ChatColor.GRAY + messages.getAvailableActions() + ": " + ChatColor.GREEN + "Unlimited");
							else
								player.sendMessage(ChatColor.GRAY + messages.getAvailableActions() + ": " + ChatColor.GREEN + currentCompany.getAvailableStocks());

							player.sendMessage(ChatColor.GRAY + messages.getActionHistoric() + ": ");
							player.sendMessage("");

							for (String element : currentCompany.getCompanyHistoric()){
								if (element.contains("+")) player.sendMessage(ChatColor.GREEN + "  " + element);
								else if (element.contains("-")) player.sendMessage(ChatColor.RED + "  " + element);
							}

							player.sendMessage("");
							player.spigot().sendMessage(buyStocks);
							player.sendMessage(messages.getPluginFooter());

						}

					}

				}else {
					player.sendMessage(messages.getPluginPrefix() + messages.getInvalidCompany());
				}

			}else {
				player.sendMessage(messages.getPluginPrefix() + messages.getInvalidCmd());
			}

		}else {
			player.sendMessage(messages.getPluginPrefix() + messages.getNoPermission());
		}

	}

}
