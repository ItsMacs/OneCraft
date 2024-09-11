package eu.macsworks.fiverr.onecraft.listeners;

import eu.macsworks.fiverr.onecraft.OneCraft;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

	@EventHandler
	public void playerQuit(PlayerQuitEvent event){
		OneCraft.getInstance().getOneCraftManager().removePlayer(event.getPlayer());
	}

}
