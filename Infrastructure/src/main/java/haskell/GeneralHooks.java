package haskell;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.type.Bed;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;


public class GeneralHooks implements Listener {

    @EventHandler
    private void Death_and_teleport(PlayerRespawnEvent e){
        Player p = e.getPlayer();
        e.setRespawnLocation(new Location(Bukkit.getWorld("world"),0,70,0));
    }

//    @EventHandler
//    private void Blcok_bed(PlayerInteractEvent e){
//        Action a = e.getAction();
//        if(a==Action.RIGHT_CLICK_BLOCK){
//            if(e.getClickedBlock().getType() instanceof Bed) {
//                e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.WHITE+"Похоже, у вас бессоница... опять..."));
//                e.setCancelled(true);
//            }
//        }
//    }

    @EventHandler
    private void noDropItemFromDeathPlayer(PlayerDeathEvent e){
        

        ItemStack eye = null;
        ItemStack brain = null;
        for(ItemStack i : e.getDrops()){
            if(i.getItemMeta().getDisplayName()!=null) {

                if (i.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "Окаменевший мозг гида")) {
                    brain = i;
                }
                if (i.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD + "Глаз бывшего бизнесмена")) {
                    eye = i;
                }

            }
        }

        e.getDrops().remove(brain);
        e.getDrops().remove(eye);

    }




}
