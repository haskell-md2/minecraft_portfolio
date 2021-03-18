package airdrop;

import org.bukkit.Location;
import org.bukkit.Material;

public class utilits {
    //используется для генерации координат
    double[] generate_cords(){
        double[] itog = new double[3];

        int quantity_of_areas = Airdrop.main.getConfig().getInt("quantity_of_areas");

        int r = (int)Math.round(1+Math.random()*quantity_of_areas);

        double x = Math.round(Airdrop.main.getConfig().getInt("areas.area"+r+".x_begin") + Math.random() * Airdrop.main.getConfig().getInt("areas.area"+r+".x_end"));
        double z = Math.round(Airdrop.main.getConfig().getInt("areas.area"+r+".z_begin") + Math.random() * Airdrop.main.getConfig().getInt("areas.area"+r+".x_end"));

        Location temp_loc = new Location(Airdrop.world,x,120,z);
        boolean pos_is_ok = false;
        for (double i = 120; i > 0; i--){
            temp_loc.setY(i-1);
            if(temp_loc.getBlock().getType()!= Material.AIR){
                if(temp_loc.getBlock().getType().isSolid()){
                    itog[0] = x;
                    itog[1] = i;
                    itog[2] = z;
                    return itog;
                }
            }
        }
        return itog;
    }

    double[] make_cords_by_location(Location loc){
        double[] itog = new double[3];

        double x = Math.round(loc.getX());
        double z = Math.round(loc.getZ());

        Location temp_loc = new Location(Airdrop.world,x,120,z);
        boolean pos_is_ok = false;
        for (double i = 120; i > 0; i--) {
            temp_loc.setY(i - 1);
            if (temp_loc.getBlock().getType() != Material.AIR) {
                itog[0] = x;
                itog[1] = i;
                itog[2] = z;
                return itog;
            }
        }
        return itog;
    }
}
