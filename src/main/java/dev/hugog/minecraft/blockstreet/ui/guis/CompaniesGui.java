package dev.hugog.minecraft.blockstreet.ui.guis;

import dev.hugog.minecraft.blockstreet.ui.AbstractPluginGui;
import dev.hugog.minecraft.blockstreet.ui.GuiManager;
import dev.hugog.minecraft.blockstreet.ui.items.CompanyItem;
import dev.hugog.minecraft.blockstreet.ui.items.NextPageItem;
import dev.hugog.minecraft.blockstreet.ui.items.PortfolioButtonItem;
import dev.hugog.minecraft.blockstreet.ui.items.PreviousPageItem;
import dev.hugog.minecraft.blockstreet.utils.Messages;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;

import java.util.List;
import java.util.stream.Collectors;

public class CompaniesGui extends AbstractPluginGui {

    public CompaniesGui(Player player, GuiManager guiManager, Messages messages) {
        super(messages.getUiCompaniesTitle(), "blockstreet.ui.companies", player, guiManager, messages);
    }

    @Override
    public Gui build() {
        List<Item> companiesItems = guiManager.getCompaniesService().getAllCompanies()
                .stream()
                .map(company -> new CompanyItem(guiManager.getPlugin(), messages, guiManager.getCompaniesService(), company.getId()))
                .collect(Collectors.toList());

        return PagedGui.items()
                .setStructure(
                        "# # # # o # # # #",
                        "# x x x x x x x #",
                        "# x x x x x x x #",
                        "# x x x x x x x #",
                        "# # # < # > # # #"
                )
                .addIngredient('#', new SimpleItem(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName("")))
                .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .addIngredient('<', new PreviousPageItem(messages))
                .addIngredient('>', new NextPageItem(messages))
                .addIngredient('o', new PortfolioButtonItem(guiManager, messages))
                .setContent(companiesItems)
                .build();
    }

}
