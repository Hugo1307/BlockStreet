package dev.hugog.minecraft.blockstreet.ui.items;

import dev.hugog.minecraft.blockstreet.data.dao.InvestmentDao;
import dev.hugog.minecraft.blockstreet.data.services.CompaniesService;
import dev.hugog.minecraft.blockstreet.utils.FormattingUtils;
import dev.hugog.minecraft.blockstreet.utils.Messages;
import dev.hugog.minecraft.blockstreet.utils.VisualizationUtils;
import io.github.hugo1307.qubinventorylib.inventory.Gui;
import io.github.hugo1307.qubinventorylib.item.RefreshableItem;
import io.github.hugo1307.qubinventorylib.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.text.MessageFormat;
import java.util.List;

/**
 * Item that provides a summary of the player's portfolio.
 */
public class PortfolioSummaryItem extends RefreshableItem {

    public PortfolioSummaryItem(CompaniesService companiesService, List<InvestmentDao> playerInvestments, Messages messages) {
        super(() -> getItem(companiesService, playerInvestments, messages));
    }

    private static ItemStack getItem(CompaniesService companiesService, List<InvestmentDao> playerInvestments, Messages messages) {
        int companyCount = playerInvestments.size();
        int totalShares = playerInvestments.stream()
                .mapToInt(InvestmentDao::getSharesAmount)
                .sum();
        double totalValue = playerInvestments.stream()
                .filter(investment -> companiesService.companyExists(investment.getCompanyId()))
                .mapToDouble(investment -> investment.getSharesAmount() * companiesService.getCompanyById(investment.getCompanyId()).getCurrentSharePrice())
                .sum();
        double totalInvestment = playerInvestments.stream()
                .mapToDouble(investment -> investment.getSharesAmount() * investment.getAverageBuyPrice())
                .sum();
        double profitPercentage = (totalValue - totalInvestment) / totalInvestment * 100;

        return new ItemBuilder(Material.DARK_OAK_SIGN)
                .setName(messages.getUiPortfolioSummaryItemTitle() + MessageFormat.format(messages.getUiCompanyItemLastVariation(),
                        VisualizationUtils.formatCompanyVariation(profitPercentage)))
                .setLore(
                        "",
                        MessageFormat.format(messages.getUiPortfolioSummaryItemCompanies(), companyCount),
                        MessageFormat.format(messages.getUiPortfolioSummaryItemShares(), totalShares),
                        MessageFormat.format(messages.getUiPortfolioSummaryItemTotalValue(), FormattingUtils.formatDouble(totalValue))
                )
                .build();
    }

    @Override
    public void onClick(ClickType clickType, Player player, Gui clickedGui, InventoryClickEvent event) {
        // Decorative item, no click behavior
    }

}
