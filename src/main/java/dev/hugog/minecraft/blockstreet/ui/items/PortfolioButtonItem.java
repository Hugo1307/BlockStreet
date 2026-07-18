package dev.hugog.minecraft.blockstreet.ui.items;

import dev.hugog.minecraft.blockstreet.BlockStreet;
import dev.hugog.minecraft.blockstreet.data.services.CompaniesService;
import dev.hugog.minecraft.blockstreet.data.services.PlayersService;
import dev.hugog.minecraft.blockstreet.ui.guis.PortfolioGui;
import dev.hugog.minecraft.blockstreet.utils.Messages;
import io.github.hugo1307.qubinventorylib.inventory.Gui;
import io.github.hugo1307.qubinventorylib.item.AbstractItem;
import io.github.hugo1307.qubinventorylib.manager.GuiManager;
import io.github.hugo1307.qubinventorylib.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

public class PortfolioButtonItem extends AbstractItem {

    private final BlockStreet plugin;
    private final GuiManager guiManager;
    private final CompaniesService companiesService;
    private final PlayersService playersService;
    private final Messages messages;

    public PortfolioButtonItem(BlockStreet plugin, GuiManager guiManager, CompaniesService companiesService,
                                PlayersService playersService, Messages messages) {
        super(new ItemBuilder(Material.DIAMOND)
                .setName(messages.getUiCheckPortfolioButtonTitle())
                .addLoreLine(messages.getUiCheckPortfolioButtonDescription())
                .build());
        this.plugin = plugin;
        this.guiManager = guiManager;
        this.companiesService = companiesService;
        this.playersService = playersService;
        this.messages = messages;
    }

    @Override
    public void onClick(ClickType clickType, Player player, Gui clickedGui, InventoryClickEvent event) {
        guiManager.navigateTo(player, new PortfolioGui(plugin, player, guiManager, companiesService, playersService, messages));
    }

}
