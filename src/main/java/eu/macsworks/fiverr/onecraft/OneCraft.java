/**
 * OneCraft developed by Macs @ MacsWorks.eu in 2024
 **/

package eu.macsworks.fiverr.onecraft;

import eu.macsworks.fiverr.onecraft.listeners.PlayerJoinListener;
import eu.macsworks.fiverr.onecraft.listeners.PlayerQuitListener;
import eu.macsworks.fiverr.onecraft.managers.OneCraftManager;
import eu.macsworks.fiverr.onecraft.utils.DataSource;
import eu.macsworks.fiverr.onecraft.utils.PluginLoader;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class OneCraft extends JavaPlugin {

	private static OneCraft instance = null;

	public static OneCraft getInstance() {
		return OneCraft.instance;
	}

	private static void setInstance(OneCraft in) {
		OneCraft.instance = in;
	}

	@Getter
	private PluginLoader macsPluginLoader;

	@Getter
	private OneCraftManager oneCraftManager;

	@Getter
	private DataSource dataSource;

	@Override
	public void onEnable() {
		setInstance(this);

		oneCraftManager = new OneCraftManager();

		macsPluginLoader = new PluginLoader();
		macsPluginLoader.load();

		dataSource = new DataSource();

		loadTasks();
		loadEvents();
		loadCommands();

		Bukkit.getLogger().info("--------------------------------------");
		Bukkit.getLogger().info("OneCraft was made by the team at macsworks.eu!");
		Bukkit.getLogger().info("--------------------------------------");
	}

	private void loadTasks() {
		Bukkit.getScheduler().runTaskTimer(this, oneCraftManager::tick, 0L, 20L);
	}

	private void loadEvents() {
		Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), this);

		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "comm:onecraft");
	}

	private void loadCommands() {

	}

	@Override
	public void onDisable() {
		macsPluginLoader.save();

		Bukkit.getLogger().info("--------------------------------------");
		Bukkit.getLogger().info("OneCraft was made by the team at macsworks.eu!");
		Bukkit.getLogger().info("--------------------------------------");
	}
}
