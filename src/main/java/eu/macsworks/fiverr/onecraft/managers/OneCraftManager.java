package eu.macsworks.fiverr.onecraft.managers;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import eu.macsworks.fiverr.onecraft.OneCraft;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.units.qual.A;

import java.text.SimpleDateFormat;
import java.util.*;

public class OneCraftManager {

	private final Queue<Player> enqueued = new LinkedList<>();
	private UUID current;
	private long cooldown = -20;
	private long quitCountdown = -1;

	@Getter @Setter
	private Location prevLoc;

	@Getter @Setter
	private List<ItemStack> playerInventory;

	/**
	 * Enqueues a player to play on OneCraft
	 * @param player
	 */
	public void addPlayer(Player player) {
		if(cooldown == -20) cooldown = OneCraft.getInstance().getMacsPluginLoader().getPlayAmt();
		if(player.hasPermission("onecraft.admin")) return;

		if(player.getUniqueId().equals(current)){
			quitCountdown = -1;
			current = player.getUniqueId();
			return;
		}

		enqueued.add(player);
		Bukkit.getScheduler().scheduleSyncDelayedTask(OneCraft.getInstance(), () -> {
			player.setGameMode(GameMode.SPECTATOR);
		}, 2L);
		player.getInventory().clear();
	}

	public void removePlayer(Player player) {
		enqueued.remove(player);

		if(current == player.getUniqueId()) {
			prepareQuit(player);
			quitCountdown = 60;
		}
	}

	private Player getCurrent(){
		return Bukkit.getPlayer(current);
	}

	/**
	 * Saves chosen player information upon quitting
	 * @param player The player who's currently playing OneCraft (will not check if the player is the correct one)
	 */
	private void prepareQuit(Player player) {
		prevLoc = player.getLocation().clone();
		playerInventory = new ArrayList<>();
		for(int i = 0; i < 40; i++){
			ItemStack stack = player.getInventory().getItem(i);
			playerInventory.add(stack == null ? new ItemStack(Material.AIR) : stack.clone());
		}
	}

	public void tick(){
		//Update the remaining time on SQL for display on Velocity (Separate plugin)
		OneCraft.getInstance().getDataSource().setTime(cooldown);

		//If the current player is not present, and it has never been chosen / the player has quit for more than the allowed quit time, choose another one
		if(current == null || getCurrent() == null){
			if(quitCountdown != -1){
				quitCountdown--;
				if(quitCountdown <= 0){
					current = null;
					choosePlayer();
					return;
				}
				return;
			}

			if(enqueued.isEmpty()) return;
			choosePlayer();
			return;
		}

		//Send remaining time actionbar
		getCurrent().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(OneCraft.getInstance().getMacsPluginLoader().getLang("timer")
				.replace("%timer%", new SimpleDateFormat("mm:ss").format(new Date(1000L * cooldown)))));

		cooldown--;
		if(cooldown <= 0){ //Time elapsed, time to choose a new player
			prepareQuit(getCurrent());

			getCurrent().kickPlayer(OneCraft.getInstance().getMacsPluginLoader().getLang("time-expired"));
			choosePlayer();
		}
	}

	/**
	 * Pulls the eldest player from the queue, if present, and prepares them for play
	 */
	private void choosePlayer() {
		Player cPlayer = enqueued.poll();
		if(cPlayer == null) return;

		current = cPlayer.getUniqueId();

		cooldown = OneCraft.getInstance().getMacsPluginLoader().getPlayAmt();
		quitCountdown = -1;

		getCurrent().teleport(prevLoc == null ? getCurrent().getWorld().getSpawnLocation() : prevLoc);
		getCurrent().setGameMode(GameMode.SURVIVAL);

		if(playerInventory != null){
			//Set stored inventory to the newly chosen player
			getCurrent().getInventory().clear();
			for(int i = 0; i < playerInventory.size(); i++){
				getCurrent().getInventory().setItem(i, playerInventory.get(i));
			}
		}

		playerInventory = null;
		prevLoc = null;
	}
}
