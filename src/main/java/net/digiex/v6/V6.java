package net.digiex.v6;

import java.util.logging.Logger;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class V6 extends JavaPlugin {

    private Logger log;
    private Permission provider;
    private String permission = "v6.features";

    @Override
    public void onDisable() {
        log.info("v6 disabled.");
    }

    @Override
    public void onEnable() {
        log = getLogger();
        if (getVault() == null) {
            getServer().getPluginManager().disablePlugin(this);
        }
        getServer().getPluginManager().registerEvents(new v6listener(), this);
        log.info("is now enabled!");
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
