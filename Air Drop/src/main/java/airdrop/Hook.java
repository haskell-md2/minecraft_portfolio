package airdrop;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class Hook implements Listener {
    @EventHandler
    void BreakBlock(BlockBreakEvent e){
        Block b = e.getBlock();
        if(b.getType()== Material.IRON_BLOCK){
            if(Airdrop.active_iron_block.contains(b.getLocation())) {
                e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', Airdrop.main.getConfig().getString("the_block_cannot_be_broken_mes")));
                e.setCancelled(true);
            }
        }

    }
}
