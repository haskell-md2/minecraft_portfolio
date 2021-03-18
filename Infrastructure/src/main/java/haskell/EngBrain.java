package haskell;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class EngBrain implements Listener {

    EngBrain(Plugin main){
        Bukkit.getPluginManager().registerEvents(this,main);
    }

    @EventHandler
    private void click(PlayerInteractEvent e){
        Action a = e.getAction();
        if(a==Action.RIGHT_CLICK_BLOCK||a==Action.RIGHT_CLICK_AIR){
            //info
        }
    }

    @EventHandler
    private void AddToPlayer(PlayerJoinEvent e){
        Player p = e.getPlayer();
        put_to_inv(p);
    }


    @EventHandler
    private void noQ(PlayerDropItemEvent e){

        ItemStack brain = e.getItemDrop().getItemStack();
        if(isBrain(brain)){
            e.setCancelled(true);
        }

    }

    @EventHandler
    private void noMove(InventoryClickEvent e){
        ItemStack brain = e.getCurrentItem();
        if(brain!=null) {
            if (isBrain(brain)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    private void ResapwnPlayerrerurnItems(PlayerRespawnEvent e){
        Player p = e.getPlayer();
        put_to_inv(p);
    }


    private void put_to_inv(Player p){
        ItemStack brain = new ItemStack(Material.POPPED_CHORUS_FRUIT);
        ItemMeta meta = brain.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "Окаменевший мозг гида");
        List<String> lore= new ArrayList<String>();
        lore.add(ChatColor.GRAY+"Содержит ценную информацию");
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addEnchant(Enchantment.LUCK,10,true);

        brain.setItemMeta(meta);

        p.getInventory().setItem(8,brain);
    }


    //chekers methods
    private boolean isBrain(ItemStack item) {
        if (item.getType() == Material.POPPED_CHORUS_FRUIT) {
            if (item.getItemMeta().getDisplayName() != null) {
                if (item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "Окаменевший мозг гида")) {
                    return true;
                }
            }
        }
        return false;
    }
}
