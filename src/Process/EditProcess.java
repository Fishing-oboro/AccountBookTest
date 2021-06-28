package Process;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EditProcess {

    public static ArrayList<Category> fetchCategoryList(int userId){
        ArrayList<Category>categoryList=new ArrayList<>();
        Category category;

        String sql="select * " +
                "from categoryList " +
                "where userId=0 or userId=? " +
                "order by userId ASC;";

        Connection con=DbAccess.dbLink();
        try{
            PreparedStatement pstmt=con.prepareStatement(sql);
            pstmt.setInt(1,userId);

            ResultSet resultSet=pstmt.executeQuery();

            while(resultSet.next()){
                //選択に挿入
                category=new Category(
                        resultSet.getInt("id"),
                        resultSet.getString("categoryName")
                );
                categoryList.add(category);
            }
            pstmt.close();
            resultSet.close();
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            DbAccess.dbShut(con);
        }
        return categoryList;
    }

    public static ArrayList<Category> fetchSubCategoryList(int userId,int id){
        ArrayList<Category>categoryList=new ArrayList<>();
        Category category;

        String sql="select * " +
                "from subcategoryList " +
                "where categoryId=? and (userId=0 or userId=?) " +
                "order by userId ASC;";

        Connection con=DbAccess.dbLink();
        try{
            PreparedStatement pstmt=con.prepareStatement(sql);
            pstmt.setInt(1,id);
            pstmt.setInt(2,userId);
            ResultSet resultSet=pstmt.executeQuery();

            while(resultSet.next()){
                //選択に挿入
                category=new Category(
                        resultSet.getInt("id"),
                        resultSet.getString("subCategoryName")
                );
                categoryList.add(category);
            }
            pstmt.close();
            resultSet.close();
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            DbAccess.dbShut(con);
        }
        return categoryList;
    }

    public static boolean checkCategory(String categoryName,int userId){
        //完成
        String sql="select * " +
                "from categoryList " +
                "where (userId=? or userId=0) and categoryName=? limit 1;";

        Connection con=DbAccess.dbLink();
        boolean check=false;
        try{
            PreparedStatement pstmt=con.prepareStatement(sql);
            pstmt.setInt(1,userId);
            pstmt.setString(2,categoryName);
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

    public static boolean checkSubCategory(String categoryName,int userId,int cid){
        //完成
        String sql="select * " +
                "from subCategoryList " +
                "join categoryList on subCategoryList.categoryId = categorylist.id " +
                "where (subcategoryList.userId=? or subcategoryList.userId=0) and subCategoryList.subCategoryName=? and categoryId=? limit 1;";

        Connection con=DbAccess.dbLink();
        boolean check=false;
        try{
            PreparedStatement pstmt=con.prepareStatement(sql);
            pstmt.setInt(1,userId);
            pstmt.setString(2,categoryName);
            pstmt.setInt(3,cid);
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

    public static void addCategory(String categoryName,int userId){
        String sql="insert into " +
                "categoryList(categoryName,userId) " +
                "values(?,?)";
        Connection con =DbAccess.dbLink();
        try{
            PreparedStatement pstmt=con.prepareStatement(sql);
            pstmt.setString(1,categoryName);
            pstmt.setInt(2,userId);
            pstmt.executeUpdate();
            pstmt.close();
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            DbAccess.dbShut(con);
        }
    }

    public static void addSubCategory(String categoryName,int userId,int cid){
        String sql="insert into " +
                "subCategoryList(categoryId,subCategoryName,userId) " +
                "values(?,?,?)";
        Connection con =DbAccess.dbLink();
        try{
            PreparedStatement pstmt=con.prepareStatement(sql);
            pstmt.setInt(1,cid);
            pstmt.setString(2,categoryName);
            pstmt.setInt(3,userId);
            pstmt.executeUpdate();
            pstmt.close();
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            DbAccess.dbShut(con);
        }
    }

    //元々存在していたカテゴリは消せないようにする。
    public static boolean deleteCategory(String tableName,int id,int userId){
        String sql="delete from categoryList where id=? and userId=?;";
        boolean check=false;
        if(tableName.equals("subCategoryList"))
            sql="delete from subCategoryList where id=? and userId=?;";
        Connection con=DbAccess.dbLink();
        try{
            PreparedStatement pstmt=con.prepareStatement(sql);

            pstmt.setInt(1,id);
            pstmt.setInt(2,userId);

            int r=pstmt.executeUpdate();
            if(r!=0) check=true;
            pstmt.close();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DbAccess.dbShut(con);
        }
        return check;
    }

}
