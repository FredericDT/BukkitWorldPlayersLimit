package onl.fdt.java.minecraft.bukkit.WorldPlayersLimit;

public class Executor {
	private static Main main;
	public static void execute(Runnable runnable) {
		if (main != null) {
			main.getServer().getScheduler().runTaskAsynchronously(main, runnable);
		}
	}
	public static void initialize(Main main) {
		Executor.main = main;
		//main.getLogger().log(Level.INFO, "executor initialized");
	}
}
