package Process;

public class Category {
    private int id;
    private String name;
    private int cost;

    public Category(int id,String name){
        this.id=id;
        this.name=name;
    }

    public Category(String name,int cost,int id){
        this.name=name;
        this.cost=cost;
        this.id=id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

}
