package onl.fdt.java.minecraft.bukkit.WorldPlayersLimit;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	@Override
	public void onEnable() {
		this.saveDefaultConfig();
		TextTemplate.textDefatulTemplate.add("%world%");
		TextTemplate.textDefatulTemplate.add("%current% / %limit%");
		this.getServer().getPluginManager().registerEvents(new PlayerWorldChangeListener(this), this);
		try {
			if (checkIsValidConfig()) {
				//for (String key : this.getConfig().getConfigurationSection("config.worlds").getKeys(false)) {
					//worlds.put(key, this.getConfig().getInt("config.worlds." + key + ".limit"));
				//}
			}
		} catch (Exception e) {
			this.getLogger().log(Level.SEVERE, "Invialid Configuration File.");
		}
	}
	
	public boolean isLimited(World world) {
		try {
			if (this.getConfig().getConfigurationSection("config.worlds").getKeys(false).contains(world.getName())) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}
	
	public Location getSignLocation(World world) {
		Double x = this.getConfig().getDouble("config.worlds." + world.getName() + ".sign.x");
		Double y = this.getConfig().getDouble("config.worlds." + world.getName() + ".sign.y");
		Double z = this.getConfig().getDouble("config.worlds." + world.getName() + ".sign.z");
		World w = this.getServer().getWorld(this.getConfig().getString("config.worlds." + world.getName() + ".sign.world"));
		if (x != null && y != null && z != null && w != null) {
			return new Location(w, x, y, z);
		} else {
			return null;
		}
	}
	
	public List<String> getTextTemplate(World world) {
		return this.getConfig().getStringList("config.worlds." + world.getName() + ".sign.text") != null ? this.getConfig().getStringList("config.worlds." + world.getName() + ".sign.text") : TextTemplate.textDefatulTemplate;
	}
	
	public List<World> getWorldsInConfig() {
		List<World> worlds = new ArrayList<>();
		for (String key : this.getConfig().getConfigurationSection("config.worlds").getKeys(false)) {
			if (this.getServer().getWorld(key) != null) {
				worlds.add(this.getServer().getWorld(key));
			}
		}
		return worlds;
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
