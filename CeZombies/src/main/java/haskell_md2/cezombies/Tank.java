package haskell_md2.cezombies;

import org.bukkit.Material;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Tank implements Listener {

    public static void CreatTank(Entity ent) {
        Attributable bossAttributable = (Attributable) ent;
        AttributeInstance ai = bossAttributable.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        ai.setBaseValue(70.0);

        ((Zombie) ent).setHealth(70);

        ((Zombie) ent).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 999999999, 1, false, false));


        ent.setCustomName("Тяжеловес");

        ItemStack armor[] = new ItemStack[4];

        armor[0] = new ItemStack(Material.CHAINMAIL_HELMET);
        armor[1] = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
        armor[2] = new ItemStack(Material.CHAINMAIL_LEGGINGS);
        armor[3] = new ItemStack(Material.CHAINMAIL_BOOTS);

        ((Zombie) ent).getEquipment().setChestplate(armor[1]);
        ((Zombie) ent).getEquipment().setLeggings(armor[2]);
        ((Zombie) ent).getEquipment().setBoots(armor[3]);
    }

    @EventHandler
    private void DamageZombie(EntityDamageByEntityEvent e){
        if(e.getDamager() instanceof Zombie){
            if(e.getDamager().getCustomName().equalsIgnoreCase("Тяжеловес")){
                e.setDamage(10);
            }
        }

    }


}
