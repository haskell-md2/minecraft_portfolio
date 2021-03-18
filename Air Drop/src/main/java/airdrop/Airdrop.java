package airdrop;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONArray;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Logger;

public final class Airdrop extends JavaPlugin {

    private Logger log;
    public static int x_b,z_b, x_end, z_end, time_to_open;
    public ArrayList<LinkedHashMap> cords_of_area;
    public static ArrayList<Location> active_iron_block;
    public static String not, timer_text;
    public static World world;
    public static Plugin main;

    void CreateConfig(){
        File config = new File(getDataFolder()+File.separator+"config.yml");
        if(!config.exists()) {
            log.info("Config is created . . . .");
            getConfig().options().copyDefaults(true);
            saveDefaultConfig();
        }
    }

    void GetConfig(){

        cords_of_area = (ArrayList<LinkedHashMap>) this.getConfig().getList("areas");
        time_to_open = this.getConfig().getInt("time_to_open");

        not = this.getConfig().getString("notification");
        timer_text = this.getConfig().getString("timer_text");


    }


    @Deprecated
    @Override
    public void onEnable() {

        log  = Logger.getLogger("Minecraft");//логгер

        main = this;//данный класс

        active_iron_block = new ArrayList<Location>();//инициализация листа

        //конфиг
        CreateConfig();
        GetConfig();

        world = Bukkit.getWorld(this.getConfig().getString("world_name"));

        log.info("Airdrop initialization completed successfully");
        Bukkit.getPluginManager().registerEvents(new Hook(), this);
        getCommand("airdrop").setExecutor(new commands());

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                SendDrop sd = new SendDrop(main,new utilits().generate_cords());
            }
        },0L,(main.getConfig().getInt("interval_between_message_and_sending")+main.getConfig().getInt("time_to_open")+main.getConfig().getInt("interval_betwem_airdrops_opperations"))*20);


    }

    @Override
    public void onDisable() {

    }
}
