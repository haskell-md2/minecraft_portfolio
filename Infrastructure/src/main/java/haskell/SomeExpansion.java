package haskell;

import db.Methods;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;


/**
 * This class will be registered through the register-method in the
 * plugins onEnable-method.
 */
public class SomeExpansion extends PlaceholderExpansion {

    private Infrastructure plugin;

    /**
     * Since we register the expansion inside our own plugin, we
     * can simply use this method here to get an instance of our
     * plugin.
     *
     * @param plugin
     *        The instance of our plugin.
     */
    public SomeExpansion(Infrastructure plugin){
        this.plugin = plugin;
    }

    /**
     * Because this is an internal class,
     * you must override this method to let PlaceholderAPI know to not unregister your expansion class when
     * PlaceholderAPI is reloaded
     *
     * @return true to persist through reloads
     */
    @Override
    public boolean persist(){
        return true;
    }

    /**
     * Because this is a internal class, this check is not needed
     * and we can simply return {@code true}
     *
     * @return Always true since it's an internal class.
     */
    @Override
    public boolean canRegister(){
        return true;
    }

    /**
     * The name of the person who created this expansion should go here.
     * <br>For convienience do we return the author from the plugin.yml
     *
     * @return The name of the author as a String.
     */
    @Override
    public String getAuthor(){
        return plugin.getDescription().getAuthors().toString();
    }

    /**
     * The placeholder identifier should go here.
     * <br>This is what tells PlaceholderAPI to call our onRequest
     * method to obtain a value if a placeholder starts with our
     * identifier.
     * <br>The identifier has to be lowercase and can't contain _ or %
     *
     * @return The identifier in {@code %<identifier>_<value>%} as String.
     */
    @Override
    public String getIdentifier(){
        return "someplugin";
    }

    /**
     * This is the version of the expansion.
     * <br>You don't have to use numbers, since it is set as a String.
     *
     * For convienience do we return the version from the plugin.yml
     *
     * @return The version as a String.
     */
    @Override
    public String getVersion(){
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier){

        if(player == null){
            return "";
        }

        // %someplugin_placeholder1%
        if(identifier.equals("chunkX")){
            return String.valueOf(player.getLocation().getChunk().getX());
        }

        if(identifier.equals("chunkZ")){
            return String.valueOf(player.getLocation().getChunk().getZ());
        }

        // %someplugin_placeholder2%
        if(identifier.equals("placeholder2")){
            return plugin.getConfig().getString("placeholder2", "value doesnt exist");
        }

        if(identifier.equals("price")){
            return getK(player.getLocation().getChunk())*1000 + "$";
        }

        if(identifier.equals("dead_line")){
            int dead_line = Methods.get_dead_line(chunk_name(player));
            if(dead_line<0){
                return String.valueOf(timeToString(dead_line-(int)System.currentTimeMillis()/1000));
            }
            return "NONE";
        }

        if(identifier.equals("auto")){
            Boolean res = Methods.get_autorent(chunk_name(player));
            if(res) return ChatColor.DARK_GREEN+ "Включена";
            if(!res) return ChatColor.RED+ "Выключена";
        }

        // We return null if an invalid placeholder (f.e. %someplugin_placeholder3%)
        // was provided
        return null;
    }

    private String chunk_name(Player p){
        Location loc = p.getLocation();
        return "x"+loc.getChunk().getX()+"z"+loc.getChunk().getZ();
    }

    public int getK(Chunk ch){
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



    private static String timeToString(int secs) {
        long hour = secs / 3600,
                min = secs / 60 % 60,
                sec = secs / 1 % 60;
        return String.format("%02d:%02d:%02d", hour, min, sec)+" ";
    }
}