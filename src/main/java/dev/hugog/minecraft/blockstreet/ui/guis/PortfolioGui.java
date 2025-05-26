package dev.hugog.minecraft.blockstreet.ui.guis;

import dev.hugog.minecraft.blockstreet.ui.AbstractPluginGui;
import dev.hugog.minecraft.blockstreet.ui.GuiManager;
import dev.hugog.minecraft.blockstreet.ui.items.InvestmentItem;
import dev.hugog.minecraft.blockstreet.ui.items.NavigateBackItem;
import dev.hugog.minecraft.blockstreet.ui.items.NextPageItem;
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

public class PortfolioGui extends AbstractPluginGui {

    public PortfolioGui(Player player, GuiManager guiManager, Messages messages) {
        super(messages.getUiPortfolioTitle(), "blockstreet.ui.portfolio", player, guiManager, messages);
    }

    @Override
    public Gui build() {
        List<Item> investmentsItems = guiManager.getPlayersService().getInvestments(player.getUniqueId())
                .stream()
                .map(investment -> new InvestmentItem(guiManager.getPlugin(), messages, investment,
                        guiManager.getCompaniesService().getCompanyDaoById(investment.getCompanyId())))
                .collect(Collectors.toList());

        return PagedGui.items()
                .setStructure(
                        "# # # # # # # # #",
                        "# x x x x x x x #",
                        "# x x x x x x x #",
                        "# x x x x x x x #",
                        "# # # < - > # # #"
                )
                .addIngredient('#', new SimpleItem(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName("")))
                .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .addIngredient('<', new PreviousPageItem(messages))
                .addIngredient('-', new NavigateBackItem(guiManager, messages))
                .addIngredient('>', new NextPageItem(messages))
                .setContent(investmentsItems)
                .build();
    }

}
