package eu.macsworks.fiverr.onecraft;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PluginLoader {

    private final Map<String, String> lang = new HashMap<>();

    public void load(){
        File configFile = new File(OneCraft.getInstance().getDataFolder() + "/config.yml");
        if(!configFile.exists()) OneCraft.getInstance().saveResource("config.yml", false);

        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        config.getConfigurationSection("lang").getKeys(false).forEach(s -> lang.put(s, ColorTranslator.translate(config.getString("lang." + s))));

        File storageFile = new File(OneCraft.getInstance().getDataFolder() + "/storage.yml");
        if(!storageFile.exists()) return;
        YamlConfiguration storage = YamlConfiguration.loadConfiguration(storageFile);
    }
    
    public String getLang(String key) {
        if(!lang.containsKey(key)) return key + " not present in config.yml. Add it under lang!";
        return lang.get(key);
    }

    public void save(){
        YamlConfiguration storage = new YamlConfiguration();

        try{
            storage.save(new File(OneCraft.getInstance().getDataFolder() + "/storage.yml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
