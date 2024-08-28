package org.randomlima.runetest.commands;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.randomlima.runetest.utilities.Colorize;
import org.randomlima.runetest.RuneTest;

public class staff implements CommandExecutor {
    private final RuneTest plugin;

    public staff(RuneTest plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length != 2) {
            sender.sendMessage(Colorize.format("&c[!] Usage: /getwand <name> <modelData>"));
            return true;
        }

        Player player = (Player) sender;

        // Replace underscores with spaces in the name
        String itemName = args[0].replace("_", " ");
        int customModelData;

        try {
            customModelData = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(Colorize.format("&c[!] Custom model data must be a number."));
            return true;
        }

        // Create the wand item
        ItemStack wandItem = createWandItem(itemName, customModelData);

        // Give the wand item to the player
        player.getInventory().addItem(wandItem);
        player.sendMessage(Colorize.format("&2[!] Wand item added to your inventory: " + itemName));
        return true;
    }

    private ItemStack createWandItem(String name, int customModelData) {
        ItemStack wand = new ItemStack(Material.STICK);
        ItemMeta meta = wand.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(Colorize.format("&6" + name));
            meta.setCustomModelData(customModelData);

            // Add persistent data using the "wand-type" key
            PersistentDataContainer data = meta.getPersistentDataContainer();
            NamespacedKey key = new NamespacedKey(plugin, "wand-type");
            data.set(key, PersistentDataType.STRING, "staff");

            wand.setItemMeta(meta);
        }

        return wand;
    }
}
