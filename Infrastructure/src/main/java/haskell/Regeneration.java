package haskell;


import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.block.BaseBlock;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.*;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.regex.Pattern;

import static haskell.Infrastructure.*;

public class Regeneration implements CommandExecutor {

    @Override
    @Deprecated
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player p = (Player)sender;
        Location loc = p.getLocation();

        boolean isOwner = WorldGuardMethods.isOwner(p);

        if(!isOwner) return true;

        if(Infrastructure.economy.getBalance(p)<reg_price){
            p.sendMessage(no_money);
            return true;
        }

        EconomyResponse reponse = Infrastructure.economy.withdrawPlayer(p, reg_price);
        if(!reponse.transactionSuccess()) return true;

        final Chunk chunk = p.getLocation().getChunk();

        p.sendMessage(leave_chunk);

        Warring_chunks.put(chunk,delay_reg);

        Bukkit.getScheduler().runTaskLater(Infrastructure.plugin, new Runnable() {
            @Override
            public void run() {

                //Location light_loc = new Location(p.getWorld(),chunk.getX()*16, 30,chunk.getZ()*16);

                for(Entity ent : chunk.getEntities()){
                    if(ent instanceof Player){

                        Player pl = (Player) ent;
                        pl.getWorld().strikeLightning(pl.getLocation());
                        pl.setHealth(0);

                    }

                }
                System.out.print("Regenerationx1");
                regen(chunk);
//                Bukkit.getWorld("world").regenerateChunk(chunk.getX(),chunk.getZ());
            }

        },20 * delay_reg);


        return true;
    }


    public static void regen(Chunk ch) {

        Bukkit.getLogger().info("Regeneration");

        int bx = ch.getX() << 4;
        int bz = ch.getZ() << 4;


        BlockVector3 min = BlockVector3.at(bx, 0, bz);
        BlockVector3 max = BlockVector3.at(bx + 15, 256, bz + 15);

        System.out.println(bx+" "+" "+bz+" "+" "+bx+15+" "+" "+bz+15);//dell

        CuboidRegion region = new CuboidRegion(new BukkitWorld(ch.getWorld()), min, max);

        EditSession es = WorldEdit.getInstance().newEditSession(region.getWorld());
        System.out.println(region.getWorld().getName());
        //es.getWorld().regenerate(region,es);
        Pattern pattern = new SingleBlockPattern(new BaseBlock(BlockID.AIR));
        es.setBlocks(region, new Pattern());



    }


    public static void createBeacon(Location loc) {
        int x = loc.getBlockX();
        int y = loc.getBlockY() - 30;
        int z = loc.getBlockZ();

        World world = loc.getWorld();

        world.getBlockAt(x, y, z).setType(Material.BEACON);
        for (int i = 0; i <= 29; ++i) world.getBlockAt(x, (y + 1) + i, z).setType(Material.GLASS);
        for (int xPoint = x-1; xPoint <= x+1 ; xPoint++) {
            for (int zPoint = z-1 ; zPoint <= z+1; zPoint++) {
                world.getBlockAt(xPoint, y-1, zPoint).setType(Material.IRON_BLOCK);
            }
        }
    }


}
