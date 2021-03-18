package haskell_md2.cezombies;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;


import java.util.logging.Logger;

public final class Cezombies extends JavaPlugin implements Listener {

    private Logger log;

    private Boolean night;

    public static Boolean jojoMod;

    public static Plugin main;



    @Override
    public void onEnable() {

        main =this;

        // Plugin startup logic
        log = Bukkit.getLogger();
        night = true;
        log.info("CeZombies Active!");

        Bukkit.getPluginManager().registerEvents(this,this);
        Bukkit.getPluginManager().registerEvents(new Tank(),this);
        Bukkit.getPluginManager().registerEvents(new Infected(),this);
        Bukkit.getPluginManager().registerEvents(new Antideath(), this);

        this.getCommand("referencetojojo").setExecutor(new Jojonight());


        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
            int count_to_cancel = 0;
            @Override
            public void run() {
                for(Entity zom : Bukkit.getServer().getWorld("world").getEntities()){
                    if(zom instanceof Zombie) {
                            int rb = rnd(1, 10);

                            if (zom.getCustomName() != null) {
                                if (rb == 1 || zom.getCustomName().equalsIgnoreCase("Тяжеловес")) {
                                    BreakBlock(zom);
                                    // System.out.println("TANK!");//dell
                                }

                            }
                        }
                    }
                }
        },0L, 20L);

        //отсёт до начала ночи
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
            @Override
            public void run() {
                //PlaySound();
               // PlaySound();
                long time = (13000 - Bukkit.getWorld("World").getTime())/20;
                if(time>0) {
                    for (Player p : Bukkit.getOnlinePlayers()) {

                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GREEN + "До захода солнца осталось " + time+" секунд"));
                    }
                    if(night) night=false;
                }else {

                    if(!night) PlaySound();
                    if(!night) night=true;

                    for (Player p : Bukkit.getOnlinePlayers()) {

                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + "До рассвета солнца осталось " + (11000/20 + time)+" секунд"));
                    }
                }
            }
        },0L, 20L);



}




    //events//////////////////////////////////
    @EventHandler
    private void SpawnZom(CreatureSpawnEvent e){

            Entity ent = e.getEntity();

            if (ent instanceof Zombie) {

                int gen_z = rnd(1, 100);
                if (gen_z <= 15) {
                    ((Zombie) ent).addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 999999999, 6, false, false));
                    ent.setCustomName("Прыгун");

                    if (((Zombie) ent).isBaby()) ((Zombie) ent).setBaby(false);

                } else if (gen_z > 15 && 30 >= gen_z) {
                    ((Zombie) ent).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999999, 4, false, false));
                    ent.setCustomName("Раннер");

                    if (((Zombie) ent).isBaby()) ((Zombie) ent).setBaby(false);


                } else if (gen_z >= 31 && gen_z <= 33) {
                    if (((Zombie) ent).isBaby()) ((Zombie) ent).setBaby(false);
                    Tank.CreatTank(ent);
                    PlaySecret();
                } else {
                    ent.setCustomName("Обычный");
                }

            }
    }


    //делаем так, чтобы ебучие зомбаки не ебались с огнём
    @EventHandler
    public void onBurn(EntityCombustEvent e)
    {
        if(e.getEntity().getType()==EntityType.ZOMBIE)
        {
            e.setCancelled(true);
        }

    }


    //blockspawn
    @EventHandler
    public void SpawnMob(EntitySpawnEvent e){
        long time = Bukkit.getWorld("World").getTime();

        if ((e.getEntity() instanceof Skeleton) || (e.getEntity() instanceof Enderman) || (e.getEntity() instanceof Spider)) e.setCancelled(true);

    }

    ////////////////////////////////////////////////////////////////////////////////////////////




    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void BreakBlock(Entity zom){
        Location eyelocation = ((Zombie) zom).getEyeLocation();
        Vector vec = zom.getLocation().getDirection();

        Location loc = eyelocation.add(vec);
        int r = rnd(0, 1);
        if (r == 0) {
            loc.add(0, -1, 0);
        }

        if (loc.getBlock().getType() != Material.AIR && loc.getBlock().getType() != Material.MOB_SPAWNER) {
            loc.getBlock().setType(Material.AIR);
            loc.getWorld().playEffect(loc, Effect.ZOMBIE_DESTROY_DOOR,1,10);

        }
    }

    private void PlaySound(){
        for(Player p : Bukkit.getOnlinePlayers()){
            p.playSound(p.getLocation(),"p.night",10,1);
        }
    }

    private void PlaySecret(){

        for(Player p : Bukkit.getOnlinePlayers()){
            p.playSound(p.getLocation(),"p.secret",100000,1);
            p.sendMessage("Тяжеловес пришёл!");
        }
    }


    //util
    public static int rnd(int min, int max)
    {
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }


}
