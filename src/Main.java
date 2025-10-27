import java.util.*;

class Task{
   private int id;
   private String name;
  private  Date createdAt;

    Task(int id, String name, Date createdAt ){
     this.id = id;
     this.name = name;
     this.createdAt = createdAt;
    }
}


public  class Main{
    public static void main(String [] args){
        System.out.printf("%70s================Welcome to java TaskManager===============\n", "");
    }
}