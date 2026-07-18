package dev.hugog.minecraft.blockstreet.ui.guis;

import dev.hugog.minecraft.blockstreet.BlockStreet;
import dev.hugog.minecraft.blockstreet.data.services.CompaniesService;
import dev.hugog.minecraft.blockstreet.data.services.PlayersService;
import dev.hugog.minecraft.blockstreet.ui.items.CompanyItem;
import dev.hugog.minecraft.blockstreet.ui.items.NotificationSettingsButtonItem;
import dev.hugog.minecraft.blockstreet.ui.items.PortfolioButtonItem;
import dev.hugog.minecraft.blockstreet.utils.Messages;
import dev.hugog.minecraft.dev_command.integration.Integration;
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

public class CompaniesGui extends AutoUpdatePagedGui {

    private final BlockStreet plugin;
    private final CompaniesService companiesService;
    private final Messages messages;

    public CompaniesGui(BlockStreet plugin, Player player, GuiManager guiManager, CompaniesService companiesService,
                        PlayersService playersService, Messages messages) {
        super(messages.getUiCompaniesTitle(), 5, player, plugin, 5 * 20L);

        this.plugin = plugin;
        this.companiesService = companiesService;
        this.messages = messages;

        setContent(this.buildContent());
        applyStructure(this.buildStructure(guiManager, playersService));
    }

    private List<InventoryItem> buildContent() {
        return companiesService.getAllCompanies()
                .stream()
                .sorted((company1, company2) -> company1.getName().compareToIgnoreCase(company2.getName()))
                .map(company -> new CompanyItem(Integration.createFromPlugin(plugin), messages, companiesService, company.getId()))
                .collect(Collectors.toList());
    }

    /**
     * Builds the inventory structure for the GUI, defining the layout and the items used in the GUI.
     *
     * @return the constructed InventoryStructure object representing the GUI layout
     */
    private InventoryStructure buildStructure(GuiManager guiManager, PlayersService playersService) {
        return new InventoryStructure.Builder()
                .withStructure(
                        "# # # o # n # # #",
                        "# x x x x x x x #",
                        "# x x x x x x x #",
                        "# x x x x x x x #",
                        "# # # < # > # # #"
                )
                .withIngredient('#', new SimpleItem.Builder().withMaterial(Material.GRAY_STAINED_GLASS_PANE).build())
                .withIngredient('<', new PreviousPageButton(messages.getUiPreviousPage()))
                .withIngredient('>', new NextPageButton(messages.getUiNextPage()))
                .withIngredient('o', new PortfolioButtonItem(plugin, guiManager, companiesService, playersService, messages))
                .withIngredient('n', new NotificationSettingsButtonItem(plugin, guiManager, playersService, messages))
                .withListMarker('x')
                .build();
    }

}
