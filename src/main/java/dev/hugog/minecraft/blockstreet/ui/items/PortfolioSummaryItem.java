package dev.hugog.minecraft.blockstreet.ui.items;

import dev.hugog.minecraft.blockstreet.data.dao.InvestmentDao;
import dev.hugog.minecraft.blockstreet.data.services.CompaniesService;
import dev.hugog.minecraft.blockstreet.utils.FormattingUtils;
import dev.hugog.minecraft.blockstreet.utils.Messages;
import org.bukkit.Material;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;

import java.text.MessageFormat;
import java.util.List;

/**
 * Item that provides a summary of the player's portfolio.
 */
public class PortfolioSummaryItem extends SimpleItem {

    public PortfolioSummaryItem(CompaniesService companiesService, List<InvestmentDao> playerInvestments, Messages messages) {
        super(getItemProvider(companiesService, playerInvestments, messages));
    }

    private static ItemProvider getItemProvider(CompaniesService companiesService, List<InvestmentDao> playerInvestments, Messages messages) {
        int companyCount = playerInvestments.size();
        int totalShares = playerInvestments.stream()
                .mapToInt(InvestmentDao::getSharesAmount)
                .sum();
        double totalValue = playerInvestments.stream()
                .mapToDouble(investment -> investment.getSharesAmount() * companiesService.getCompanyById(investment.getCompanyId()).getCurrentSharePrice())
                .sum();

        return new ItemBuilder(Material.DARK_OAK_SIGN)
                .setDisplayName(messages.getUiPortfolioSummaryItemTitle())
                .addLoreLines(
                        "",
                        MessageFormat.format(messages.getUiPortfolioSummaryItemCompanies(), companyCount),
                        MessageFormat.format(messages.getUiPortfolioSummaryItemShares(), totalShares),
                        MessageFormat.format(messages.getUiPortfolioSummaryItemTotalValue(), FormattingUtils.formatDouble(totalValue))
                );
    }

}
