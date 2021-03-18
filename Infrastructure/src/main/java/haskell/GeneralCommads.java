package haskell;

import db.Methods;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class GeneralCommads implements CommandExecutor {

    @Deprecated
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player){
            Player p = (Player) sender;
            Location loc = p.getLocation();

            if (command.getName().equalsIgnoreCase("confirn_rent_function")) {

                //TODO
                if(loc.getChunk().getX()==0||loc.getChunk().getZ()==0){
                    p.sendMessage(Infrastructure.no_buy);
                    return false;
                }

                if(!WorldGuardMethods.isset_region_rent(loc.getChunk())) {
                        int k = getK(loc.getChunk());
                        if (Infrastructure.economy.getBalance(p) >= Infrastructure.coefficient_price * k) {
                            EconomyResponse reponse = Infrastructure.economy.withdrawPlayer(p.getName(), Infrastructure.coefficient_price * k);
                            if (reponse.transactionSuccess() && reponse.balance >= 0) {
                                if (Methods.rent_Chunk("x" + loc.getChunk().getX() + "z" + loc.getChunk().getZ(), p.getUniqueId().toString(), getK(loc.getChunk()) * 1000, true, getDeadLine())) {
                                    WorldGuardMethods.rent_chunk(((Player) sender).getLocation().getChunk(), (Player) sender);
                                    p.sendMessage(ChatColor.GREEN + "Чанк успешно арендован на координатах: " + (loc.getChunk().getX() + " " + loc.getChunk().getZ()));
                                }
                            }else {
                                p.sendMessage(ChatColor.RED + "Что-то пошло не так");
                            }
                    }
                    else {
                        p.sendMessage(Infrastructure.no_money);
                    }
                } else {
                    p.sendMessage(ChatColor.RED + "Этот чанк уже арендован!");
                }
            }


        }


        if(command.getName().equalsIgnoreCase("private_all_chunks_min")){
            if(!sender.isOp()) return false;
            WorldGuardMethods.private_all_chunks_minus();
        }

        if(command.getName().equalsIgnoreCase("private_all_chunks_plus")){
            if(!sender.isOp()) return false;
            WorldGuardMethods.private_all_chunks_plus();
        }

        if(command.getName().equalsIgnoreCase("change_mode")){
            if(WorldGuardMethods.isOwner((Player)sender)) {
                Methods.change_autorent(WorldGuardMethods.get_region_name((Player) sender));
            }
        }

        return false;
    }


    private int getK(Chunk ch){
        if(Math.abs(ch.getX()) > Math.abs(ch.getZ())){
            return 51 - Math.abs(ch.getX());
        }
        if(Math.abs(ch.getX()) < Math.abs(ch.getZ())){
            return 51 - Math.abs(ch.getZ());
        }

        if(Math.abs(ch.getX()) == Math.abs(ch.getZ())){
            return 51 - Math.abs(ch.getX());
        }
        return 0;
    }

    private int getDeadLine(){
        long realTime = System.currentTimeMillis();
        int realTime_int = (int)System.currentTimeMillis()/1000;
        return realTime_int+ Infrastructure.rental_time;//change
    }
}
