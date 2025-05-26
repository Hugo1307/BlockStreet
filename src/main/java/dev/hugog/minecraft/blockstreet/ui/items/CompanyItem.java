package dev.hugog.minecraft.blockstreet.ui.items;

import dev.hugog.minecraft.blockstreet.BlockStreet;
import dev.hugog.minecraft.blockstreet.commands.BuyCommand;
import dev.hugog.minecraft.blockstreet.data.dao.CompanyDao;
import dev.hugog.minecraft.blockstreet.utils.Messages;
import dev.hugog.minecraft.blockstreet.utils.VisualizationUtils;
import dev.hugog.minecraft.dev_command.DevCommand;
import dev.hugog.minecraft.dev_command.integration.Integration;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

import java.text.MessageFormat;
import java.util.List;

public class CompanyItem extends AbstractItem {

    private final BlockStreet plugin;
    private final Messages messages;
    private final CompanyDao company;

    public CompanyItem(BlockStreet plugin, Messages messages, CompanyDao company) {
        this.plugin = plugin;
        this.messages = messages;
        this.company = company;
    }

    @Override
    public ItemProvider getItemProvider() {
        double marketCap = company.getCurrentSharePrice() * company.getTotalShares();
        double totalVariation = (company.getCurrentSharePrice() -  company.getInitialSharePrice()) /  company.getInitialSharePrice() * 100;
        double lastVariation = !company.getHistoric().isEmpty() ? company.getHistoric().peek().getVariation() * 100 : 0;

        return new ItemBuilder(Material.EMERALD)
                .setDisplayName(ChatColor.GOLD + company.getName() + MessageFormat.format(messages.getUiCompanyItemLastVariation(), VisualizationUtils.formatCompanyVariation(lastVariation)))
                .setItemFlags(List.of(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS))
                .addLoreLines(
                        "",
                        MessageFormat.format(messages.getUiCompanyItemRisk(), company.getRisk()),
                        MessageFormat.format(messages.getUiCompanyItemSharePrice(), company.getCurrentSharePrice()),
                        MessageFormat.format(messages.getUiCompanyItemMarketCap(), marketCap),
                        MessageFormat.format(messages.getUiCompanyItemAllTimeVariation(), VisualizationUtils.formatCompanyVariation(totalVariation)),
                        "",
                        messages.getUiCompanyItemBuyOneShare(),
                        messages.getUiCompanyItemBuyTenShares()
                );
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        int sharesToBuy = -1;
        if (clickType == ClickType.RIGHT) {
            sharesToBuy = 1; // Right click to buy one share
        } else if (clickType == ClickType.SHIFT_RIGHT) {
            sharesToBuy = 10; // Shift + Right click to buy ten shares
        }

        if (sharesToBuy <= 0) {
            return; // No shares to buy
        }

        DevCommand devCommand = DevCommand.getOrCreateInstance();
        // Execute the command to buy the shares
        devCommand.getCommandHandler().executeCommand(Integration.createFromPlugin(plugin), player, BuyCommand.class,
                String.valueOf(company.getId()), String.valueOf(sharesToBuy));
    }

}
