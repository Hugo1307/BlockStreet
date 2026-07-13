package dev.hugog.minecraft.blockstreet.ui.guis;

import dev.hugog.minecraft.blockstreet.BlockStreet;
import dev.hugog.minecraft.blockstreet.data.services.CompaniesService;
import dev.hugog.minecraft.blockstreet.ui.items.CompanyItem;
import dev.hugog.minecraft.blockstreet.utils.Messages;
import dev.hugog.minecraft.dev_command.integration.Integration;
import io.github.hugo1307.qubinventorylib.inventory.AbstractPagedGui;
import io.github.hugo1307.qubinventorylib.inventory.InventoryStructure;
import io.github.hugo1307.qubinventorylib.item.InventoryItem;
import io.github.hugo1307.qubinventorylib.item.NextPageButton;
import io.github.hugo1307.qubinventorylib.item.PreviousPageButton;
import io.github.hugo1307.qubinventorylib.item.SimpleItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CompaniesGui extends AbstractPagedGui {

    private final Integration commandsIntegration;
    private final CompaniesService companiesService;
    private final Messages messages;

    public CompaniesGui(Player player, Integration commandsIntegration, CompaniesService companiesService, Messages messages) {
        super(messages.getUiCompaniesTitle(), 5, player);

        this.commandsIntegration = commandsIntegration;
        this.companiesService = companiesService;
        this.messages = messages;

        setContent(this.buildContent());
        applyStructure(this.buildStructure());
    }

    private List<InventoryItem> buildContent() {
        return companiesService.getAllCompanies()
                .stream()
                .map(company -> new CompanyItem(commandsIntegration, messages, companiesService, company.getId()))
                .collect(Collectors.toList());
    }

    /**
     * Builds the inventory structure for the GUI, defining the layout and the items used in the GUI.
     *
     * @return the constructed InventoryStructure object representing the GUI layout
     */
    private InventoryStructure buildStructure() {
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
                .withListMarker('x')
                .build();
    }

}
