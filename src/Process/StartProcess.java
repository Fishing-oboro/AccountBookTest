package Process;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class StartProcess {

    //ユーザー作成、ログイン管理
    public static boolean makeUser(String userName,String userPassword){
        //完成
        String sql="insert into userlist(username,password)" +
                " values(?,?);";
        boolean result=false;
        //既に存在するか確認
        if(checkUser(userName,userPassword))return result;
        //ユーザーデータ入力
        Connection con=DbAccess.dbLink();
        try {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1,userName);
            pstmt.setString(2,userPassword);
            int r=pstmt.executeUpdate();
            //debug
            if(r!=0) System.out.println("ユーザー作成に成功しました。");
            else result=true;
            pstmt.close();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DbAccess.dbShut(con);
        }
        return result;
    }

    public static boolean checkUser(String userName,String password){
        //完成
        String sql="select *" +
                "from userlist" +
                " where username = ? and password = ? limit 1;";
        Connection con=DbAccess.dbLink();
        boolean check=false;
        try{
            PreparedStatement pstmt=con.prepareStatement(sql);
            pstmt.setString(1,userName);
            pstmt.setString(2,password);
            //selectの結果判定
            ResultSet resultSet=pstmt.executeQuery();
            while(resultSet.next()){
                check=true;
            }
            pstmt.close();
            resultSet.close();
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            DbAccess.dbShut(con);
        }
        return check;
    }

    public static User fetchUser(String userName,String password){
        //完成
        User user=new User();
        String sql="select id, username from userlist " +
                "where username = ? and password = ? limit 1;";
        Connection con =DbAccess.dbLink();
        try{
            PreparedStatement pstmt=con.prepareStatement(sql);
            pstmt.setString(1,userName);
            pstmt.setString(2,password);

            ResultSet resultSet=pstmt.executeQuery();
            while(resultSet.next()){
                user.setId(resultSet.getInt("id"));
                user.setName(resultSet.getString("username"));
            }
            pstmt.close();
            resultSet.close();
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            DbAccess.dbShut(con);
        }
        return user;
    }
}

