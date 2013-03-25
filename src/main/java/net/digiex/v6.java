package net.digiex;

import java.util.logging.Logger;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class v6 extends JavaPlugin {
	
	private Logger log;
	private Listener v6listener = new v6listener();
	private String permission = "v6.features";
	private Permission provider;

    @Override
    public void onDisable() {
    	log = getLogger();
        log.info("v6 disabled");
    }

    @Override
    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(v6listener, this);
        getVault();
        PluginDescriptionFile pdfFile = this.getDescription();
        getLogger().info( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
    }
    
    public void addPermission(Player player) {
    	getVault().playerAdd(player, permission);
    }
    
    public void removePermission(Player player) {
    	getVault().playerRemove(player, permission);
    }
    
    private Permission getVault() {
    	if (provider != null) {
    		return provider;
    	}
		Plugin plugin = getServer().getPluginManager().getPlugin("Vault");
		if (plugin == null || !(plugin instanceof net.milkbowl.vault.Vault)) {
			getServer().getPluginManager().disablePlugin(this);
			return null;
		}
		RegisteredServiceProvider<Permission> rsp = Bukkit.getServer()
				.getServicesManager().getRegistration(Permission.class);
		if (rsp == null) {
			getServer().getPluginManager().disablePlugin(this);
			return null;
		}
		return provider = rsp.getProvider();
	}
    
    private class v6listener implements Listener {
    	
    	@EventHandler
    	public void onPlayerJoin(PlayerJoinEvent event) {
    		Player player = event.getPlayer();
    		if (player.getAddress().getAddress().getHostAddress().contains(":")) {
    			addPermission(player);
    			getServer().dispatchCommand(getServer().getConsoleSender(), "broadcast " + player.getName() + " has IPv6!");
    		} else {
    			removePermission(player);
    		}
    	}
    }
    
    
}
