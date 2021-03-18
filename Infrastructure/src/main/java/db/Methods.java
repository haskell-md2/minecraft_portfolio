package db;

import org.bukkit.Chunk;
import org.bukkit.Location;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Methods {

    private static final String SQL_ISSET_CHUNK = "SELECT * FROM `chunks` WHERE x = ? AND z=?";
    private static final String SQL_RENT_CHUNK = "INSERT INTO `chunks` VALUES (?,?,?,?,?)";
    private static final String SQL_GET_EXPIRED  = "SELECT * FROM `chunks` WHERE next_rent < ?";
    private static final String SQL_DELL_RENT = "DELETE FROM `chunks` WHERE `id` = ?";;
    private static final String SQL_SHARPNESS= "UPDATE `chunks` SET `next_rent` = ? WHERE `id` = ?";
    private static final String SQL_DEADLINE_CHUNK = "SELECT next_rent FROM `chunks` WHERE id = ?";
    private static final String SQL_GET_AUTORENT = "SELECT auto FROM `chunks` WHERE id = ?";
    private static final String SQL_CHUNGE_RENT= "UPDATE `chunks` SET auto = NOT auto WHERE `id` = ?";

    //old
    private static final String SQL_ADD_RELIGION="INSERT INTO `religions` VALUES (?,?,?,?)";
    private static final String SQL_ADD_PLAYERS="INSERT INTO `pl` VALUES (?,?,?)";
    private static final String SQL_GET_PLAYER_RELIGION_OWNER="SELECT * FROM `religions` WHERE `owner` = ?";
    private static final String SQL_DELETE_RELIGION="DELETE FROM `religions` WHERE `name` = ?";
    private static final String SQL_GET_SCORE = "SELECT * FROM `pl` WHERE `name` = ?";
    private static final String SQL_SET_RELIG = "UPDATE `pl` SET `rel` = ? WHERE `name` = ?";
    private static final String SQL_SET_SCORE = "UPDATE `pl` SET `score` = score+? WHERE `name` = ?";
    private static final String SQL_SET_NONE = "UPDATE `pl` SET `rel` = ? WHERE `rel` = ?";
    private static final String SQL_LEAVE = "UPDATE `pl` SET `rel` = ? WHERE `name` = ?";
    private static final String SQL_SET_RELIG_OPEN = "UPDATE `pl` SET `rel` = ? WHERE `name`= ?"; //проблема тут
    private static final String SQL_SET_FLAG= "UPDATE `religions` SET `open` = ? WHERE `owner` = ?";
    private static final String SQL_GET_FLAG = "SELECT * FROM `religions` WHERE `name` = ?";
    private static final String SQL_GET_PLAYER_RELIGION="SELECT * FROM `pl` WHERE `name` = ?";
    private static final String SQL_IS_OWNER ="SELECT * FROM `religions` WHERE `owner` = ?";
    private static final String SQL_GET_INFO ="SELECT * FROM `religions` WHERE `name` = ?";
    private static final String SQL_PLUS_HUNGRED = "UPDATE `pl` SET `score` = 100 WHERE `name` = ?";
    private static final String SQL_MINUS_HUNGRED = "UPDATE `pl` SET `score` = -100 WHERE `name` = ?";
    private static final String SQL_GET_ALL_PLAYERS ="SELECT COUNT(*) FROM `pl` WHERE `rel` = ?";
    private static final String SQL_WRITE_CHUNK_MAIN = "INSERT INTO `chunk` VALUES (?,?,?,?)";
    private static final String SQL_ADD_CHUNK = "INSERT INTO `chunk` VALUES (?,?,?,?)";
    private static final String SQL_CHECK_MAIN ="SELECT * FROM `chunk` WHERE `rel` = ? AND `main` = ?";
    private static final String SQL_ADJOIN ="SELECT * FROM `chunk` WHERE `rel` = ? AND ((`x` = ?-1 AND `z` = ?) OR (`x` = ? AND `z` = ?-1) OR (`x` = ?+1 AND `z` = ?) OR (`x` = ? AND `z` = ?+1))";
    private static final String SQL_CHUNK_IS_SET = "SELECT * FROM `chunk` WHERE `rel` = ? AND `x` = ? AND `z` = ?";
    private static final String SQL_DELETE_CHUNK = "DELETE FROM `chunk` WHERE `rel` = ?";
    private static final String SQL_ADD_ALT = "INSERT INTO `alt` VALUES (?,?,?,?)";
    private static final String SQL_ALTAR_BY_LOC = "SELECT * FROM `alt` WHERE `x` = ? AND `y` = ? AND `z` = ?";
    private static final String SQL_DELETE_ALT = "DELETE FROM `alt` WHERE `rel` = ?";
    private static final String SQL_REL_BY_CHUNK = "SELECT * FROM `chunk` WHERE `x` = ? AND `z` = ?";
    private static final String SQL_RESET_CHUNK = "UPDATE `chunk` SET `rel` = ? WHERE `x` = ? AND `z`= ?";
    private static final String SQL_ADD_MONEY = "UPDATE `religions` SET `bank` = bank + ? WHERE `name` = ?";
    private static final String SQL_CHUNK_IS_MAIN = "SELECT * FROM `chunk` WHERE `x` = ? AND `z` = ? AND `rel` = ?";
    private static final String SQL_GET_ALL_CHUNKS_FROM_RELIG ="SELECT COUNT(*) FROM `chunk` WHERE `rel` = ?";


    public static void change_autorent(String id){
        try {
            PreparedStatement statement = SQlite.conn.prepareStatement(SQL_CHUNGE_RENT);
            statement.setString(1,id);
            statement.executeUpdate();
        }catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public static boolean get_autorent(String id){
        try {
            PreparedStatement statement = SQlite.conn.prepareStatement(SQL_GET_AUTORENT);
            statement.setString(1,id);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.getBoolean("auto");
        }catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public static int get_dead_line(String id){
        try {
            PreparedStatement statement = SQlite.conn.prepareStatement(SQL_DEADLINE_CHUNK);
            statement.setString(1,id);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.getInt("next_rent");
        }catch (SQLException e){
            System.out.println(e.getMessage());
            //e.printStackTrace();

        }
        return 0;
    }

    public static void sharpness_rent(String id, int time){
        try {
            PreparedStatement statement = SQlite.conn.prepareStatement(SQL_SHARPNESS);
            statement.setInt(1,time);
            statement.setString(2,id);
            statement.executeUpdate();
        }catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }


    public static void cancel_rent(String name){
        try {
            PreparedStatement statement = SQlite.conn.prepareStatement(SQL_DELL_RENT);
            statement.setString(1,name);
            statement.executeUpdate();
        }catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }



    public static ArrayList<String[]> get_expired(int RealTime) {
        ArrayList<String[]> regions = new ArrayList<String[]>();
        try {
            PreparedStatement statement = SQlite.conn.prepareStatement(SQL_GET_EXPIRED);
            statement.setInt(1, RealTime);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String info[] = {resultSet.getString(1),
                        resultSet.getString(2),
                        String.valueOf(resultSet.getInt(3)),
                        String.valueOf(resultSet.getBoolean(4)),
                                String.valueOf(resultSet.getInt(5))};
                regions.add(info);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("No Find");
        }
        return regions;
    }


    public static boolean isset(int x, int z){
        try {
            PreparedStatement statement = SQlite.conn.prepareStatement(SQL_ISSET_CHUNK);
            statement.setInt(1,x);
            statement.setInt(2,z);
            ResultSet resultSet = statement.executeQuery();
            System.out.println(resultSet.getString("id"));
            return true;
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static boolean rent_Chunk(String id, String player_name, int price, Boolean auto, int next_rent){
        try {
            PreparedStatement statement = SQlite.conn.prepareStatement(SQL_RENT_CHUNK);
            statement.setString(1,id);
            statement.setString(2,player_name);
            statement.setInt(3,price);
            statement.setBoolean(4, auto);
            statement.setInt(5, next_rent);

            statement.executeUpdate();
            return true;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }


    //odl

    public static void CreateReligons(String name,String owner) throws SQLException {
        try(PreparedStatement statement = SQlite.conn.prepareStatement(SQL_ADD_RELIGION)) {
            statement.setString(1,name);
            statement.setString(2,owner);
            statement.setBoolean(3,false);
            statement.setInt(4,0);
            statement.executeUpdate();
        }
    }

    public static void RegisterPLayer(String name) throws SQLException {
        try(PreparedStatement statement = SQlite.conn.prepareStatement(SQL_ADD_PLAYERS)) {
            statement.setString(1,name);
            statement.setInt(2,0);
            statement.setString(3,"NONE");
            statement.executeUpdate();
        }
    }

    public static String GetReligionPlayerOwner(String name){
        try(PreparedStatement statement = SQlite.conn.prepareStatement(SQL_GET_PLAYER_RELIGION_OWNER)) {
            statement.setString(1,name);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.getString("name");
        }catch (SQLException e){
            return "NONE";
        }
    }

    public static void DeleteReligion(String name) throws SQLException {
        try(PreparedStatement statement = SQlite.conn.prepareStatement(SQL_DELETE_RELIGION)) {
            UpdateRelig(name);
            DeleteChunks(name);
            DeleteAlt(name);
            statement.setString(1,name);
            statement.executeUpdate();
        }
    }

    public static int GetScore(String name){
        try(PreparedStatement statement = SQlite.conn.prepareStatement(SQL_GET_SCORE)) {
            statement.setString(1,name);
            return statement.executeQuery().getInt("score");
        }catch (SQLException e){
            e.printStackTrace();
            return 0;
        }
    }

    public static String GetRelig(String name){
        try(PreparedStatement statement = SQlite.conn.prepareStatement(SQL_GET_PLAYER_RELIGION)) {
            statement.setString(1,name);
            return statement.executeQuery().getString("rel");
        }catch (SQLException e){
            e.printStackTrace();
        }
        return "";
    }

    public static void SetPlayerRelig(String name, String player) throws SQLException {
        try(PreparedStatement statement = SQlite.conn.prepareStatement(SQL_SET_RELIG)) {
            statement.setString(1,name);
            statement.setString(2,player);
            statement.executeUpdate();
        }
    }

    public static void SetScore(int score,String name){
        try(PreparedStatement statement = SQlite.conn.prepareStatement(SQL_SET_SCORE)) {
            //TODO проверка на -100 и 100
            statement.setInt(1,score);
            statement.setString(2,name);
            statement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void UpdateRelig(String name) throws SQLException {
        try(PreparedStatement statement = SQlite.conn.prepareStatement(SQL_SET_NONE)) {
            statement.setString(1,"NONE");
            statement.setString(2,name);
            statement.executeUpdate();
        }
    }

    public static void SetPlayerReligOpen(String name, String player) throws SQLException {
        try(PreparedStatement statement = SQlite.conn.prepareStatement(SQL_SET_RELIG_OPEN)) {
            statement.setString(1, name);
            statement.setString(2, player);
            statement.executeUpdate();
        }
    }

    public static boolean GetFlag(String relig) throws SQLException {
        try(PreparedStatement statement = SQlite.conn.prepareStatement(SQL_GET_FLAG)) {
            statement.setString(1,relig);
            return statement.executeQuery().getBoolean("open");
        }
    }

    public static void SetFlag(String player,Boolean b) throws SQLException {
        try(PreparedStatement statement = SQlite.conn.prepareStatement(SQL_SET_FLAG)) {
            statement.setBoolean(1,b);
            statement.setString(2,player);
            statement.executeUpdate();
        }
    }

    public static void Leave(String player) throws SQLException{
        try(PreparedStatement statement = SQlite.conn.prepareStatement(SQL_LEAVE)) {
            statement.setString(1,"NONE");
            statement.setString(2,player);
            statement.executeUpdate();
        }
    }

    public static boolean IsOwner(String player){
        try(PreparedStatement statement = SQlite.conn.prepareStatement(SQL_IS_OWNER)) {
            statement.setString(1,player);
            ResultSet resultSet = statement.executeQuery();
            return true;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public static String[] GetInfo(String name) {
        String[] itog = new String[3];
        try (PreparedStatement statement = SQlite.conn.prepareStatement(SQL_GET_INFO)) {
            statement.setString(1, name);
            itog[0] = statement.executeQuery().getString("owner");
            itog[1] = statement.executeQuery().getString("bank");
        } catch (SQLException e) {
            e.printStackTrace();
            return itog;
        }

        try(PreparedStatement statement = SQlite.conn.prepareStatement(SQL_GET_ALL_PLAYERS)){

            statement.setString(1,name);

            ResultSet rs = statement.executeQuery();
            //Retrieving the result
            rs.next();
            int count = rs.getInt(1);
            itog[2] = String.valueOf(count);
        } catch (SQLException e) {
            e.printStackTrace();
            return itog;
        }
        return itog;
    }


    public static void SetPlusHungred(String name){
        try(PreparedStatement statement = SQlite.conn.prepareStatement(SQL_PLUS_HUNGRED)){
            statement.setString(1,name);
            statement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void SetMinusHungred(String name){
        try(PreparedStatement statement = SQlite.conn.prepareStatement(SQL_MINUS_HUNGRED)){
            statement.setString(1,name);
            statement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static boolean CheckMain(String rel){
        System.out.println(rel+"g");
        try(PreparedStatement statement = SQlite.conn.prepareStatement((SQL_CHECK_MAIN))){

            statement.setString(1,rel);
            statement.setBoolean(2,true);
            statement.executeQuery().getInt("x");
            return true;

        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }


    public static void WriteChunkMain(int x,int z, String relig){
        try(PreparedStatement statement = SQlite.conn.prepareStatement(SQL_WRITE_CHUNK_MAIN)){

            statement.setInt(1,x);
            statement.setInt(2,z);
            statement.setString(3,relig);
            statement.setBoolean(4,true);

            statement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void AddChunk(int x,int z, String relig){
        try(PreparedStatement statement = SQlite.conn.prepareStatement(SQL_ADD_CHUNK)){

            statement.setInt(1,x);
            statement.setInt(2,z);
            statement.setString(3,relig);
            statement.setBoolean(4,false);

            statement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static boolean chunkIsSet(int x, int z, String relig){
        try(PreparedStatement statement = SQlite.conn.prepareStatement(SQL_CHUNK_IS_SET)){
            statement.setString(1,relig);
            statement.setInt(2, x);
            statement.setInt(3, z);

            statement.executeQuery().getInt("x");
            return false;
        }catch (SQLException e){
            //e.printStackTrace();
        }
        return true;
    }

    public static boolean Adjoins(int x, int z, String relig){
        try(PreparedStatement statement = SQlite.conn.prepareStatement(SQL_ADJOIN)){

            statement.setString(1,relig);
            statement.setInt(2, x);
            statement.setInt(3, z);
            statement.setInt(4, x);
            statement.setInt(5, z);
            statement.setInt(6, x);
            statement.setInt(7, z);
            statement.setInt(8, x);
            statement.setInt(9, z);

            System.out.println("X "+x);
            System.out.println("Z "+z);

            System.out.println("SQL X "+statement.executeQuery().getInt("x"));
            System.out.println("SQL Z "+statement.executeQuery().getInt("z"));
            return true;

        }catch (SQLException e){
           // e.printStackTrace();
        }
        return false;
    }

    public static void DeleteChunks(String rel){
        try(PreparedStatement statement = SQlite.conn.prepareStatement(SQL_DELETE_CHUNK)){

            statement.setString(1,rel);
            statement.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void AddAltar(Location loc, String rel){
        try(PreparedStatement statement = SQlite.conn.prepareStatement(SQL_ADD_ALT)){

            statement.setInt(1,(int) loc.getX());
            statement.setInt(2,(int) loc.getY());
            statement.setInt(3,(int) loc.getZ());
            statement.setString(4,rel);
            statement.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static boolean AltByLoc(int x,int y, int z){
        try(PreparedStatement statement = SQlite.conn.prepareStatement(SQL_ALTAR_BY_LOC)){

            statement.setInt(1,x);
            statement.setInt(2,y);
            statement.setInt(3,z);

            statement.executeQuery().getInt("x");
            return false;
        }catch (SQLException e){
            e.printStackTrace();
            return true;
        }
    }

    public static void DeleteAlt(String rel){
        try(PreparedStatement statement = SQlite.conn.prepareStatement(SQL_DELETE_ALT)){

            statement.setString(1,rel);
            statement.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static String GetRelByChunk(Chunk chunk){
        try(PreparedStatement statement = SQlite.conn.prepareStatement(SQL_REL_BY_CHUNK)){
            statement.setInt(1,chunk.getX());
            statement.setInt(2,chunk.getZ());

            String rel = statement.executeQuery().getString("rel");
            return rel;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public static void ChangeChunkRel(Chunk chunk, String rel){
        try(PreparedStatement statement = SQlite.conn.prepareStatement(SQL_RESET_CHUNK)){

            statement.setString(1,rel);
            statement.setInt(2,chunk.getX());
            statement.setInt(3,chunk.getZ());
            statement.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void AddMoney(int money, String rel){
        try(PreparedStatement statement = SQlite.conn.prepareStatement(SQL_ADD_MONEY)){

            statement.setInt(1, money);
            statement.setString(2,rel);
            statement.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static boolean chunkIsMain(int x, int z, String relig){
        try(PreparedStatement statement = SQlite.conn.prepareStatement(SQL_CHUNK_IS_MAIN)){

            statement.setInt(1, x);
            statement.setInt(2, z);
            statement.setString(3,relig);

            return statement.executeQuery().getBoolean("main");
        }catch (SQLException e){
            e.printStackTrace();

        }
        return false;
    }

    public static int countChunk(String rel){
        try(PreparedStatement statement = SQlite.conn.prepareStatement(SQL_GET_ALL_CHUNKS_FROM_RELIG)) {

            statement.setString(1,rel);

            ResultSet rs = statement.executeQuery();
            //Retrieving the result
            rs.next();
            int count = rs.getInt(1);
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int countPlayer(String relig){
        try(PreparedStatement statement = SQlite.conn.prepareStatement(SQL_GET_ALL_PLAYERS)) {

            statement.setString(1,relig);

            ResultSet rs = statement.executeQuery();
            //Retrieving the result
            rs.next();
            int count = rs.getInt(1);
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


}
