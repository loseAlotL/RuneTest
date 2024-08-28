package org.randomlima.runetest.commands;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.randomlima.runetest.utilities.Colorize;
import org.randomlima.runetest.RuneTest;

import java.util.ArrayList;
import java.util.List;

public class rune implements CommandExecutor {
    private RuneTest plugin;
    public rune(RuneTest plugin){
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) return true;
        if (args.length != 2) {
            sender.sendMessage(Colorize.format("&c[!] Usage: /getrune <rune name> <rune level>"));
            return true;
        }

        String runeName = args[0].toLowerCase();
        int runeLevel;

        // Check if the rune name exists in the config
        ConfigurationSection runeSection = plugin.getConfig().getConfigurationSection("runes." + runeName);
        if (runeSection == null) {
            sender.sendMessage(Colorize.format("&c[!] Rune not found: " + runeName));
            sender.sendMessage(Colorize.format("&c[|] Check CONFIG.YML for valid runes."));
            return true;
        }

        try {
            runeLevel = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(Colorize.format("&c[!] Rune level must be a number."));
            return true;
        }


        ItemStack runeItem = getRune(runeName, runeLevel);

        Player player = (Player) sender;
        player.getInventory().addItem(runeItem);
        player.sendMessage(Colorize.format("&2[!] Rune item added to your inventory."));
        return true;
    }

//    private ItemStack createRuneItem(String runeName, int level) {
//        ConfigurationSection runeSection = plugin.getConfig().getConfigurationSection("runes." + runeName.toLowerCase());
//        if(runeSection == null){return null;}
//
//        String display = Colorize.format(runeSection.getString("display"));
//        //boolean toggle = runeSection.getBoolean("toggle");
//        //double levelScaling = runeSection.getDouble("level-scaling");
//        List<String> description = runeSection.getStringList("description");
//
//        ItemStack runeItem = new ItemStack(Material.ENCHANTED_BOOK);
//        ItemMeta meta = runeItem.getItemMeta();
//        meta.setDisplayName(display);
//
//        List<String> lore = new ArrayList<>();
//        for (String line : description) {
//            lore.add(Colorize.format(line));
//        }
//        lore.add(Colorize.format("&5Level: &d"+level));
//        meta.setLore(lore);
//
//        PersistentDataContainer data = meta.getPersistentDataContainer();
//        NamespacedKey key = new NamespacedKey(plugin, "rune-type");
//        data.set(key, PersistentDataType.STRING, "rune");
//
//        runeItem.setItemMeta(meta);
//        return runeItem;
//    }
    public ItemStack getRune(String runeName, int level){
        ConfigurationSection runeSection = plugin.getConfig().getConfigurationSection("runes." + runeName.toLowerCase());
        if(runeSection == null){return null;}

        String display = Colorize.format(runeSection.getString("display"));
        List<String> description = runeSection.getStringList("description");

        ItemStack runeItem = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = runeItem.getItemMeta();
        meta.setDisplayName(display);

        List<String> lore = new ArrayList<>();
        for (String line : description) {
            lore.add(Colorize.format(line));
        }
        lore.add(Colorize.format("&5Level: &d"+level));
        meta.setLore(lore);

        PersistentDataContainer data = meta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "rune-type");
        data.set(key, PersistentDataType.STRING, "rune");

        runeItem.setItemMeta(meta);
        return runeItem;
    }
}
