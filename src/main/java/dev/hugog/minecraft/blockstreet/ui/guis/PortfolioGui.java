package dev.hugog.minecraft.blockstreet.ui.guis;

import dev.hugog.minecraft.blockstreet.BlockStreet;
import dev.hugog.minecraft.blockstreet.data.dao.InvestmentDao;
import dev.hugog.minecraft.blockstreet.data.services.CompaniesService;
import dev.hugog.minecraft.blockstreet.data.services.PlayersService;
import dev.hugog.minecraft.blockstreet.ui.items.InvestmentItem;
import dev.hugog.minecraft.blockstreet.ui.items.NavigateBackItem;
import dev.hugog.minecraft.blockstreet.ui.items.PortfolioSummaryItem;
import dev.hugog.minecraft.blockstreet.utils.Messages;
import io.github.hugo1307.qubinventorylib.inventory.AutoUpdatePagedGui;
import io.github.hugo1307.qubinventorylib.inventory.InventoryStructure;
import io.github.hugo1307.qubinventorylib.item.InventoryItem;
import io.github.hugo1307.qubinventorylib.item.NextPageButton;
import io.github.hugo1307.qubinventorylib.item.PreviousPageButton;
import io.github.hugo1307.qubinventorylib.item.SimpleItem;
import io.github.hugo1307.qubinventorylib.manager.GuiManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class PortfolioGui extends AutoUpdatePagedGui {

    private final CompaniesService companiesService;
    private final Messages messages;
    private final List<InvestmentDao> playerInvestments;

    public PortfolioGui(BlockStreet plugin, Player player, GuiManager guiManager, CompaniesService companiesService,
                        PlayersService playersService, Messages messages) {
        super(messages.getUiPortfolioTitle(), 5, player, plugin, 5 * 20L);

        this.companiesService = companiesService;
        this.messages = messages;
        this.playerInvestments = playersService.getInvestments(player.getUniqueId()).stream()
                .filter(investment -> companiesService.companyExists(investment.getCompanyId()))
                .collect(Collectors.toList());

        setContent(buildContent(plugin));
        applyStructure(buildStructure(guiManager));
    }

    private List<InventoryItem> buildContent(BlockStreet plugin) {
        return playerInvestments.stream()
                .sorted((investment1, investment2) -> {
                    String companyName1 = companiesService.getCompanyById(investment1.getCompanyId()).getName();
                    String companyName2 = companiesService.getCompanyById(investment2.getCompanyId()).getName();
                    return companyName1.compareToIgnoreCase(companyName2);
                })
                .map(investment -> new InvestmentItem(plugin, messages, companiesService, investment))
                .collect(Collectors.toList());
    }

    private InventoryStructure buildStructure(GuiManager guiManager) {
        return new InventoryStructure.Builder()
                .withStructure(
                        "# # # # o # # # #",
                        "# x x x x x x x #",
                        "# x x x x x x x #",
                        "# x x x x x x x #",
                        "# # # < - > # # #"
                )
                .withIngredient('#', new SimpleItem.Builder().withMaterial(Material.GRAY_STAINED_GLASS_PANE).build())
                .withListMarker('x')
                .withIngredient('<', new PreviousPageButton(messages.getUiPreviousPage()))
                .withIngredient('-', new NavigateBackItem(guiManager, messages))
                .withIngredient('>', new NextPageButton(messages.getUiNextPage()))
                .withIngredient('o', new PortfolioSummaryItem(companiesService, playerInvestments, messages))
                .build();
    }

}
