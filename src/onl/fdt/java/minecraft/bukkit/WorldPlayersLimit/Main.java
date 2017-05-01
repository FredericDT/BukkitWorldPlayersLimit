package onl.fdt.java.minecraft.bukkit.WorldPlayersLimit;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	public static Map<String, Integer> worlds = new HashMap<>();
	
	@Override
	public void onEnable() {
		this.saveDefaultConfig();
		this.getServer().getPluginManager().registerEvents(new PlayerWorldChangeListener(this), this);
		if (checkIsValidConfig()) {
			for (String key : this.getConfig().getConfigurationSection("config.worlds").getKeys(false)) {
				worlds.put(key, this.getConfig().getInt("config.worlds." + key + ".limit"));
			}
		}
	}
	
	@Override
	public void onDisable() {
		this.saveConfig();
	}
	
	public World getFallbackworld() {
		if (this.getConfig().getString("config.fallbackworld") != null) {
			if (this.getServer().getWorld(this.getConfig().getString("config.fallbackworld")) != null) {
				return this.getServer().getWorld(this.getConfig().getString("config.fallbackworld"));
			}
		}
		return this.getServer().getWorlds().get(0);
	}
	
	private boolean checkIsValidConfig() {
		if (this.getConfig().get("config.worlds") != null) {
			return this.getConfig().getConfigurationSection("config.worlds").getKeys(false).size() > 0;
		}
		return false;
	}
	
}
