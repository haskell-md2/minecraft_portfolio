package db;

import haskell.Infrastructure;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQlite {

    public static Connection conn;

    private static final String SQL_CREATE_TABLE_CHUNKS =
            "CREATE TABLE IF NOT EXISTS `chunks` (" +
                    "`id` VARCHAR(64) NOT NULL,"+ //1
                    "`owner` VARCHAR(64) NOT NULL,"+ //2
                    "`price` INTEGER  NOT NULL,"+ //3
                    "`auto` BOOLEAN NOT NULL,"+ //4
                    "`next_rent` INTEGER NOT NULL"+ //5
                    ")";


    public static void connect() throws SQLException, ClassNotFoundException {

        String path = Infrastructure.main.getDataFolder()+File.separator;
        CreateDataBase();
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:"+path+"Infrastructure.db");
            conn.createStatement().executeUpdate(SQL_CREATE_TABLE_CHUNKS);
            System.out.println("Data Base enable!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void CreateDataBase(){
        File dataBase = new File(Infrastructure.main.getDataFolder()+File.separator+"Infrastructure.db");
        if(!dataBase.exists()) {
            try { dataBase.createNewFile(); } catch (IOException e) { e.printStackTrace(); }
        }
    }

    public static void close() throws  SQLException, ClassCastException{
        conn.close();
    }

}
