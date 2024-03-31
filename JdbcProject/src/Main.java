import dbconn.DbConn;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        DbConn dbConn = new DbConn();
        try{
            dbConn.select("notusertb");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}