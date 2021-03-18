package haskell_md2.cezombies;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import sun.plugin2.main.server.Plugin;

public class Jojonight implements CommandExecutor {


    private ItemStack skull;

    public  void JojonightStart(){

        Cezombies.jojoMod = true;

        for(Player p: Bukkit.getOnlinePlayers()){
            p.sendMessage(ChatColor.DARK_PURPLE+"ゴ");
            p.sendMessage(ChatColor.DARK_PURPLE+" ゴ");
            p.sendMessage(ChatColor.DARK_PURPLE+"  ゴ");
        }

        for(Player p: Bukkit.getOnlinePlayers()){
            p.sendMessage(ChatColor.DARK_PURPLE+"Активорованна отсылка к JoJo");
            p.playSound(p.getLocation(),"p.jojo",10,1);
        }

        Bukkit.getScheduler().runTaskLater(Cezombies.main, new Runnable() {
            @Override
            public void run() {
                for(Player p: Bukkit.getOnlinePlayers()){
                    p.sendMessage(ChatColor.DARK_PURPLE+"Вам всем пизда! МУДА МУДА!");
                }
            }
        },140l);

        SetSullMeta();
        int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Cezombies.main, new Runnable() {
            @Override
            public void run() {
                for(Player p: Bukkit.getOnlinePlayers()){
                    spawnJorno(p.getLocation());
                }

            }
        },140l,20l);

        Bukkit.getScheduler().runTaskLater(Cezombies.main, new Runnable() {
            @Override
            public void run() {
                Bukkit.getScheduler().cancelTask(id);
                Cezombies.jojoMod = false;
            }
        },2000l);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender.isOp()){
            JojonightStart();
        }
        return false;
    }


    @Deprecated
    public void spawnJorno(Location loc){
        Zombie jorno = (Zombie) loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE);
        jorno.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 999999999, 6, false, false));
        jorno.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999999, 4, false, false));

        jorno.setCustomName("Джорно Джаванно");
        jorno.getEquipment().setHelmet(skull);


        ItemStack che = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta meta = (LeatherArmorMeta) che.getItemMeta();
        meta.setColor(Color.FUCHSIA);
        che.setItemMeta(meta);

        ItemStack leg = new ItemStack(Material.LEATHER_LEGGINGS);
        meta = (LeatherArmorMeta) che.getItemMeta();
        meta.setColor(Color.FUCHSIA);
        leg.setItemMeta(meta);

        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        meta = (LeatherArmorMeta) che.getItemMeta();
        meta.setColor(Color.FUCHSIA);
        boots.setItemMeta(meta);

        jorno.getEquipment().setChestplate(che);
        jorno.getEquipment().setLeggings(leg);
        jorno.getEquipment().setBoots(boots);


        //0x38BAA
    }

    private void SetSullMeta(){
        skull = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
        SkullMeta sm = (SkullMeta) skull.getItemMeta();
        sm.setOwningPlayer(Bukkit.getOfflinePlayer("50d27400-aecf-47f9-bb39-736e1adebc55\\"));
        skull.setItemMeta(sm);
    }



}
