package io.github.nikorr0.randomSlotDrop;

import org.bukkit.configuration.ConfigurationSection;

import org.jetbrains.annotations.NotNull;

public final class DropConfig {

    private final boolean enabled;
    private final boolean spectatorFullDrop;
    private final double slotPercent;
    private final double stackMin;
    private final double stackMax;

    public DropConfig(@NotNull ConfigurationSection section) {
        this.enabled = section.getBoolean("enabled", true);
        this.spectatorFullDrop = section.getBoolean("spectatorFullDrop", true);

        double x = section.getDouble("dropSlotsPercent", 25.0);
        double y1 = section.getDouble("stackDropPercentMin", 20.0);
        double y2 = section.getDouble("stackDropPercentMax", 80.0);

        // Normalization
        x = clamp(x, 0, 100);
        y1 = clamp(y1, 0, 100);
        y2 = clamp(y2, 0, 100);
        if (y1 > y2) { double t = y1; y1 = y2; y2 = t; }

        this.slotPercent = x;
        this.stackMin = y1;
        this.stackMax = y2;
    }

    public boolean enabled() { return enabled; }
    public boolean spectatorFullDrop() { return spectatorFullDrop;}
    public double slotPercent() { return slotPercent; }
    public double stackMin() { return stackMin; }
    public double stackMax() { return stackMax; }

    private static double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }
}