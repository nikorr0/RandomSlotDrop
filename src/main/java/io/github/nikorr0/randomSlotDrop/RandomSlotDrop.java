package io.github.nikorr0.randomSlotDrop;

import io.github.nikorr0.randomSlotDrop.listeners.PlayerDeathListener;
import io.github.nikorr0.randomSlotDrop.listeners.SpectatorDropListener;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class RandomSlotDrop extends JavaPlugin {

    private DropConfig config;
    private final List<Listener> listeners = new ArrayList<>();

    // Death timestamps
    private final Map<UUID, Long> recentDeaths = new ConcurrentHashMap<>();

    // Recording the moment of death
    public void noteDeath(UUID id) {
        recentDeaths.put(id, System.currentTimeMillis());
    }

    // Clearing the records
    public void clearDeath(UUID id) {
        recentDeaths.remove(id);
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadInternals();
        var cmd = new RSDCommand(this);

        getCommand("rsd").setExecutor(cmd);
        getCommand("rsd").setTabCompleter(cmd);

        getLogger().info(() -> "RandomSlotDrop enabled v" + getDescription().getVersion());
    }

    @Override
    public void onDisable() {
        if (!listeners.isEmpty()) {
            listeners.forEach(HandlerList::unregisterAll);
            listeners.clear();
        }

        recentDeaths.clear();
        getLogger().info(() -> "RandomSlotDrop disabled v" + getDescription().getVersion());
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        reloadInternals();
        getLogger().info("RandomSlotDrop configuration reloaded.");
    }

    private void reloadInternals() {
        this.config = new DropConfig(getConfig());

        if (!listeners.isEmpty()) {
            listeners.forEach(HandlerList::unregisterAll);
            listeners.clear();
        }

        listeners.add(new PlayerDeathListener(this, new DropManager(config)));
        listeners.add(new SpectatorDropListener(this));

        listeners.forEach(l -> Bukkit.getPluginManager().registerEvents(l, this));
    }

    public DropConfig getDropConfig() {
        return config;
    }

    public void setActive(boolean enabled) {
        getConfig().set("enabled", enabled);
        saveConfig();
        reloadConfig();
    }
}
