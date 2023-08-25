package dev.hugog.minecraft.blockstreet.listeners;

import com.google.inject.Inject;
import dev.hugog.minecraft.blockstreet.BlockStreet;
import dev.hugog.minecraft.blockstreet.data.repositories.UpdatesRepository;
import dev.hugog.minecraft.blockstreet.others.Messages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

	private final BlockStreet blockStreet;
	private final Messages messages;
	private final UpdatesRepository updatesRepository;

	@Inject
	public PlayerJoinListener(BlockStreet blockStreet, Messages messages, UpdatesRepository updatesRepository) {
		this.blockStreet = blockStreet;
		this.messages = messages;
		this.updatesRepository = updatesRepository;
	}
	
	@EventHandler
	public void onPlayerJoinEvent (PlayerJoinEvent e) {
		
		final Player joinedPlayer = e.getPlayer();

		if (blockStreet.getConfig().getBoolean("BlockStreet.Updates.Reminder") && (joinedPlayer.isOp() || joinedPlayer.hasPermission("blockstreet.admin.*"))) {

			boolean isUpdateAvailable = updatesRepository.isUpdateAvailable();

			if (isUpdateAvailable) {
				blockStreet.getServer().getScheduler().runTaskLater(blockStreet, () -> {
					joinedPlayer.sendMessage(messages.getPluginPrefix() + messages.getNewVersionAvailable());
				}, 5000);
			}

		}		
		
	}
	
}
