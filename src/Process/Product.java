package Process;

import java.sql.Date;

public class Product {
    private int id;
    private int categoryId;
    private String categoryName;
    private int subcategoryId;
    private String subcategoryName;
    private int cost;
    private Date buyDate;
    private String message;
    //input
    public Product(int id,int cid,int scid,int cost,Date date,String mes){
        this.id=id;
        this.categoryId=cid;
        this.subcategoryId=scid;
        this.cost=cost;
        this.buyDate=date;
        this.message=mes;
    }
    //output
    public Product(int id,String cName,String scName,int cost,Date date,String mes){
        this.id=id;
        this.categoryName=cName;
        this.subcategoryName=scName;
        this.cost=cost;
        this.buyDate=date;//date変
        this.message=mes;
    }


    public boolean checkMessage(){
        if(this.message.length()<20){
            return true;
        }
        return false;
    }

    public int getId() {
        return id;
    }

    public Date getBuyDate() {
        return buyDate;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getCost() {
        return cost;
    }

    public int getSubcategoryId() {
        return subcategoryId;
    }

    public String getMessage() {
        return message;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getSubcategoryName() {
        return subcategoryName;
    }
}
