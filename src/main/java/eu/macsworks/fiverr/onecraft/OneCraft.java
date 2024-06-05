/**
    OneCraft developed by Macs @ MacsWorks.eu in 2024
**/

package eu.macsworks.fiverr.onecraft;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class OneCraft extends JavaPlugin {

    private static OneCraft instance = null;
    public static OneCraft getInstance() { return OneCraft.instance; }
    private static void setInstance(OneCraft in) { OneCraft.instance = in; }

    @Getter private PluginLoader macsPluginLoader;

    @Override
    public void onEnable() {
        setInstance(this);

        macsPluginLoader = new PluginLoader();
        macsPluginLoader.load();

        loadTasks();
        loadEvents();
        loadCommands();

        Bukkit.getLogger().info("--------------------------------------");
        Bukkit.getLogger().info("OneCraft was made by the team at macsworks.eu!");
        Bukkit.getLogger().info("--------------------------------------");
    }

    private void loadTasks(){

    }

    private void loadEvents(){
        
    }

    private void loadCommands(){

    }

    @Override
    public void onDisable() {
        macsPluginLoader.save();

        Bukkit.getLogger().info("--------------------------------------");
        Bukkit.getLogger().info("OneCraft was made by the team at macsworks.eu!");
        Bukkit.getLogger().info("--------------------------------------");
    }
}
