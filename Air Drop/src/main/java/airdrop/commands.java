package airdrop;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class commands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender.isOp() || sender instanceof ConsoleCommandSender){
            if(command.getName().equalsIgnoreCase("airdrop")){

                    if(args[0]!=null){
                        Player p = Bukkit.getPlayer(args[0]);
                        new SendDrop(Airdrop.main,new utilits().make_cords_by_location(p.getLocation()));
                    }

            }
        }
        return true;
    }
}
