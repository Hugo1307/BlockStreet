package hugog.blockstreet.events;

import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import hugog.blockstreet.Main;
import hugog.blockstreet.others.Messages;
import hugog.blockstreet.update.AutoUpdate;

public class PJoinEvent implements Listener{

	private Main main;
	Timer timer = new Timer();
	
	public PJoinEvent(Main main) {
		this.main = main;
	}
	
	@EventHandler
	public void onPlayerJoinEvent (PlayerJoinEvent e) {
		
		Player p = e.getPlayer();
		Messages messages = new Messages(main.messagesConfig);
		
		if (main.getConfig().getBoolean("BlockStreet.Updates.Reminder") == true) {
			if (p.isOp() || p.hasPermission("blockstreet.admin.*")) {
				
				if (AutoUpdate.isNewVersionAvailable()) {
					timer.schedule(new TimerTask() {
						  @Override
						  public void run() {
						    p.sendMessage(messages.getPluginPrefix() + messages.getNewVersionAvailable());
						  }
						}, 5000);	
				}	
				
			}
			
		}		
		
	}
	
}
