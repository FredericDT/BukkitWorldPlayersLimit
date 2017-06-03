package onl.fdt.java.minecraft.bukkit.WorldPlayersLimit;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("worldplayerslimit")) {
			
			if (sender instanceof Player) {
				
				if (args.length == 1) {
					
					Player p = (Player) sender;
					if (p.isOp() || p.hasPermission("worldplayerslimit.admin")) {
						
						Block block = p.getTargetBlock((HashSet<Byte>)null, 10);
						if (block != null) {
							
							if (block.getState() instanceof Sign) {
								if (this.getConfig().getConfigurationSection("config.worlds").getKeys(false).contains(args[0])) {
									this.getConfig().set("config.worlds." + args[0] + ".sign.world", block.getWorld().getName());
									this.getConfig().set("config.worlds." + args[0] + ".sign.x", block.getLocation().getX());
									this.getConfig().set("config.worlds." + args[0] + ".sign.y", block.getLocation().getY());
									this.getConfig().set("config.worlds." + args[0] + ".sign.z", block.getLocation().getZ());
									p.sendMessage("set successful");
									return true;
								}
							}
						} else {
							//this.getLogger().log(Level.INFO, "block is null");
						}
					} else {
						//this.getLogger().log(Level.INFO, "player do not have permission");
					}
				}// else {
					//this.getLogger().log(Level.INFO, "args not permitted");
					sender.sendMessage("Usage: look at a sign then perform \"/worldplayerslimit <target_world_name>\"");
				//}
			} else {
				//this.getLogger().log(Level.INFO, 
				sender.sendMessage("sender not a player");
			}
		} else {
			//this.getLogger().log(Level.INFO, "got command worldplayerslimit");
		}
		return false;
	}
	
	
	@Override
	public void onEnable() {
		Executor.initialize(this);
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
