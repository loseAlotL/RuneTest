package org.randomlima.runetest;

import org.bukkit.plugin.java.JavaPlugin;
import org.randomlima.runetest.commands.rune;
import org.randomlima.runetest.commands.staff;

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
