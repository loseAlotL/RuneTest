package org.randomlima.runetest;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

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
            openCustomGUI(player, item.getItemMeta().getCustomModelData(), item.getItemMeta().getDisplayName());
            event.setCancelled(true);
        }
    }
    private void openCustomGUI(Player player, int modelData, String name) {
        // Create and open the custom GUI here
        Inventory gui = plugin.getServer().createInventory(null, 9, name+" Menu");

        // Add items to the GUI based on modelData or other conditions
        // Example: adding a single item to the GUI
        ItemStack exampleItem = new ItemStack(Material.BOOK);
        ItemMeta exampleMeta = exampleItem.getItemMeta();
        exampleMeta.setDisplayName("Example Book");
        exampleItem.setItemMeta(exampleMeta);

        gui.setItem(13, exampleItem); // Place the item in the center of the GUI

        player.openInventory(gui);
    }
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        Inventory gui = event.getInventory();
        ItemStack rune = gui.getItem(RUNE_SLOT);

        if (rune != null && rune.getItemMeta() != null) {
            ItemMeta runeMeta = rune.getItemMeta();

            if (runeMeta.hasLore()) {
                // Get the wand (assumed to be the item in the player's main hand)
                ItemStack wand = player.getInventory().getItemInMainHand();

                if (wand != null && wand.getItemMeta() != null) {
                    ItemMeta wandMeta = wand.getItemMeta();

                    // Get or create the wand's lore
                    List<String> wandLore = wandMeta.hasLore() ? wandMeta.getLore() : new ArrayList<>();
                    wandLore.addAll(runeMeta.getLore());

                    // Set the updated lore on the wand
                    wandMeta.setLore(wandLore);
                    wand.setItemMeta(wandMeta);

                    player.sendMessage(Colorize.format("&6Rune lore has been added to your staff."));
                }
            }
        }
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
        ItemMeta runeMeta = rune.getItemMeta();
        PersistentDataContainer data = runeMeta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "rune-type");
        if (data.has(key, PersistentDataType.STRING) && "rune".equals(data.get(key, PersistentDataType.STRING))) {

        }
    }
    }
}
