package Process;

import java.sql.*;
import java.time.LocalDate;

public class BudgetProcess {
    public static boolean checkBudget(LocalDate date){
        //完成
        String sql="select * " +
                "from budgetList" +
                " where month(budgetDate) = ? and year(budgetDate)=? limit 1;";
        Connection con=DbAccess.dbLink();
        boolean check=false;
        try{
            PreparedStatement pstmt=con.prepareStatement(sql);
            pstmt.setInt(1,date.getMonth().getValue());
            pstmt.setInt(2,date.getYear());
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

    public static void addBudget(int userId, int budget, LocalDate date){
        String sql="insert into BudgetLIst(userId,budgetDate,budget) " +
                "values(?,?,?) ";

        Connection con=DbAccess.dbLink();
        try{
            PreparedStatement pstmt=con.prepareStatement(sql);
            pstmt.setInt(1,userId);
            pstmt.setDate(2, Date.valueOf(date));
            pstmt.setInt(3,budget);

            pstmt.executeUpdate();
            pstmt.close();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DbAccess.dbShut(con);
        }
    }

    public static void updateBudget(int userId, int budget, LocalDate date){
        //完成
        String sql="update budgetList set " +
                "budget=?" +
                " where MONTH(budgetDate)=? and year(budgetDate)=? and userId=?;";
        Connection con=DbAccess.dbLink();
        try{
            PreparedStatement pstmt=con.prepareStatement(sql);

            pstmt.setInt(1,budget);
            pstmt.setInt(2,date.getMonth().getValue());
            pstmt.setInt(3,date.getYear());
            pstmt.setInt(4,userId);

            int r=pstmt.executeUpdate();
            if(r!=0) System.out.println("編集完了");
            pstmt.close();
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            DbAccess.dbShut(con);
        }
    }

    public static int fetchBudget(int userId, LocalDate date) {
        //完成
        int money=0;
        String sql="select budget from budgetList " +
                "where MONTH(budgetDate)=? and year(budgetDate)=? and userId=?";

        Connection con=DbAccess.dbLink();
        try{
            PreparedStatement pstmt =con.prepareStatement(sql);

            pstmt.setInt(1,date.getMonth().getValue());
            pstmt.setInt(2,date.getYear());
            pstmt.setInt(3,userId);

            ResultSet resultSet=pstmt.executeQuery();
            while(resultSet.next()){
                money=resultSet.getInt("budget");
            }
            pstmt.close();
            resultSet.close();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DbAccess.dbShut(con);
        }
        return money;
    }
}
