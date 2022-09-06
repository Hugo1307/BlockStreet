package hugog.blockstreet.commands;

import hugog.blockstreet.Main;
import hugog.blockstreet.enums.ConfigurationFiles;
import hugog.blockstreet.others.Company;
import hugog.blockstreet.others.ConfigAccessor;
import hugog.blockstreet.others.Messages;
import me.hgsoft.minecraft.devcommand.annotations.Command;
import me.hgsoft.minecraft.devcommand.commands.BukkitDevCommand;
import me.hgsoft.minecraft.devcommand.commands.data.BukkitCommandData;
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
@Command(alias = "company", permission = "blockstreet.command.company")
public class CompanyCommand extends BukkitDevCommand {

	public CompanyCommand(BukkitCommandData command, CommandSender commandSender, String[] args) {
		super(command, commandSender, args);
	}

	@Override
	public void execute() {

		Player p = (Player) getCommandSender();
		Messages messages = new Messages();
		ConfigAccessor companiesReg = new ConfigAccessor(Main.getInstance(), ConfigurationFiles.COMPANIES.getFileName());

		if (p.hasPermission("blockstreet.command.company") || p.hasPermission("blockstreet.command.*")) {

			if (getArgs().length > 1) {

				int numberOfCompanies = 0;

				if (companiesReg.getConfig().get("Companies") != null)
					numberOfCompanies = companiesReg.getConfig().getConfigurationSection("Companies").getKeys(false).size();

				if (numberOfCompanies > 0) {

					for (int i = 1; i <= numberOfCompanies; i++) {

						if(getArgs()[1].equals(String.valueOf(i))){

							Company currentCompany = new Company(i).load();

							TextComponent buyStocks = new TextComponent(ChatColor.GRAY + "[Buy Stocks]");
							buyStocks.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
									new ComponentBuilder(ChatColor.GRAY + "Click to see company's details.").create()));
							buyStocks.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/invest buy 1 " + i));

							p.sendMessage(messages.getPluginHeader());
							p.sendMessage("");
							p.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + currentCompany.getName());
							p.sendMessage("");
							p.sendMessage(ChatColor.GRAY + "Id: " + ChatColor.GREEN + currentCompany.getId());
							p.sendMessage(ChatColor.GRAY + messages.getPrice() + ": " + ChatColor.GREEN + currentCompany.getStocksPrice());
							p.sendMessage(ChatColor.GRAY + messages.getRisk() + ": " + ChatColor.GREEN + currentCompany.getRisk());

							if (currentCompany.getAvailableStocks() < 0)
								p.sendMessage(ChatColor.GRAY + messages.getAvailableActions() + ": " + ChatColor.GREEN + "Unlimited");
							else
								p.sendMessage(ChatColor.GRAY + messages.getAvailableActions() + ": " + ChatColor.GREEN + currentCompany.getAvailableStocks());

							p.sendMessage(ChatColor.GRAY + messages.getActionHistoric() + ": ");
							p.sendMessage("");

							for (String element : currentCompany.getCompanyHistoric()){
								if (element.contains("+")) p.sendMessage(ChatColor.GREEN + "  " + element);
								else if (element.contains("-")) p.sendMessage(ChatColor.RED + "  " + element);
							}

							p.sendMessage("");
							p.spigot().sendMessage(buyStocks);
							p.sendMessage(messages.getPluginFooter());

						}

					}

				}else {
					p.sendMessage(messages.getPluginPrefix() + messages.getInvalidCompany());
				}

			}else {
				p.sendMessage(messages.getPluginPrefix() + messages.getInvalidCmd());
			}

		}else {
			p.sendMessage(messages.getPluginPrefix() + messages.getNoPermission());
		}

	}

}
