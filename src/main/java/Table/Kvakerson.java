package Table;

import org.bukkit.plugin.java.JavaPlugin;

public final class Kvakerson extends JavaPlugin {
    public static Kvakerson instance;
    @Override
    public void onEnable() {
        instance=this;

        getServer().getPluginManager().registerEvents(new Table(),this);
        Table.workingMeth();
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
