package onl.fdt.java.minecraft.bukkit.WorldPlayersLimit;

import java.util.logging.Level;

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
			if (Main.worlds.containsKey(event.getPlayer().getWorld().getName())) {
				if (Main.worlds.get(event.getPlayer().getWorld().getName()) <= event.getPlayer().getWorld().getPlayers().size()) {
					event.getPlayer().teleport(main.getFallbackworld().getSpawnLocation());
					main.getLogger().log(Level.INFO, "Hit map limit, teleported " + event.getPlayer().getName() + " back.");
					event.getPlayer().sendMessage("Hit map limit.");
				}
			}
		}
	}
}
