package Process;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbAccess {
    private static String driver="org.h2.Driver";
    private static String url="jdbc:h2:file:~/h2db/AccountBookDateBase;Mode=PostgreSQL;AUTO_SERVER=TRUE;;MV_STORE=false";
    private static String userName="b2191520";
    //データベース接続、切断

    public static Connection dbLink(){
        Connection con=null;
        try{
            Class.forName(driver);
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        try {
            con = DriverManager.getConnection(url,userName,userName);
            return con;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return con;
    }

    public static void dbShut(Connection con){
        if(con!=null){
            try{
                con.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

}
