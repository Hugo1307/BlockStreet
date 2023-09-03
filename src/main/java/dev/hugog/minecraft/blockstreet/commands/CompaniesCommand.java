package dev.hugog.minecraft.blockstreet.commands;

import dev.hugog.minecraft.blockstreet.data.dao.CompanyDao;
import dev.hugog.minecraft.blockstreet.data.services.CompaniesService;
import dev.hugog.minecraft.blockstreet.utils.Messages;
import dev.hugog.minecraft.dev_command.annotations.ArgsValidation;
import dev.hugog.minecraft.dev_command.annotations.Command;
import dev.hugog.minecraft.dev_command.annotations.Dependencies;
import dev.hugog.minecraft.dev_command.commands.BukkitDevCommand;
import dev.hugog.minecraft.dev_command.commands.data.BukkitCommandData;
import dev.hugog.minecraft.dev_command.validators.IntegerArgument;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Companies command
 * <p>
 * Command to check the available companies with stocks to sell.
 * <p>
 * Syntax: /invest companies
 *
 * @author Hugo1307
 * @since v1.0.0
 */
@Command(alias = "companies", description = "Check the companies with available shares to sell.",
		permission = "blockstreet.command.companies", isPlayerOnly = true)
@Dependencies(dependencies = { Messages.class, CompaniesService.class })
@ArgsValidation(optionalArgs = {IntegerArgument.class})
public class CompaniesCommand extends BukkitDevCommand {

	public CompaniesCommand(BukkitCommandData command, CommandSender commandSender, String[] args) {
		super(command, commandSender, args);
	}

	@Override
	public void execute() {

		Messages messages = (Messages) getDependency(Messages.class);
		CompaniesService companiesService = (CompaniesService) getDependency(CompaniesService.class);

		if (!validateCommand()) {
			return;
		}

		Player player = (Player) getCommandSender();

		List<CompanyDao> companiesList = companiesService.getCompaniesByIdInterval(0, 3);

		player.sendMessage(messages.getPluginHeader());
		companiesList.forEach(company -> printCompanyDetails(player, messages, company));
		player.sendMessage(messages.getPluginFooter());

	}

	@SuppressWarnings("deprecation")
	private void printCompanyDetails(Player player, Messages messages, CompanyDao currentCompany) {

		TextComponent companyDetails = new TextComponent(ChatColor.GRAY + "[Details]");
		companyDetails.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
				new ComponentBuilder(ChatColor.GRAY + "Click to see company's details.").create()));
		companyDetails.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/invest company " + currentCompany.getId()));

		if(currentCompany.getName() != null) {
			player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + currentCompany.getName());
			player.sendMessage("");
			player.sendMessage(ChatColor.GRAY + messages.getPrice() + ": " + ChatColor.GREEN + currentCompany.getFormattedCurrentSharePrice());
			player.sendMessage(ChatColor.GRAY + messages.getRisk() + ": " + ChatColor.GREEN + currentCompany.getRisk());
			player.spigot().sendMessage(companyDetails);
			player.sendMessage("");
		}

	}

	private boolean validateCommand() {

		Messages messages = (Messages) getDependency(Messages.class);

		if (!canSenderExecuteCommand()) {
			getCommandSender().sendMessage(messages.getPluginPrefix() + messages.getPlayerOnlyCommand());
			return false;
		}

		if (!hasPermissionToExecuteCommand()) {
			getCommandSender().sendMessage(messages.getPluginPrefix() + messages.getNoPermission());
			return false;
		}

		if (!hasValidArgs()) {
			getCommandSender().sendMessage(messages.getPluginPrefix() + messages.getWrongArguments());
			return false;
		}

		return true;
	}

}
