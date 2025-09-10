package dev.hugog.minecraft.blockstreet.ui;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.hugog.minecraft.blockstreet.BlockStreet;
import dev.hugog.minecraft.blockstreet.data.services.CompaniesService;
import dev.hugog.minecraft.blockstreet.data.services.PlayersService;
import dev.hugog.minecraft.blockstreet.ui.guis.CompaniesGui;
import dev.hugog.minecraft.blockstreet.ui.guis.NotificationsGui;
import dev.hugog.minecraft.blockstreet.ui.guis.PortfolioGui;
import dev.hugog.minecraft.blockstreet.utils.Messages;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import org.bukkit.entity.Player;

@Singleton
@Getter
public class GuiManager {

    private final Map<UUID, Deque<AbstractPluginGui>> playerStack = new HashMap<>();
    private final BlockStreet plugin;
    private final Messages messages;
    private final PlayersService playersService;
    private final CompaniesService companiesService;

    @Inject
    public GuiManager(BlockStreet plugin, Messages messages, PlayersService playersService, CompaniesService companiesService) {
        this.plugin = plugin;
        this.messages = messages;
        this.playersService = playersService;
        this.companiesService = companiesService;
    }

    public void navigate(Player player, PluginGuiType guiType) {
        AbstractPluginGui gui = createGui(guiType, player);
        if (!playerStack.containsKey(player.getUniqueId())) {
            playerStack.put(player.getUniqueId(), new LinkedList<>());
        }
        playerStack.get(player.getUniqueId()).push(gui);
        gui.open();
    }

    public void navigateBack(Player player) {
        if (!playerStack.containsKey(player.getUniqueId())) {
            return;
        }
        Deque<AbstractPluginGui> stack = playerStack.get(player.getUniqueId());
        if (stack.size() <= 1) {
            return;
        }
        stack.pop().close();
        stack.peek().open();
    }

    public void close(Player player) {
        player.closeInventory();
        if (!playerStack.containsKey(player.getUniqueId())) {
            return;
        }
        Deque<AbstractPluginGui> stack = playerStack.get(player.getUniqueId());
        if (!stack.isEmpty()) {
            stack.pop().close();
            stack.clear();
        }
    }

    private AbstractPluginGui createGui(PluginGuiType pluginGuiType, Player player) {
        switch (pluginGuiType) {
            case PORTFOLIO_GUI:
                return new PortfolioGui(player, this, messages);
            case COMPANIES_GUI:
                return new CompaniesGui(player, this, messages);
            case NOTIFICATIONS_GUI:
                return new NotificationsGui(player, this, messages);
            default:
                throw new IllegalArgumentException("Unknown GUI type: " + pluginGuiType);
        }
    }

}
