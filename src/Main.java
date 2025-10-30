import java.io.*;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

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
    private int nextId =1;

    TaskDb(File file) {
        this.file = file;
        load();
    }
private void load(){
        if (!file.exists()){
            tasks = new ArrayList<>();
            nextId = 1;
            return;
        }

        try(Reader r = new FileReader(file)){
            tasks = gson.fromJson(r, new TypeToken<>(){}.getType());
            if (tasks == null) tasks = new ArrayList<>();

            nextId = 1;
            for (int i = 0; i < tasks.size() ; i++) {  // or use enhanced for (Task t : tasks) { nextId = Math.max(nextId, t.getId}
                Task t = tasks.get(i);
                nextId =  Math.max(nextId, i) +1;
            }
        } catch (IOException | JsonSyntaxException e) {
            System.err.println("Failed to load data from JSON"+ e.getMessage());
            tasks = new ArrayList<>();
            nextId = 1;
        }
}

private void save() throws  OperationException {
      try(Writer w = new FileWriter(file)){
      gson.toJson(tasks, w);

      } catch (Exception e) {
     throw new OperationException("Task not save");
      }
}

}


public  class Main{
    public static void main(String [] args){
        System.out.printf("%70s================Welcome to java TaskManager===============\n", "");

    }
}