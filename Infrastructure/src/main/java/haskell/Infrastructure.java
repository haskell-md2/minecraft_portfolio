package haskell;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import db.Methods;
import db.SQlite;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;


import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public final class Infrastructure extends JavaPlugin {

    private Logger log;
    public static Plugin plugin;

    public static  WorldGuardPlugin world_guard;
    public static Economy economy;
    public static Plugin main;

    public static HashMap<Chunk,Integer> Warring_chunks = new HashMap<>();

    //config
    public static Integer coefficient_price, rental_time, reg_price, delay_reg;
    public static String no_buy, no_money, pay_event, cancel_rent, no_your_chunk, leave_chunk;

    @Override
    public void onEnable() {

        plugin = this;

        createConfig();
        getConfig_value();
        // Plugin startup logic
        main = this;
        log = Bukkit.getLogger();
        log.info("Civilization logic enable!");

        //load economy
        if(!setupEconomy()){
            log.warning("Economy error!");
        }

        //start database
        try {
            try {
                SQlite.connect();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }catch (ClassNotFoundException e){
            log.warning(e.getMessage());
        }

        getWorldGuard();


        //commands change_mode
        getCommand("confirn_rent_function").setExecutor(new GeneralCommads());
        getCommand("private_all_chunks_min").setExecutor(new GeneralCommads());
        getCommand("private_all_chunks_plus").setExecutor(new GeneralCommads());
        getCommand("change_mode").setExecutor(new GeneralCommads());
        getCommand("regenerate_chunk").setExecutor(new Regeneration());


        //placeholder
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new SomeExpansion(this).register();
        }

        //hooks init
        Bukkit.getPluginManager().registerEvents(new GeneralHooks(),this);


        //eye
        new GlazBusnesmans(this);
        new EngBrain(this);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            public void run() {
                pay_everyone();
            }
        },100,1*20);



        //timer to leave chunk

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this,new Runnable() {
            @Override
            public void run() {
                for(Map.Entry<Chunk,Integer> entry : Warring_chunks.entrySet()){

                    if(entry.getValue() > 0){
                        entry.setValue(entry.getValue()-1);
                    }else {Warring_chunks.remove(entry.getKey());}

                    for(Player p : Bukkit.getOnlinePlayers()){
                        for(Map.Entry<Chunk,Integer> chunks : Warring_chunks.entrySet()){
                            if(p.getLocation().getChunk() == chunks.getKey()){
                                p.sendTitle(ChatColor.RED+"Регенерация чанка через: "+chunks.getValue()+"с",
                                        ChatColor.RED+"Покиньте его",
                                        10,10,10);
                            }
                        }
                    }
                }
            }
        },0l,20l);




        //WorldGuardMethods.private_all_chunks();
    }



    @Override
    public void onDisable() {
        // Plugin shutdown logic
        try { SQlite.close(); }catch (Exception e){}
    }



    private boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }

    public void getWorldGuard() {
        world_guard = (WorldGuardPlugin) this.getServer().getPluginManager().getPlugin("WorldGuard");
    }


    @Deprecated
    public void pay_everyone(){

        int real = (int)System.currentTimeMillis()/1000;
        ArrayList<String[]> owners = Methods.get_expired(real);
        for(String[] rs : owners){


            boolean auto = Boolean.valueOf(rs[3]);
            String id = rs[0];
            String playerUID = rs[1];
            if(auto){
                double balance = economy.getBalance(Bukkit.getOfflinePlayer(UUID.fromString(playerUID)));
                double price = Double.valueOf(rs[2]);

                if(balance >= price) {
                    EconomyResponse response = withdrawPl(playerUID,price,id);
//                    if (response.transactionSuccess()) {
                        Methods.sharpness_rent(id,getDeadLine());
//                    }else {
//                        System.out.println(response.errorMessage);
//                    }
                }else {
                    Methods.cancel_rent(id);
                    WorldGuardMethods.delete_owner(id);
                    if(Bukkit.getPlayer(playerUID)!=null) Bukkit.getPlayer(playerUID).sendMessage(cancel_rent+" "+id);
                }
            }else {
                if(Bukkit.getPlayer(UUID.fromString(playerUID))!=null) {
                    Bukkit.getPlayer(UUID.fromString(playerUID)).sendMessage(cancel_rent + " " + id);
                }
                Methods.cancel_rent(id);
                WorldGuardMethods.delete_owner(id);
            }

        }
    }


    private void getConfig_value(){
        FileConfiguration conf = this.getConfig();

        //int
        this.coefficient_price = conf.getInt("coefficient_price");
        this.rental_time = conf.getInt("rental_time");
        this.reg_price = conf.getInt("regen_price");
        this.delay_reg = conf.getInt("delay_regenerate");



        //mess
        this.no_buy = ChatColor.translateAlternateColorCodes('&',conf.getString("messages.no_buy"));
        this.no_money = ChatColor.translateAlternateColorCodes('&',conf.getString("messages.you_no_have_money"));
        this.pay_event = ChatColor.translateAlternateColorCodes('&',conf.getString("messages.pay_rent"));
        this.cancel_rent = ChatColor.translateAlternateColorCodes('&',conf.getString("messages.cancel_rent"));
        this.no_your_chunk =conf.getString("messages.no_your_chunk");
        this.leave_chunk = ChatColor.translateAlternateColorCodes('&',conf.getString("messages.leave_the_chunk"));

        this.leave_chunk = leave_chunk.replace("%time%", delay_reg.toString());

    }


    private int getDeadLine(){
        long realTime = System.currentTimeMillis();
        int realTime_int = (int)System.currentTimeMillis()/1000;
        return realTime_int+ rental_time;//change
    }

    private EconomyResponse withdrawPl(String uid, double price, String id){
        if(Bukkit.getPlayer(UUID.fromString(uid))!=null) {
            EconomyResponse response = economy.withdrawPlayer(Bukkit.getPlayer(UUID.fromString(uid)).getName(), price);
            Bukkit.getPlayer(UUID.fromString(uid)).sendMessage(this.pay_event+" "+(int)price +"$ за чанк "+id);
            return response;
        }else {
            EconomyResponse response = economy.withdrawPlayer(Bukkit.getOfflinePlayer(UUID.fromString(uid)).getName(), price);
            return response;
        }
    }

    private void createConfig() {
        File config = new File(getDataFolder() + File.separator + "config.yml");
        if (!config.exists()) {
            getConfig().options().copyDefaults(true);
            saveDefaultConfig();
        }
    }

}
