package onl.fdt.java.minecraft.bukkit.WorldPlayersLimit;

import java.util.List;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class PlayerWorldChangeListener implements Listener {
	private Main main = null;
	
	public PlayerWorldChangeListener(Main main) {
		this.main = main;
	}
	
	@EventHandler
	public void onPlayerChangedWorldEvent(final PlayerChangedWorldEvent event) {
		if (! (event.getPlayer().hasPermission("worldplayerslimit.admin") || event.getPlayer().isOp())) {
			if (main.isLimited(event.getPlayer().getWorld())) {
				if (main.getConfig().getInt("config.worlds." + event.getPlayer().getWorld().getName() + ".limit") <= event.getPlayer().getWorld().getPlayers().size()) {
					event.getPlayer().teleport(main.getFallbackworld().getSpawnLocation());
					main.getLogger().log(Level.INFO, "Hit map limit, teleported " + event.getPlayer().getName() + " back.");
					event.getPlayer().sendMessage("Hit map limit.");
				}
			}
		}
		for (World world : main.getWorldsInConfig()) {
			updateSign(world);
		}
		//change Players Data
	}
	
	private void updateSign(World world) {
		try {
			Location l = main.getSignLocation(world);
			List<String> s = main.getTextTemplate(world);
			if (l != null) {
				
				if (l.getBlock().getState() instanceof Sign) {
					Sign sign = (Sign) l.getBlock().getState();
					for (int i = 0; i < 4; i++) {
						try {
							String line = "";
							if (i < s.size()) {
								line = s.get(i)
										.replaceAll("%limit%", String.valueOf(main.getConfig().getInt("config.worlds." + world.getName() + ".limit")))
										.replaceAll("%current%", String.valueOf(world.getPlayers().size()))
										.replaceAll("%world%", world.getName()).trim();
								//main.getLogger().log(Level.INFO, line);
							}
							
							sign.setLine(i, line);
							sign.update();
						} catch (Exception e) {
							main.getLogger().log(Level.SEVERE, "Sign set error");
						}
					}
				} else {
					main.getLogger().log(Level.SEVERE, "not a sign " + l.getBlock().getType().name());
				}	
			} else {
				main.getLogger().log(Level.SEVERE, "location null");
			}
		} catch (Exception e) {
			main.getLogger().log(Level.WARNING, "Update fail at world " + world.getName());
		}
	}
}
