package org.randomlima.runetest;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.randomlima.runetest.utilities.Colorize;

import java.util.ArrayList;
import java.util.List;

public class WandGui implements Listener {
    private final RuneTest plugin;

    public WandGui(RuneTest plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if(isStaff(item)){
            openCustomGUI(player, player.getInventory().getItemInMainHand());
            event.setCancelled(true);
        }
    }
    public void openCustomGUI(Player player, ItemStack staff) {
        Inventory gui = Bukkit.createInventory(null, 9, Colorize.format("&6Staff Menu"));
        player.openInventory(gui);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(Colorize.format("&6Staff Menu"))) return;
        if (event.getClickedInventory() == null || event.getClickedInventory().getType() != InventoryType.CHEST) return;

        int slot = event.getSlot();
        ItemStack rune = event.getInventory().getItem(slot);

        if(!isRune(rune)){return;}
        if(event.getWhoClicked() instanceof Player){
            Player player = (Player) event.getWhoClicked();
            ItemStack staff = player.getInventory().getItemInMainHand();
            if(isStaff(staff)){
                ItemMeta staffMeta = staff.getItemMeta();
                ItemMeta runeMeta = rune.getItemMeta();
                List<String> runeLore = runeMeta != null ? runeMeta.getLore() : new ArrayList<>();
                List<String> staffLore = staffMeta != null ? staffMeta.getLore() : new ArrayList<>();
                if (staffLore == null) {
                    staffLore = new ArrayList<>();
                }
                if (runeLore != null) {
                    staffLore.addAll(runeLore);
                }
                staffMeta.setLore(staffLore);
                staff.setItemMeta(staffMeta);
            }
        }
    }
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event){
        if (!event.getView().getTitle().equals(Colorize.format("&6Staff Menu"))) return;

    }

    public boolean isStaff(ItemStack item){
        if (item != null && item.getType() == Material.STICK) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                PersistentDataContainer data = meta.getPersistentDataContainer();
                NamespacedKey key = new NamespacedKey(plugin, "wand-type");
                if (data.has(key, PersistentDataType.STRING)) {
                    return true;
                }
            }
        }
        return false;
    }
    public boolean isRune(ItemStack item){
        ItemMeta runeMeta = item.getItemMeta();
        PersistentDataContainer data = runeMeta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "rune-type");
        if (data.has(key, PersistentDataType.STRING) && "rune".equals(data.get(key, PersistentDataType.STRING))) {
            return true;
        }
        return false;
    }
}
