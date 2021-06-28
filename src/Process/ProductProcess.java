package Process;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class ProductProcess {
    //追加、削除、編集、データ取得
    public static void addProduct(int userId,Product product){
        //完成
        String sql="insert into costdata"+
                "(userId,categoryId,subcategoryId,buyDate,cost,message)" +
                "values(?,?,?,?,?,?)";
        Connection con=DbAccess.dbLink();
        try{
            PreparedStatement pstmt=con.prepareStatement(sql);
            //データ入力
            pstmt.setInt(1,userId);
            pstmt.setInt(2,product.getCategoryId());
            pstmt.setInt(3,product.getSubcategoryId());
            pstmt.setDate(4,product.getBuyDate());
            pstmt.setInt(5,product.getCost());
            pstmt.setString(6,product.getMessage());

            pstmt.executeUpdate();
            pstmt.close();
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            DbAccess.dbShut(con);
        }
    }

    public static void deleteProduct(int productId){
        //完成
        String sql="delete from costdata where id=?;";
        Connection con=DbAccess.dbLink();
        try{
            PreparedStatement pstmt=con.prepareStatement(sql);

            pstmt.setInt(1,productId);

            pstmt.executeUpdate();
            pstmt.close();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DbAccess.dbShut(con);
        }
    }

    public static void updateProduct(Product product){
        //完成
        String sql="update costdata set " +
                "categoryId=?, subcategoryId=?, cost=?, message=?" +
                " where id=?;";
        Connection con=DbAccess.dbLink();
        try{
            PreparedStatement pstmt=con.prepareStatement(sql);

            pstmt.setInt(1,product.getCategoryId());
            pstmt.setInt(2,product.getSubcategoryId());
            pstmt.setInt(3,product.getCost());
            pstmt.setString(4,product.getMessage());
            pstmt.setInt(5,product.getId());

            int r=pstmt.executeUpdate();
            if(r!=0) System.out.println("編集完了");
            pstmt.close();
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            DbAccess.dbShut(con);
        }
    }

    //日ごと取得 品目のリストを返す
    public static ArrayList<Product> fetchProduct(int userId, Date selectDate) {
        //完成
        String sql="select * from costdata " +
                "join categorylist on costdata.categoryid = categorylist.id " +
                "join subcategorylist on costdata.subcategoryid = subcategorylist.id " +
                "where buyDate=? and costdata.userId=?" +
                "order by categoryId ASC;";
        ArrayList<Product> products=new ArrayList<>();
        Product product;
        Connection con=DbAccess.dbLink();
        try{
            PreparedStatement pstmt =con.prepareStatement(sql);

            pstmt.setDate(1,selectDate);
            pstmt.setInt(2,userId);

            ResultSet resultSet=pstmt.executeQuery();
            while(resultSet.next()){
                product=new Product(
                        resultSet.getInt("id"),
                        resultSet.getString("categorylist.categoryName"),
                        resultSet.getString("subcategorylist.subcategoryName"),
                        resultSet.getInt("cost"),
                        resultSet.getDate("buyDate"),
                        resultSet.getString("message")
                );
                products.add(product);
            }
            pstmt.close();
            resultSet.close();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DbAccess.dbShut(con);
        }
        return products;
    }

    public static ArrayList<Product> fetchMonthProduct(int userId, LocalDate selectDate) {
        //完成
        String sql="select * from costdata " +
                "join categorylist on costdata.categoryid = categorylist.id " +
                "join subcategorylist on costdata.subcategoryid = subcategorylist.id " +
                "where year(buyDate)=? and month(buyDate)=? and costdata.userId=?" +
                "order by buyDate ASC;";
        ArrayList<Product> products=new ArrayList<>();
        Product product;
        Connection con=DbAccess.dbLink();
        try{
            PreparedStatement pstmt =con.prepareStatement(sql);

            pstmt.setInt(1,selectDate.getYear());
            pstmt.setInt(2,selectDate.getMonth().getValue());
            pstmt.setInt(3,userId);

            ResultSet resultSet=pstmt.executeQuery();
            while(resultSet.next()){
                product=new Product(
                        resultSet.getInt("id"),
                        resultSet.getString("categorylist.categoryName"),
                        resultSet.getString("subcategorylist.subcategoryName"),
                        resultSet.getInt("cost"),
                        resultSet.getDate("buyDate"),
                        resultSet.getString("message")
                );
                products.add(product);
            }
            pstmt.close();
            resultSet.close();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DbAccess.dbShut(con);
        }
        return products;
    }

}
