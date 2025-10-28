import java.io.File;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

class Task{
    private int id;
    private String name;
    private  Date createdAt;

    Task(int id, String name, Date createdAt ){
     this.id = id;
     this.name = name;
     this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public Date getCreatedAt(){
        return createdAt;
    }
    public void setCreatedAt(Date createdAt){
        this.createdAt = createdAt;
    }

    public String toString(){
        return String.format("Task{Id=%d,Name=%s,CreatedAt=%s }", id,name,createdAt );
    }
}

class TaskDb{
    private final File file;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private  List<Task> tasks = new ArrayList<>();
    private int nextId =1 ;

    TaskDb(File file) {
        this.file = file;
        load();
    }
private void load(){}

}


public  class Main{
    public static void main(String [] args){
        System.out.printf("%70s================Welcome to java TaskManager===============\n", "");

    }
}