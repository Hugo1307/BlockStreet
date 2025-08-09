package dev.hugog.minecraft.blockstreet.listeners;

import com.google.inject.Inject;
import dev.hugog.minecraft.blockstreet.BlockStreet;
import dev.hugog.minecraft.blockstreet.api.services.AutoUpdateService;
import dev.hugog.minecraft.blockstreet.data.services.CompaniesService;
import dev.hugog.minecraft.blockstreet.data.services.PlayersService;
import dev.hugog.minecraft.blockstreet.utils.Messages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final BlockStreet blockStreet;
    private final Messages messages;
    private final AutoUpdateService autoUpdateService;
    private final PlayersService playersService;
    private final CompaniesService companiesService;

    @Inject
    public PlayerJoinListener(BlockStreet blockStreet, Messages messages, AutoUpdateService autoUpdateService,
                              PlayersService playersService, CompaniesService companiesService) {
        this.blockStreet = blockStreet;
        this.messages = messages;
        this.autoUpdateService = autoUpdateService;
        this.playersService = playersService;
        this.companiesService = companiesService;
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
        Player joinedPlayer = e.getPlayer();

        if (blockStreet.getConfig().getBoolean("BlockStreet.Updates.Reminder") && (joinedPlayer.isOp() || joinedPlayer.hasPermission("blockstreet.admin.*"))) {
            autoUpdateService.isUpdateAvailable().thenAcceptAsync((isUpdateAvailable) -> {
                if (isUpdateAvailable) {
                    blockStreet.getServer().getScheduler().runTaskLater(blockStreet, () -> {
                        joinedPlayer.sendMessage(messages.getPluginPrefix() + messages.getNewVersionAvailable());
                    }, 5000);
                }
            });
        }

        // Clean up investments for the player (remove investments in deleted companies)
        playersService.cleanUpInvestments(joinedPlayer.getUniqueId(), companiesService.getAllCompanies());
    }

}
