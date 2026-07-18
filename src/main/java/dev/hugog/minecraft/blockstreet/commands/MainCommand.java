package dev.hugog.minecraft.blockstreet.commands;

import dev.hugog.minecraft.blockstreet.BlockStreet;
import dev.hugog.minecraft.blockstreet.data.services.CompaniesService;
import dev.hugog.minecraft.blockstreet.data.services.PlayersService;
import dev.hugog.minecraft.blockstreet.ui.guis.CompaniesGui;
import dev.hugog.minecraft.blockstreet.utils.Messages;
import dev.hugog.minecraft.dev_command.annotations.AutoValidation;
import dev.hugog.minecraft.dev_command.annotations.Command;
import dev.hugog.minecraft.dev_command.annotations.Dependencies;
import dev.hugog.minecraft.dev_command.commands.BukkitDevCommand;
import dev.hugog.minecraft.dev_command.commands.data.BukkitCommandData;
import io.github.hugo1307.qubinventorylib.QubInventoryLib;
import io.github.hugo1307.qubinventorylib.manager.GuiManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@AutoValidation
@Command(alias = "", description = "mainCommand.description", permission = "blockstreet.command.main", isPlayerOnly = true)
@Dependencies(dependencies = {Messages.class, PlayersService.class})
@SuppressWarnings("unused")
public class MainCommand extends BukkitDevCommand {

    private final BlockStreet plugin;
    private final QubInventoryLib qubInventoryLib;
    private final CompaniesService companiesService;
    private final PlayersService playersService;
    private final Messages messages;

    public MainCommand(BukkitCommandData commandData, CommandSender commandSender, String[] args) {
        super(commandData, commandSender, args);

        this.plugin = getDependency(BlockStreet.class);
        this.qubInventoryLib = getDependency(QubInventoryLib.class);
        this.companiesService = getDependency(CompaniesService.class);
        this.playersService = getDependency(PlayersService.class);
        this.messages = getDependency(Messages.class);
    }

    @Override
    public void execute() {
        Player player = (Player) getCommandSender();
        GuiManager guiManager = qubInventoryLib.getGuiManager();
        guiManager.startSession(player,
                new CompaniesGui(plugin, player, guiManager, companiesService, playersService, messages));
    }

    @Override
    public List<String> onTabComplete(String[] strings) {
        return List.of();
    }

}
