package Process;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class CalculateProcess {

    public static ArrayList<Category> fetchCategoryCostMonth(int userId, LocalDate date){
        ArrayList<Category>categories=new ArrayList<>();
        Category category;
        String sql="select categorylist.categoryName, sum(cost), categoryId from costdata " +
                "join categoryList on costdata.categoryId = categorylist.id " +
                "where year(buyDate)=? and month(buyDate)=? and costdata.userId=? " +
                "group by categoryId ;";

        Connection con=DbAccess.dbLink();
        try{
            PreparedStatement pstmt=con.prepareStatement(sql);
            pstmt.setInt(1,date.getYear());
            pstmt.setInt(2,date.getMonth().getValue());
            pstmt.setInt(3,userId);
            ResultSet resultSet=pstmt.executeQuery();
            
            while(resultSet.next()){
                category=new Category(
                        resultSet.getString(1),
                        resultSet.getInt(2),
                        resultSet.getInt(3)
                );
                categories.add(category);
            }
            pstmt.close();
            resultSet.close();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DbAccess.dbShut(con);
        }
        return categories;
    }

    public static ArrayList<Category> fetchSubCategoryCostMonth(int userId,LocalDate date,int cId){
        ArrayList<Category>categories=new ArrayList<>();
        Category category;
        String sql="select subcategorylist.subcategoryName, sum(cost) ,subCategoryId from costdata " +
                "join subcategoryList on costdata.subcategoryId = subcategorylist.id " +
                "where year(buyDate)=? and month(buyDate)=? and costdata.userId=? and costdata.categoryId=? " +
                "group by subcategoryId ";

        Connection con=DbAccess.dbLink();
        try{
            PreparedStatement pstmt=con.prepareStatement(sql);
            pstmt.setInt(1,date.getYear());
            pstmt.setInt(2,date.getMonth().getValue());
            pstmt.setInt(3,userId);
            pstmt.setInt(4,cId);
            ResultSet resultSet=pstmt.executeQuery();

            while(resultSet.next()){
                category=new Category(
                        resultSet.getString(1),
                        resultSet.getInt(2),
                        resultSet.getInt(3)
                );
                categories.add(category);
            }
            pstmt.close();
            resultSet.close();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DbAccess.dbShut(con);
        }
        return categories;
    }

    public static int fetchMonthCost(int id,LocalDate date){
        int cost=0;
        String sql="select sum(cost) from costData " +
                "where year(buyDate)=? and month(buyDate)=? and userId=? ;";

        Connection con=DbAccess.dbLink();
        try{
            PreparedStatement pstmt=con.prepareStatement(sql);
            pstmt.setInt(1,date.getYear());
            pstmt.setInt(2,date.getMonth().getValue());
            pstmt.setInt(3,id);
            ResultSet resultSet=pstmt.executeQuery();

            while(resultSet.next()){
                cost=resultSet.getInt(1);
            }
            pstmt.close();
            resultSet.close();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DbAccess.dbShut(con);
        }
        return cost;
    }

}
