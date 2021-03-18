package haskell;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.domains.DefaultDomain;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
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


public class GlazBusnesmans implements Listener {


    GlazBusnesmans(Plugin main){
        Bukkit.getPluginManager().registerEvents(this,main);
    }

    @EventHandler
    private void click(PlayerInteractEvent e){
        Action a = e.getAction();
        if(a==Action.RIGHT_CLICK_BLOCK||a==Action.RIGHT_CLICK_AIR){
            Player p = e.getPlayer();
            ItemStack eye = p.getInventory().getItemInMainHand();
            if(isEyeB(eye)){
                if(WorldGuardMethods.region_isset(p.getLocation().getChunk())) {

                    if(isOwner(p)){
                        Bukkit.getServer().dispatchCommand(p, "dm open manage");
                    } else {
                        Bukkit.getServer().dispatchCommand(p, "dm open eye");
                    }

                }else {
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED+"Это дикая территория, я тут бессилен..."));
                }
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    private void AddToPlayer(PlayerJoinEvent e){
        Player p = e.getPlayer();
        put_to_inv(p);
    }


    @EventHandler
    private void noQ(PlayerDropItemEvent e){

        ItemStack eye = e.getItemDrop().getItemStack();
        if(isEyeB(eye)){
            e.setCancelled(true);
        }

    }

    @EventHandler
    private void noMove(InventoryClickEvent e){
        ItemStack eye = e.getCurrentItem();
        if(eye!=null) {
            if (isEyeB(eye)) {
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
        ItemStack eye = new ItemStack(Material.ENDER_EYE);
        ItemMeta meta = eye.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD +"Глаз бывшего бизнесмена");
        List<String> lore= new ArrayList<String>();
        lore.add(ChatColor.GRAY+"Помощник в выборе территорий");
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addEnchant(Enchantment.LUCK,10,true);

        eye.setItemMeta(meta);

        p.getInventory().setItem(7,eye);
    }


    //chekers methods
    private boolean isEyeB(ItemStack item) {
        if (item.getType() == Material.ENDER_EYE) {
            if (item.getItemMeta().getDisplayName() != null) {
                if (item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD + "Глаз бывшего бизнесмена")) {
                    return true;
                }
            }
        }
        return false;
    }


    private boolean isOwner(Player p){
        String id = "x"+p.getLocation().getChunk().getX()+"z"+p.getLocation().getChunk().getZ();
        DefaultDomain owners = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt( Bukkit.getWorld("world"))).getRegion(id).getOwners();
        if(owners.contains(p.getUniqueId())) return true;
        return false;
    }
}
