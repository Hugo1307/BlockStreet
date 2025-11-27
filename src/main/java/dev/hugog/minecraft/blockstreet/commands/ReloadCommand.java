package dev.hugog.minecraft.blockstreet.commands;

import dev.hugog.minecraft.blockstreet.BlockStreet;
import dev.hugog.minecraft.blockstreet.utils.Messages;
import dev.hugog.minecraft.dev_command.annotations.AutoValidation;
import dev.hugog.minecraft.dev_command.annotations.Command;
import dev.hugog.minecraft.dev_command.annotations.Dependencies;
import dev.hugog.minecraft.dev_command.commands.BukkitDevCommand;
import dev.hugog.minecraft.dev_command.commands.data.BukkitCommandData;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Reload Command
 *
 * <p>Command to reload the plugin.
 * <p>Syntax: /invest reload
 *
 * @author Hugo1307
 * @since v1.0.0
 */
@AutoValidation(arguments = false, sender = false)
@Command(alias = "reload", description = "reloadCommandDescription", permission = "blockstreet.admin.reload")
@Dependencies(dependencies = {Messages.class, BlockStreet.class})
public class ReloadCommand extends BukkitDevCommand {

	public ReloadCommand(BukkitCommandData commandData, CommandSender commandSender, String[] args) {
		super(commandData, commandSender, args);
	}

	@Override
	public void execute() {
		Messages messages = getDependency(Messages.class);
		BlockStreet plugin = getDependency(BlockStreet.class);

		plugin.reloadConfig();
		messages.reload();

		plugin.stopSchedulers();
		plugin.registerSchedulers();

		getCommandSender().sendMessage(messages.getPluginPrefix() + ChatColor.GREEN + messages.getPluginReload());
	}

	@Override
	public List<String> onTabComplete(String[] strings) {
		return List.of();
	}

}
