package dev.hugog.minecraft.blockstreet.ui.items;

import dev.hugog.minecraft.blockstreet.BlockStreet;
import dev.hugog.minecraft.blockstreet.commands.SellCommand;
import dev.hugog.minecraft.blockstreet.data.dao.CompanyDao;
import dev.hugog.minecraft.blockstreet.data.dao.InvestmentDao;
import dev.hugog.minecraft.blockstreet.data.services.CompaniesService;
import dev.hugog.minecraft.blockstreet.utils.FormattingUtils;
import dev.hugog.minecraft.blockstreet.utils.Messages;
import dev.hugog.minecraft.blockstreet.utils.VisualizationUtils;
import dev.hugog.minecraft.dev_command.DevCommand;
import dev.hugog.minecraft.dev_command.integration.Integration;
import io.github.hugo1307.qubinventorylib.inventory.Gui;
import io.github.hugo1307.qubinventorylib.item.RefreshableItem;
import io.github.hugo1307.qubinventorylib.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.text.MessageFormat;

public class InvestmentItem extends RefreshableItem {

    private final BlockStreet plugin;
    private final InvestmentDao investment;

    public InvestmentItem(BlockStreet plugin, Messages messages, CompaniesService companiesService, InvestmentDao investment) {
        super(() -> getItem(messages, companiesService, investment));
        this.plugin = plugin;
        this.investment = investment;
    }

    private static ItemStack getItem(Messages messages, CompaniesService companiesService, InvestmentDao investment) {
        if (!companiesService.companyExists(investment.getCompanyId())) {
            return new ItemBuilder(Material.AIR).build();
        }

        CompanyDao company = companiesService.getCompanyById(investment.getCompanyId());
        double currentValue = investment.getSharesAmount() * company.getCurrentSharePrice();

        return new ItemBuilder(company.getIcon() != null ? company.getIcon() : Material.DIAMOND)
                .setName(ChatColor.GOLD + company.getName() + (!company.isBankrupt() ? MessageFormat.format(messages.getUiCompanyItemLastVariation(),
                        VisualizationUtils.formatCompanyVariation(investment.getInvestmentVariation(company.getCurrentSharePrice()))) : ""))
                .addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS)
                .setLore(
                        company.isBankrupt() ? messages.getCompanyStatusBankrupt() : messages.getCompanyStatusTrading(),
                        "",
                        MessageFormat.format(messages.getUiInvestmentItemShares(), investment.getSharesAmount()),
                        MessageFormat.format(messages.getUiInvestmentItemCurrentValue(), FormattingUtils.formatDouble(currentValue)),
                        MessageFormat.format(messages.getUiInvestmentItemAverageBuyPrice(), FormattingUtils.formatDouble(investment.getAverageBuyPrice())),
                        "",
                        messages.getUiInvestmentItemSellOne(),
                        messages.getUiInvestmentItemSellAll()
                )
                .build();
    }

    @Override
    public void onClick(ClickType clickType, Player player, Gui clickedGui, InventoryClickEvent event) {
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
        devCommand.getCommandHandler().executeCommand(Integration.createFromPlugin(plugin), player, SellCommand.class,
                String.valueOf(investment.getCompanyId()), String.valueOf(sharesToSell));

        if (investment.getSharesAmount() - sharesToSell >= 0) {
            investment.setSharesAmount(investment.getSharesAmount() - sharesToSell);
        }
        clickedGui.update();
    }

}
