package dev.hugog.minecraft.blockstreet.ui.items;

import dev.hugog.minecraft.blockstreet.BlockStreet;
import dev.hugog.minecraft.blockstreet.commands.BuyCommand;
import dev.hugog.minecraft.blockstreet.data.dao.CompanyDao;
import dev.hugog.minecraft.blockstreet.data.services.CompaniesService;
import dev.hugog.minecraft.blockstreet.utils.FormattingUtils;
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
import xyz.xenondevs.invui.item.impl.AutoUpdateItem;

import java.text.MessageFormat;
import java.util.List;

public class CompanyItem extends AutoUpdateItem {

    private final BlockStreet plugin;
    private final CompanyDao company;

    public CompanyItem(BlockStreet plugin, Messages messages, CompaniesService companiesService, int companyId) {
        super(20 * 5, () -> getItemProvider(companiesService.getCompanyById(companyId), messages));
        this.plugin = plugin;
        this.company = companiesService.getCompanyById(companyId);
    }

    public static ItemProvider getItemProvider(CompanyDao company, Messages messages) {
        double marketCap = company.getCurrentSharePrice() * company.getTotalShares();
        double totalVariation = (company.getCurrentSharePrice() - company.getInitialSharePrice()) / company.getInitialSharePrice() * 100;
        double lastVariation = !company.getHistoric().isEmpty() ? company.getHistoric().peek().getVariation() * 100 : 0;

        return new ItemBuilder(Material.EMERALD)
                .setDisplayName(ChatColor.GOLD + company.getName() + (!company.isBankrupt() ? MessageFormat.format(messages.getUiCompanyItemLastVariation(), VisualizationUtils.formatCompanyVariation(lastVariation)) : ""))
                .setItemFlags(List.of(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS))
                .addLoreLines(
                        company.isBankrupt() ? messages.getCompanyStatusBankrupt() : messages.getCompanyStatusTrading(),
                        "",
                        MessageFormat.format(messages.getUiCompanyItemRisk(), company.getRisk()),
                        MessageFormat.format(messages.getUiCompanyItemSharePrice(), FormattingUtils.formatDouble(company.getCurrentSharePrice())),
                        MessageFormat.format(messages.getUiCompanyItemMarketCap(), FormattingUtils.formatDouble(marketCap)),
                        MessageFormat.format(messages.getUiCompanyItemAllTimeVariation(), VisualizationUtils.formatCompanyVariation(totalVariation)),
                        "",
                        messages.getUiCompanyItemBuyOneShare(),
                        messages.getUiCompanyItemBuyTenShares(),
                        messages.getUiCompanyItemBuyHundredShares()
                );
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        int sharesToBuy = -1;
        if (clickType == ClickType.RIGHT) {
            sharesToBuy = 1; // Right click to buy one share
        } else if (clickType == ClickType.SHIFT_RIGHT) {
            sharesToBuy = 10; // Shift + Right click to buy ten shares
        } else if (clickType == ClickType.SHIFT_LEFT) {
            sharesToBuy = 100; // Shift + Left click to buy one hundred shares
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
