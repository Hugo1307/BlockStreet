package hugog.blockstreet.commands.implementation;

import hugog.blockstreet.commands.CmdDependencyInjector;
import hugog.blockstreet.commands.PluginCommand;
import hugog.blockstreet.others.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Main Command
 *
 * Command to display a help page with all the plugin's commands.
 *
 * Syntax /invest
 *
 * @author Hugo1307
 * @since v1.0.0
 */
public class MainCommand extends PluginCommand {

	public MainCommand(CommandSender sender, String[] args, CmdDependencyInjector cmdDependencyInjector) {
		super(sender, args, cmdDependencyInjector);
	}

	@Override
	public void execute() {

		Messages messages = new Messages();
		Player player = (Player) sender;

		if (player.isOp() || player.hasPermission("blockstreet.admin.*")) {

			player.sendMessage(messages.getPluginHeader());
			player.sendMessage("");
			player.sendMessage(ChatColor.GREEN + " • /invest" + ChatColor.GRAY + " - " + messages.getMenuMainCmd());
			player.sendMessage(ChatColor.GREEN + " • /invest companies" + ChatColor.GRAY + " - " + messages.getMenuCompaniesCmd());
			player.sendMessage(ChatColor.GREEN + " • /invest company" + ChatColor.DARK_GRAY + " [Id]" + ChatColor.GRAY + " - " + messages.getMenuCompanyCmd());
			player.sendMessage(ChatColor.GREEN + " • /invest buy" + ChatColor.DARK_GRAY + " [amount] [Id]" + ChatColor.GRAY + " - " + messages.getMenuBuyCmd());
			player.sendMessage(ChatColor.GREEN + " • /invest sell" + ChatColor.DARK_GRAY + " [amount] [Id]" + ChatColor.GRAY + " - " + messages.getMenuSellCmd());
			player.sendMessage(ChatColor.GREEN + " • /invest actions" + ChatColor.GRAY + " - " + messages.getMenuActionsCmd());
			player.sendMessage(ChatColor.GREEN + " • /invest time" + ChatColor.GRAY + " - " + messages.getMenuTimeCmd());
			player.sendMessage(ChatColor.GREEN + " • /invest create" + ChatColor.DARK_GRAY + " [name] [price] [risk(1-5)]" + ChatColor.GRAY + " - " + messages.getMenuCreateCompanyCmd());
			player.sendMessage(ChatColor.GREEN + " • /invest delete" + ChatColor.DARK_GRAY + " [Id]" + ChatColor.GRAY + " - " + messages.getMenuDeleteCompanyCmd());
			player.sendMessage(ChatColor.GREEN + " • /invest reload" + ChatColor.GRAY + " - " + messages.getMenuReloadCmd());
			player.sendMessage("");
			player.sendMessage(messages.getPluginFooter());

		}else if (player.hasPermission("blockstreet.command.main") || player.hasPermission("blockstreet.command.*")) {

			player.sendMessage(messages.getPluginHeader());
			player.sendMessage("");
			player.sendMessage(ChatColor.GREEN + " • /invest" + ChatColor.GRAY + " - " + messages.getMenuMainCmd());
			player.sendMessage(ChatColor.GREEN + " • /invest companies" + ChatColor.GRAY + " - " + messages.getMenuCompaniesCmd());
			player.sendMessage(ChatColor.GREEN + " • /invest company" + ChatColor.DARK_GRAY + " [Id]" + ChatColor.GRAY + " - " + messages.getMenuCompanyCmd());
			player.sendMessage(ChatColor.GREEN + " • /invest buy" + ChatColor.DARK_GRAY + " [amount] [Id]" + ChatColor.GRAY + " - " + messages.getMenuBuyCmd());
			player.sendMessage(ChatColor.GREEN + " • /invest sell" + ChatColor.DARK_GRAY + " [amount] [Id]" + ChatColor.GRAY + " - " + messages.getMenuSellCmd());
			player.sendMessage(ChatColor.GREEN + " • /invest stocks" + ChatColor.GRAY + " - " + messages.getMenuActionsCmd());
			player.sendMessage(ChatColor.GREEN + " • /invest time" + ChatColor.GRAY + " - " + messages.getMenuTimeCmd());
			player.sendMessage("");
			player.sendMessage(messages.getPluginFooter());

		}else {
			player.sendMessage(messages.getPluginPrefix() + ChatColor.RED + messages.getNoPermission());
		}

	}

}
