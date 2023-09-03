package dev.hugog.minecraft.blockstreet.commands;

import dev.hugog.minecraft.blockstreet.data.dao.CompanyDao;
import dev.hugog.minecraft.blockstreet.data.dao.QuoteDao;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Company Command
 * <p>
 * Command which allow players to get details about a company.
 * <p>
 * Syntax: /invest company [ID]
 *
 * @author Hugo1307
 * @since v1.0.0
 */
@Command(alias = "company", description = "Get details about a company", permission = "blockstreet.command.company", isPlayerOnly = true)
@ArgsValidation(mandatoryArgs = {IntegerArgument.class})
@Dependencies(dependencies = { Messages.class, CompaniesService.class })
public class CompanyCommand extends BukkitDevCommand {

	public CompanyCommand(BukkitCommandData command, CommandSender commandSender, String[] args) {
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

		long companyId = Long.parseLong(getArgs()[0]);
		CompanyDao companyDao = companiesService.getCompanyDaoById(companyId);

		if (companyDao == null) {
			player.sendMessage(messages.getPluginPrefix() + messages.getInvalidCompany());
			return;
		}

		printCompanyDetails(companyDao, player, messages);

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

	private void printCompanyDetails(CompanyDao currentCompany, Player player, Messages messages) {


		TextComponent buyStocks = new TextComponent(ChatColor.GRAY + "[Buy Stocks]");
		buyStocks.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
				new ComponentBuilder(ChatColor.GRAY + "Click to see company's details.").create()));
		buyStocks.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/invest buy " + currentCompany.getId() + " 1"));

		BigDecimal companySharePriceDecimal = BigDecimal.valueOf(currentCompany.getCurrentSharePrice());
		companySharePriceDecimal = companySharePriceDecimal.setScale(3, RoundingMode.HALF_UP);

		player.sendMessage(messages.getPluginHeader());
		player.sendMessage("");
		player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + currentCompany.getName());
		player.sendMessage("");
		player.sendMessage(ChatColor.GRAY + "Id: " + ChatColor.GREEN + currentCompany.getId());
		player.sendMessage(ChatColor.GRAY + messages.getPrice() + ": " + ChatColor.GREEN + companySharePriceDecimal.doubleValue());
		player.sendMessage(ChatColor.GRAY + messages.getRisk() + ": " + ChatColor.GREEN + currentCompany.getRisk());

		if (currentCompany.getAvailableShares() < 0)
			player.sendMessage(ChatColor.GRAY + messages.getAvailableActions() + ": " + ChatColor.GREEN + "Unlimited");
		else
			player.sendMessage(ChatColor.GRAY + messages.getAvailableActions() + ": " + ChatColor.GREEN + currentCompany.getAvailableShares());

		player.sendMessage(ChatColor.GRAY + messages.getActionHistoric() + ": ");
		player.sendMessage("");

		List<QuoteDao> quotesToPresent = currentCompany.getHistoric().toList().subList(0, Math.min(5, currentCompany.getHistoric().size()));
		for (QuoteDao quote : quotesToPresent){

			DecimalFormat df = new DecimalFormat("###.###%");

			if (quote.getVariation() > 0) {
				player.sendMessage(ChatColor.GREEN + "  +" + df.format(quote.getVariation()));
			} else {
				player.sendMessage(ChatColor.RED + "  " + df.format(quote.getVariation()));
			}

		}

		player.sendMessage("");
		player.spigot().sendMessage(buyStocks);
		player.sendMessage(messages.getPluginFooter());

	}

}