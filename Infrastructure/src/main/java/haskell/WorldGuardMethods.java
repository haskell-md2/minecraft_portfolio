package haskell;

import com.sk89q.worldedit.world.World;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.domains.DefaultDomain;

import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import static haskell.Infrastructure.no_your_chunk;

public class WorldGuardMethods {


    public static void rent_chunk(Chunk chunk, Player p){
        if(chunk.getX()==0 || chunk.getZ()==0)return;
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();

        World w = BukkitAdapter.adapt(p.getLocation().getWorld());
        RegionManager regions = container.get(w);

        ProtectedRegion region = regions.getRegion("x"+chunk.getX()+"z"+chunk.getZ());

        DefaultDomain owners = region.getOwners();
        owners.addPlayer(p.getUniqueId());
    }

    public static void private_all_chunks_minus(){

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(BukkitAdapter.adapt( Bukkit.getWorld("world")));
        //if(regions.getRegion("x1z1")==null) {
            System.out.println("Private all chunks!");
            for (int x = -50; x < 0 ; x++) {
                for (int z = -50; z < 50; z++) {
                    Chunk chunk = Bukkit.getWorld("world").getChunkAt(x, z);
                    int bx = chunk.getX() << 4;
                    int bz = chunk.getZ() << 4;

                    BlockVector3 min = BlockVector3.at(bx, 0, bz);
                    BlockVector3 max = BlockVector3.at(bx + 15, 256, bz + 15);

                    ProtectedCuboidRegion region = new ProtectedCuboidRegion("x" + x + "z" + z, min, max);
                    regions.addRegion(region);
                }
            }
        System.out.println("Private all chunk minus ok!");
    }

    public static void private_all_chunks_plus(){
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(BukkitAdapter.adapt( Bukkit.getWorld("world")));
        //if(regions.getRegion("x1z1")==null) {
        System.out.println("Private all chunks!");
        for (int x = 0; x < 50 ; x++) {
            for (int z = -50; z < 50; z++) {
                Chunk chunk = Bukkit.getWorld("world").getChunkAt(x, z);
                int bx = chunk.getX() << 4;
                int bz = chunk.getZ() << 4;

                BlockVector3 min = BlockVector3.at(bx, 0, bz);
                BlockVector3 max = BlockVector3.at(bx + 15, 256, bz + 15);

                ProtectedCuboidRegion region = new ProtectedCuboidRegion("x" + x + "z" + z, min, max);
                regions.addRegion(region);
            }

        }
        System.out.println("Pruvate all chunk + ok!");
    }


    public static String get_first_owner(Location loc){

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        BlockVector3 reg_loc = BlockVector3.at(loc.getX(), loc.getY(), loc.getZ());
        ApplicableRegionSet regions =  container.get(BukkitAdapter.adapt( Bukkit.getWorld("world"))).getApplicableRegions(reg_loc);
        for (ProtectedRegion r : regions) {
            return r.getMembers().getPlayers().toArray()[0].toString();
        }
        return "NONE";

    }


    public static boolean isOwner(Player p){

        Location loc = p.getLocation();

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        BlockVector3 reg_loc = BlockVector3.at(loc.getX(), loc.getY(), loc.getZ());
        ApplicableRegionSet regions =  container.get(BukkitAdapter.adapt( Bukkit.getWorld("world"))).getApplicableRegions(reg_loc);


        for (ProtectedRegion r : regions) {
            if(r.getOwners().contains(p.getUniqueId())) return true;
        }
        p.sendMessage(no_your_chunk);
        return false;
    }

    public static boolean isset_region_rent(Chunk chunk){
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(BukkitAdapter.adapt( Bukkit.getWorld("world")));

        ProtectedRegion region = regions.getRegion("x"+chunk.getX()+"z"+chunk.getZ());
        if(region == null) return true;
        DefaultDomain owners = region.getOwners();

        if(owners.getPlayerDomain().size() > 0) return true;
        return false;
    }

    public static boolean region_isset(Chunk chunk){

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(BukkitAdapter.adapt( Bukkit.getWorld("world")));

        ProtectedRegion region = regions.getRegion("x"+chunk.getX()+"z"+chunk.getZ());
        if(region == null) return false;
        return true;
    }

    public static void delete_owner(String id){
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(BukkitAdapter.adapt(Bukkit.getWorld("world")));
        DefaultDomain owners = regions.getRegion(id).getOwners();
        DefaultDomain members = regions.getRegion(id).getMembers();

        owners.removeAll();
        members.removeAll();

    }

    public static String get_region_name(Player p){
        Chunk loc = p.getLocation().getChunk();

        return "x"+loc.getX()+"z"+loc.getZ();
    }


}
