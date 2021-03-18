package haskell_md2.cezombies;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class Antideath implements Listener {

    @EventHandler
    void onDeath(PlayerDeathEvent e){
        Player p = e.getEntity();
        p.setHealth(20);
        p.teleport(p.getBedSpawnLocation());
    }
}
