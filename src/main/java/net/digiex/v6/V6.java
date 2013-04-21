package net.digiex.v6;

import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class V6 extends JavaPlugin {

	private Logger log;
	private String permission = "v6.features";

	@Override
	public void onDisable() {
		log.info("is now disabled.");
	}

	@Override
	public void onEnable() {
		log = getLogger();
		getServer().getPluginManager().registerEvents(new v6listener(), this);
		log.info("is now enabled!");
	}

	public void addPermission(Player player) {
		getServer().dispatchCommand(getServer().getConsoleSender(),
				"pex user " + player.getName() + " add " + permission);
		getServer().dispatchCommand(getServer().getConsoleSender(),
				"broadcast " + player.getName() + " has IPv6!");
	}

	public void removePermission(Player player) {
		getServer().dispatchCommand(getServer().getConsoleSender(),
				"pex user " + player.getName() + " remove " + permission);
	}

	private class v6listener implements Listener {

		@EventHandler
		public void onPlayerJoin(PlayerJoinEvent event) {
			Player player = event.getPlayer();
			if (!player.hasPermission(permission)
					&& player.getAddress().getAddress().getHostAddress()
							.contains(":")) {
				addPermission(player);
			} else if (player.hasPermission(permission)
					&& !player.getAddress().getAddress().getHostAddress()
							.contains(":")) {
				removePermission(player);
			}
		}
	}
}