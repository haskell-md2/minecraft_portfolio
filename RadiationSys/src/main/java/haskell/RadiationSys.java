package haskell;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public final class RadiationSys extends JavaPlugin implements Listener {

    public static Logger log;
    public static HashMap<Player, Integer> players = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.log = Bukkit.getLogger();
        this.log.info(Colors.ANSI_GREEN + "Radiation System has been enable!" + Colors.ANSI_RESET);

        Bukkit.getPluginManager().registerEvents(this,this);

        //show
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {

                for(Map.Entry<Player, Integer> entry : players.entrySet()){
                   // System.out.println(entry.getKey());
                }

            }
        },0l,20l);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    //hooks
    @EventHandler
    void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        if(players.containsKey(p)) return;

        players.put(p, 0);

    }


}
