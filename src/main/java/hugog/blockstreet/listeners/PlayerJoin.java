package hugog.blockstreet.listeners;

import com.google.inject.Inject;
import hugog.blockstreet.Main;
import hugog.blockstreet.others.Messages;
import hugog.blockstreet.update.UpdateChecker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener{

	private final Main main;
	private final UpdateChecker updateChecker;

	@Inject
	public PlayerJoin(Main main, UpdateChecker updateChecker) {
		this.main = main;
		this.updateChecker = updateChecker;
	}
	
	@EventHandler
	public void onPlayerJoinEvent (PlayerJoinEvent e) {
		
		final Player joinedPlayer = e.getPlayer();
		final Messages messages = new Messages();
		
		if (main.getConfig().getBoolean("BlockStreet.Updates.Reminder") && (joinedPlayer.isOp() || joinedPlayer.hasPermission("blockstreet.admin.*"))) {

			if (updateChecker.isNewVersionAvailable()) {

				main.getServer().getScheduler().runTaskLater(main, () -> {
					joinedPlayer.sendMessage(messages.getPluginPrefix() + messages.getNewVersionAvailable());
				}, 5000);

			}

		}		
		
	}
	
}
