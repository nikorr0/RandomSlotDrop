package io.github.nikorr0.randomSlotDrop.listeners;

import io.github.nikorr0.randomSlotDrop.DropManager;
import io.github.nikorr0.randomSlotDrop.RandomSlotDrop;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public final class PlayerDeathListener implements Listener {

    private final RandomSlotDrop plugin;
    private final DropManager manager;

    public PlayerDeathListener(@NotNull RandomSlotDrop plugin,
                               @NotNull DropManager manager) {
        this.plugin  = plugin;
        this.manager = manager;
    }

    public void register(@NotNull Object plugin) {
        Bukkit.getPluginManager().registerEvents(this, (Plugin) plugin);
    }

    public static void unregister(@NotNull Object plugin, @NotNull Listener listener) {
        PlayerDeathEvent.getHandlerList().unregister(listener);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        plugin.noteDeath(player.getUniqueId());

        if (!plugin.getDropConfig().enabled()) return;

        // Removing the drop and saving player inventory
        e.getDrops().clear();
        e.setKeepInventory(true);

        manager.handleDeath(player);
    }
}
