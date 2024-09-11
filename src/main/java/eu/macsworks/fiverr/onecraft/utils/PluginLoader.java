package eu.macsworks.fiverr.onecraft.utils;

import eu.macsworks.fiverr.onecraft.OneCraft;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PluginLoader {

	private final Map<String, String> lang = new HashMap<>();
	@Getter private long playAmt;

	@Getter private String sqlHost, sqlUser, sqlPassword;

	public void load() {
		File configFile = new File(OneCraft.getInstance().getDataFolder() + "/config.yml");
		if (!configFile.exists()) OneCraft.getInstance().saveResource("config.yml", false);

		YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
		config.getConfigurationSection("lang").getKeys(false).forEach(s -> lang.put(s, ColorTranslator.translate(config.getString("lang." + s))));

		playAmt = config.getLong("playAmt");

		sqlHost = config.getString("sql.host");
		sqlUser = config.getString("sql.user");
		sqlPassword = config.getString("sql.password");

		File storageFile = new File(OneCraft.getInstance().getDataFolder() + "/storage.yml");
		if (!storageFile.exists()) return;
		YamlConfiguration storage = YamlConfiguration.loadConfiguration(storageFile);

		OneCraft.getInstance().getOneCraftManager().setPrevLoc(storage.getLocation("prev-loc"));
		if(storage.getConfigurationSection("items") != null){
			OneCraft.getInstance().getOneCraftManager().setPlayerInventory(new ArrayList<>());
			storage.getConfigurationSection("items").getKeys(false).forEach(s -> {
				OneCraft.getInstance().getOneCraftManager().getPlayerInventory().add(storage.getItemStack("items." + s));
			});
		}
	}

	public String getLang(String key) {
		if (!lang.containsKey(key)) return key + " not present in config.yml. Add it under lang!";
		return lang.get(key);
	}

	public void save() {
		YamlConfiguration storage = new YamlConfiguration();

		storage.set("prev-loc", OneCraft.getInstance().getOneCraftManager().getPrevLoc());
		List<ItemStack> items = OneCraft.getInstance().getOneCraftManager().getPlayerInventory();
		if(items != null){
			items.forEach(it -> {
				storage.set("items." + items.indexOf(it), it);
			});
		}

		try {
			storage.save(new File(OneCraft.getInstance().getDataFolder() + "/storage.yml"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
