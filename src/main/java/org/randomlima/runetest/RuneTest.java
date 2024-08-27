package org.randomlima.runetest;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.randomlima.runetest.commands.rune;
import org.randomlima.runetest.commands.staff;

import java.util.ArrayList;
import java.util.List;

public final class RuneTest extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        this.getCommand("getrune").setExecutor(new rune(this));
        this.getCommand("getstaff").setExecutor(new staff(this));
        getServer().getPluginManager().registerEvents(new WandGui(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
