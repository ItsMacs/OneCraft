package eu.macsworks.fiverr.onecraft.listeners;

import eu.macsworks.fiverr.onecraft.OneCraft;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

	@EventHandler
	public void playerJoin(PlayerJoinEvent event){
		OneCraft.getInstance().getOneCraftManager().addPlayer(event.getPlayer());
	}

}
