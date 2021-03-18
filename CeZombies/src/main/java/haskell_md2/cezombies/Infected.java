package haskell_md2.cezombies;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.PlayerInventory;

import javax.swing.text.html.parser.Entity;
import java.util.HashMap;

public class Infected implements Listener {

    private HashMap<Zombie, PlayerInventory> zom_inv = new HashMap<Zombie, PlayerInventory>();

    @EventHandler
    private void Infected(PlayerDeathEvent e){

        Player p = e.getEntity();

        if (e.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent nEvent = (EntityDamageByEntityEvent) e.getEntity().getLastDamageCause();


            if ((nEvent.getDamager() instanceof Zombie)) {
                System.out.println("kill");
                Zombie zom = (Zombie) p.getLocation().getWorld().spawnEntity(p.getLocation(), EntityType.ZOMBIE);

                zom.getEquipment().setArmorContents(p.getInventory().getArmorContents());

                zom.setCustomName(p.getName());

                zom_inv.put(zom, p.getInventory());

                if(zom.isBaby()) zom.setBaby(false);
            }
        }
    }


    @EventHandler
    private void onEntityDeath(EntityDeathEvent e){
        if(e.getEntity() instanceof Zombie){

            if(zom_inv.get((Zombie) e.getEntity())!=null){
                System.out.println("Drop!");
                for (int i = 0; i < zom_inv.get(e.getEntity()).getExtraContents().length;i++) {
                    System.out.println(i);
                    e.getDrops().add(zom_inv.get(e.getEntity()).getItem(i));
                }
                zom_inv.remove((Zombie) e.getEntity());
            }
        }
    }
}
