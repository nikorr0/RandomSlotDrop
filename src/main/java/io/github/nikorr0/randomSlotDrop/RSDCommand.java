package io.github.nikorr0.randomSlotDrop;

import org.bukkit.command.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class RSDCommand implements CommandExecutor, TabCompleter {

    private final RandomSlotDrop plugin;
    private static final List<String> ROOT = List.of("enable", "disable",
                                                        "reload", "status", "set");
    private static final List<String> SETTER = List.of("slots", "min", "max", "spectatorDrop");
    private static final List<String> BOOL_TAB = List.of("true", "false");

    public RSDCommand(RandomSlotDrop plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command cmd,
                             @NotNull String label,
                             @NotNull String[] args) {

        if (args.length == 0) {
            usage(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "enable" -> {
                toggle(sender, true);
            }
            case "disable" -> {
                toggle(sender, false);
            }
            case "reload" -> {
                plugin.reloadConfig();
                sender.sendMessage("§eRandomSlotDrop configuration reloaded.");
            }
            case "status" -> {
                boolean st = plugin.getDropConfig().enabled();
                sender.sendMessage("§7Status: " + (st ? "§aENABLED" : "§cDISABLED"));
            }
            case "set" -> handleSet(sender, args);
            default -> usage(sender);
        }
        return true;
    }

    // Tab completion
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender,
                                      @NotNull Command cmd,
                                      @NotNull String alias,
                                      @NotNull String[] args) {
        return switch (args.length) {
            case 1  -> prefix(ROOT,  args[0]);
            case 2  -> "set".equalsIgnoreCase(args[0]) ? prefix(SETTER, args[1]) : List.of();
            case 3  -> {
                if (!"set".equalsIgnoreCase(args[0])) { yield List.of(); }
                switch (args[1].toLowerCase()) {
                    case "spectatordrop" -> {
                        yield prefix(BOOL_TAB, args[2]);
                    }
                    default -> {
                        yield List.of();
                    }
                }
            }
            default -> List.of();
        };
    }

    private void toggle(CommandSender snd, boolean enabled) {
        plugin.setActive(enabled);
        snd.sendMessage(enabled ? "§aRandomSlotDrop enabled."
                : "§cRandomSlotDrop disabled.");
    }

    private void handleSet(CommandSender snd, String[] args) {
        if (args.length != 3) { usageSet(snd); return; }

        String key   = args[1].toLowerCase();
        String value = args[2];

        switch (key) {
            case "slots", "min", "max" -> {
                double num;
                try { num = Double.parseDouble(value); }
                catch (NumberFormatException ex) { snd.sendMessage("§cNot a number: " + value); return; }

                switch (key) {
                    case "slots" -> plugin.getConfig().set("dropSlotsPercent", num);
                    case "min" -> plugin.getConfig().set("stackDropPercentMin", num);
                    case "max" -> plugin.getConfig().set("stackDropPercentMax", num);
                }
                plugin.saveConfig();
                plugin.reloadConfig();
                snd.sendMessage("§aSaved and reloaded. " + key + " = " + num);
            }

            case "spectatordrop" -> {
                Boolean flag;
                switch (value.toLowerCase()) {
                    case "true", "yes", "on" -> flag = true;
                    case "false", "no", "off" -> flag = false;
                    default -> {
                        snd.sendMessage("§cValue must be true|false.");
                        return;
                    }
                }

                plugin.getConfig().set("spectatorFullDrop", flag);
                plugin.saveConfig();
                plugin.reloadConfig();
                snd.sendMessage("§aSaved and reloaded. spectatorDrop = " + flag);
            }

            default -> usageSet(snd);
        }
    }

    private static void usage(CommandSender s) {
        s.sendMessage("§7Usage: /rsd <enable|disable|reload|status|set>");
        usageSet(s);
    }
    private static void usageSet(CommandSender s) { s.sendMessage("§7Usage: /rsd set <slots|min|max|spectatorDrop> <value>"); }

    private static List<String> prefix(List<String> base, String token) {
        return base.stream().filter(e -> e.startsWith(token.toLowerCase())).toList();
    }
}
