package dev.hugog.minecraft.blockstreet.ui.items;

import dev.hugog.minecraft.blockstreet.BlockStreet;
import dev.hugog.minecraft.blockstreet.commands.SellCommand;
import dev.hugog.minecraft.blockstreet.data.dao.CompanyDao;
import dev.hugog.minecraft.blockstreet.data.dao.InvestmentDao;
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

public class InvestmentItem extends AbstractItem {

    private final BlockStreet plugin;
    private final Messages messages;
    private final InvestmentDao investment;
    private final CompanyDao company;

    public InvestmentItem(BlockStreet plugin, Messages messages, InvestmentDao investment, CompanyDao company) {
        this.plugin = plugin;
        this.messages = messages;
        this.investment = investment;
        this.company = company;
    }

    @Override
    public ItemProvider getItemProvider() {
        double currentValue = investment.getSharesAmount() * company.getCurrentSharePrice();
        double lastVariation = !company.getHistoric().isEmpty() ? company.getHistoric().peek().getVariation() * 100 : 0;

        return new ItemBuilder(Material.DIAMOND)
                .setDisplayName(ChatColor.GOLD + company.getName() + MessageFormat.format(messages.getUiCompanyItemLastVariation(), VisualizationUtils.formatCompanyVariation(lastVariation)))
                .setItemFlags(List.of(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS))
                .addLoreLines(
                        "",
                        MessageFormat.format(messages.getUiInvestmentItemShares(), investment.getSharesAmount()),
                        MessageFormat.format(messages.getUiInvestmentItemCurrentValue(), currentValue),
                        "",
                        messages.getUiInvestmentItemSellOne(),
                        messages.getUiInvestmentItemSellAll()
                );
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        int sharesToSell = -1;
        if (clickType == ClickType.RIGHT) {
            sharesToSell = 1;
        } else if (clickType == ClickType.SHIFT_RIGHT) {
            sharesToSell = investment.getSharesAmount(); // Sell all shares
        }

        if (sharesToSell <= 0) {
            return; // No shares to sell
        }

        DevCommand devCommand = DevCommand.getOrCreateInstance();
        // Execute the command to sell shares
        devCommand.getCommandHandler().executeCommand(Integration.createFromPlugin(plugin), player, SellCommand.class,
                String.valueOf(investment.getCompanyId()), String.valueOf(sharesToSell));

        investment.setSharesAmount(investment.getSharesAmount() - sharesToSell);
        notifyWindows();
    }

}
