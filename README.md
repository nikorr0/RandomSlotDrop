# RandomSlotDrop
**RandomSlotDrop** is a plugin for the Paper/Purpur/Spigot/Bukkit servers (Minecraft Java Edition 1.17 - 1.21.*).  

The plugin allows players to keep a portion of their inventory upon death. It introduces a configurable chance for dropping only a certain percent of random inventory slots, giving players a better experience by reducing frustration from losing everything after death.

![Plugin showcase](https://github.com/nikorr0/RandomSlotDrop/blob/main/screenshots/RandomSlotDrop_plugin_showcase.webp)

---

## Functional overview

* **Configurable slot drops**<br>
Only a specific percentage of inventory slots are randomly selected to drop upon player death.
For example, suppose the `dropSlotsPercent` is set to 10%. Since players have a total of 41 inventory slots (36 general slots + 4 armor slots + off-hand slot), the plugin calculates 10% of 41, resulting in approximately 4 random slots chosen to drop items. If a player has items in only 10 of slots, then up to 4 slots might drop items or none at all if the randomly selected slots are empty.

* **Partial stack drops**<br>
For each selected slot containing more than one item, the plugin will randomly choose a partial stack size to drop, within a configured range (from `stackDropPercentMin`% to `stackDropPercentMax`% of the stack). If you set both the min and max to 100, the entire stack will always drop.

* **Drop full inventory on spectator gamemode**<br>
Optionally, if a player dies and is switched to spectator mode shortly afterward, their entire inventory will automatically drop (configured via `spectatorFullDrop`).

---

## Plugin commands

| Command | Description |
|---------|-------------|
| `/rsd enable/disable` | Enable or disable the RandomSlotDrop plugin. |
| `/rsd reload` | Reload `config.yml` to apply configuration changes.|
| `/rsd status` | Show current plugin status (enabled/disabled). |
| `/rsd set slots <0-100>` | Set percentage of inventory slots to drop. |
| `/rsd set min <0-100>` | Set minimum stack drop percentage. |
| `/rsd set max <0-100>` | Set maximum stack drop percentage. |
| `/rsd set spectatorDrop <true/false>` | Enable/disable full inventory drop on spectator gamemode. |

---

## Configuration (`plugins/RandomSlotDrop/config.yml`)

```yml
# Enable or disable the plugin
enabled: true

# If set to true, the player’s full inventory will drop when they die and their gamemode is Spectator.
spectatorFullDrop: true

# Percentage of inventory slots to randomly select for partial drop on death.
dropSlotsPercent: 25.0

# Minimum percentage of items in a selected slot to drop when its stack size > 1.
stackDropPercentMin: 20.0

# Maximum percentage of items in a selected slot to drop when its stack size > 1.
stackDropPercentMax: 80.0
```
