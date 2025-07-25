package io.github.nikorr0.randomSlotDrop.listeners;

import io.github.nikorr0.randomSlotDrop.RandomSlotDrop;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.jetbrains.annotations.NotNull;

/**
 Drops the ENTIRE contents of the inventory if
 the player in Spectator mode after death.
 */
public final class SpectatorDropListener implements Listener {
    private final RandomSlotDrop plugin;

    public SpectatorDropListener(@NotNull RandomSlotDrop plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {

        if (!plugin.getDropConfig().enabled()) return;

        if (!plugin.getDropConfig().spectatorFullDrop()) return;

        Player p = e.getEntity();
        Location loc = p.getLocation();
        World w = p.getWorld();

        // Adding delay because some mods/plugins setting player's
        // gamemode to spectator after a small delay
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (p.getGameMode() != GameMode.SPECTATOR) return;

            for (var item : p.getInventory().getContents()) {
                if (item == null || item.getType() == Material.AIR) continue;
                w.dropItemNaturally(loc, item.clone());
            }
            p.getInventory().clear();

            // clearing record
            plugin.clearDeath(p.getUniqueId());

        }, 10L);
    }
}