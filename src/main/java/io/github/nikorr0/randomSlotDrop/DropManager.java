package io.github.nikorr0.randomSlotDrop;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class DropManager {

    private final DropConfig config;

    public DropManager(@NotNull DropConfig config) {
        this.config = config;
    }

    /**
      Performs a partial drop of the player's
      inventory according to the rules specified in the config.
     */
    public void handleDeath(@NotNull Player player) {
        PlayerInventory inv = player.getInventory();
        ItemStack[] contents = inv.getContents();     // includes armor slots + off-hand
        int totalSlots  = contents.length;
        int slotsToDrop = (int) Math.round(totalSlots * config.slotPercent() / 100.0);

        // Shuffling indexes
        List<Integer> shuffled = IntStream.range(0, totalSlots)
                .boxed()
                .collect(Collectors.toList());
        Collections.shuffle(shuffled, ThreadLocalRandom.current());

        World world = player.getWorld();

        for (int i = 0; i < slotsToDrop; i++) {
            int slot = shuffled.get(i);
            ItemStack stack = contents[slot];
            if (stack == null || stack.getType() == Material.AIR) continue;

            int amount = stack.getAmount();
            if (amount <= 0) continue;

            int toDrop = computeDropAmount(amount);

            // Coping item
            ItemStack drop = stack.clone();
            drop.setAmount(toDrop);
            world.dropItemNaturally(player.getLocation(), drop);

            // Updating slot
            int remain = amount - toDrop;
            if (remain <= 0) {
                inv.setItem(slot, null);
            } else {
                stack.setAmount(remain);
            }
        }
    }

    private int computeDropAmount(int amount) {
        if (amount <= 1) return 1;
        double pct = config.stackMin() +
                ThreadLocalRandom.current().nextDouble() * (config.stackMax() - config.stackMin());
        int num = (int) Math.round(amount * pct / 100.0);
        return Math.max(1, Math.min(num, amount));
    }
}