import dbconn.DbConn;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        DbConn dbConn = new DbConn();
        try{
            dbConn.selectAll();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
