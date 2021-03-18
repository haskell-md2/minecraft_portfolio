package airdrop;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;


public class SendDrop {

    //private ArrayList<ItemStack> items;
    private ArrayList<ItemStack> items;
    private Plugin main;
    private int taskid, id_f, main_id;
    private World world;

    @Deprecated
    SendDrop(Plugin plugin, double[] cords){

        this.main = plugin;
        world = Airdrop.world;
        items = new ArrayList<ItemStack>();

        generate_items();//генерирует предметы
        nottification_one(new Location(world,cords[0],cords[1],cords[2]),main.getConfig().getString("notification_before_drop"));
        //верное ли значение у координат
        if(cords[0] !=0 || cords[1] !=0 || cords[2] !=0){
            main_process_of_send_drop(cords[0],cords[1],cords[2]);
        }




    }

    //оповещение первое
    void nottification_one(Location loc, String mes){
        for(Player ply: Bukkit.getOnlinePlayers()){
            if(ply.getWorld()==world) {
                ply.sendMessage(ChatColor.translateAlternateColorCodes('&', mes)+" "+loc.getX()+", "+loc.getY()+", "+loc.getZ()+" через "+main.getConfig().getInt("interval_between_message_and_sending")+" секунд");
            }
        }
    }

    //оповещение второе
    void nottification_two(Location loc, String mes){
        for(Player ply: Bukkit.getOnlinePlayers()){
            if(ply.getWorld()==world) {
                ply.sendMessage(ChatColor.translateAlternateColorCodes('&', mes)+" "+loc.getX()+", "+loc.getY()+", "+loc.getZ());
            }
        }
    }


    @Deprecated
    void generate_items(){
        int begin = main.getConfig().getInt("range_of_items_in_one_drop.begin");
        int end =main.getConfig().getInt("range_of_items_in_one_drop.end");

        int ammount =(int)Math.round(begin+Math.random()*end);
        int i =1;
        int count = 0;
        do {

            int id_of_item = main.getConfig().getInt("items.i" + i + ".id");
            int ammount_of_item = main.getConfig().getInt("items.i" + i + ".ammount");
            int rarity = main.getConfig().getInt("items.i" + i + ".rarity");

            //int random_int = new Random().nextInt((rarity-1)+1)+1;
            int random_int = (int)Math.round(1+Math.random()*rarity);
            if(random_int==1) {

                items.add(new ItemStack(id_of_item, ammount_of_item));
                count++;
            }
            i++;

            if (main.getConfig().getInt("number_of_variations_of_items")<i){
                i=1;

            }
        }while (ammount!=count && ammount>count);

    }


    //спавнит сундук-железный блок
    @Deprecated
    void chest(Location loc){
        loc.getBlock().setType(Material.IRON_BLOCK);

        world.playEffect(loc, Effect.EXPLOSION,10);
        world.playEffect(loc, Effect.ANVIL_LAND,1);
    }



    //create drop
    void make_drop_in_pos(Location loc){
        for (ItemStack item: items) {
            if(item!=null) {
                loc.getWorld().dropItem(loc, item);
            }
        }
    }

    //drop_block
    void cancel(){
        Bukkit.getServer().getScheduler().cancelTask(taskid);
    }

    //firwork
    void cancel_firework(Hologram h){
        Bukkit.getServer().getScheduler().cancelTask(id_f);
        h.delete();
    }

    //main_stream
    void  cancel_main(){
        Bukkit.getServer().getScheduler().cancelTask(main_id);
    }



    //пускание фейрверков
    @Deprecated
    void Smooking(Location loc, ItemStack[] items){

        Location sl = new Location(loc.getWorld(),loc.getX()+0.5,loc.getY()+2,loc.getZ()+0.5);

        Hologram h = HologramsAPI.createHologram(main,sl);
        h.appendTextLine(ChatColor.translateAlternateColorCodes('&', Airdrop.timer_text));
        h.appendTextLine("n/a"+" секунд");


        id_f = Bukkit.getScheduler().scheduleSyncRepeatingTask(main, new Runnable() {

            ItemStack[] last_cont= null;//последнее значение инвенторя
            int count = Airdrop.time_to_open;

            public void run() {

                if(count <= 0){
                    loc.getBlock().setType(Material.AIR);
                    world.playEffect(loc,Effect.ANVIL_BREAK,1,10);
                    world.playEffect(loc,Effect.EXPLOSION,1,10);
                    make_drop_in_pos(loc);
                    cancel_firework(h);
                    return;
                }

                if(loc.getBlock().getType()!=Material.IRON_BLOCK){ //проверяет стоит ли блок на месте
//                    make_drop_in_pos(loc);
//                    cancel_firework(h);
                    chest(loc);
                    return;
                }

                //феерверки
                Firework f = world.spawn(sl, Firework.class);
                FireworkMeta fm = f.getFireworkMeta();
                fm.addEffect(FireworkEffect.builder()
                        .flicker(false)
                        .trail(true)
                        .with(FireworkEffect.Type.BALL)
                        .withColor(Color.RED)
                        .withFade(Color.BLUE)
                        .build());
                fm.setPower(3);
                f.setFireworkMeta(fm);

                h.removeLine(1);
                h.appendTextLine(Integer.toString(count)+" секунд");
                count--;

            }
        },0L,20L);
    }


    @Deprecated
    void drop_block(Double x, Double y,Double z){
        Location loc = new Location( world , x,y,z);

            FallingBlock fb = loc.getWorld().spawnFallingBlock(new Location(world,x,100.0,z),Material.IRON_BLOCK,(byte)1);//падает блок железа
            fb.setDropItem(false);
            taskid = Bukkit.getScheduler().scheduleSyncRepeatingTask(main, new Runnable(){

                double last_loc = 0;
                int count_to_cancel = 0;
                @Override

                public void run() {

                   if(fb.getLocation().getY()==y ||fb.getLocation().getY()==last_loc || count_to_cancel>120/*!fb.getType().getName().equalsIgnoreCase("FALLING_BLOCK")*/){//когда блок железа упал
                        nottification_two(loc, Airdrop.not);
                        chest(loc);//на месте падения создаём блок железа

                        ItemStack[] stacks = new ItemStack[items.size()];
                        stacks = items.toArray(stacks);
                        Smooking(loc,stacks);
                        Airdrop.active_iron_block.add(loc);

                        cancel();
                   }
                    count_to_cancel++;
                   last_loc = fb.getLocation().getY();
                }
            },0L,5L);

    }

    void main_process_of_send_drop(double x, double y, double z){
        main_id = Bukkit.getScheduler().scheduleSyncRepeatingTask(main, new Runnable(){
            int count_to_cancel = 0;
            @Override
            public void run() {
                if(count_to_cancel>main.getConfig().getInt("interval_between_message_and_sending")){
                    drop_block(x,y,z);
                    cancel_main();
                    return;
                }
                count_to_cancel++;
            }
        },0L, 20L);
    }

}

