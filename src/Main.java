import java.io.*;
import java.util.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;


public class Main {


    public static class OperationException extends Exception {
        public OperationException(String message) { super(message); }
        public OperationException(String message, Throwable cause) { super(message, cause); }
    }

    // Task model
    public static class Task {
        private int id;
        private String name;
        private Date createdAt;

        public Task(int id, String name, Date createdAt) {
            this.id = id;
            this.name = name;
            this.createdAt = createdAt;
        }

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public Date getCreatedAt() { return createdAt; }
        public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

        @Override
        public String toString() {
            return String.format("Task{Id=%d, Name='%s', CreatedAt=%s}", id, name, createdAt);
        }
    }


    public static class TaskDb {
        private final File file;
        private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        private List<Task> tasks = new ArrayList<>();
        private int nextId = 1;

        public TaskDb(File file) {
            this.file = file;
            load();
        }


        private void load() {
            if (!file.exists()) {
                tasks = new ArrayList<>();
                nextId = 1;
                return;
            }

            try (Reader r = new FileReader(file)) {
                // explicit type token for List<Task>
                tasks = gson.fromJson(r, new TypeToken<List<Task>>() {}.getType());
                if (tasks == null) tasks = new ArrayList<>();

                // compute nextId based on existing task ids
                nextId = 1;
                // using regular for loop (not enhanced) as requested earlier
                for (int i = 0; i < tasks.size(); i++) {
                    Task t = tasks.get(i);
                    nextId = Math.max(nextId, t.getId() + 1);
                }
            } catch (IOException | JsonSyntaxException e) {
                System.out.println("Failed to laod tasks form Json"+e.getMessage());
                tasks = new ArrayList<>();
                nextId = 1;
            }
        }


        private void save() throws OperationException {
            try (Writer w = new FileWriter(file)) {
                gson.toJson(tasks, w);
            } catch (Exception e) {
                throw new OperationException("Task not saved", e);
            }
        }


        public Task addTask(String name) throws OperationException {
            Task t = new Task(nextId++, name, new Date());
            tasks.add(t);
            save();
            return t;
        }


        public List<Task> listTasks() {
            return new ArrayList<>(tasks);
        }


        public Task findById(int id) {
            for (Task t : tasks) if (t.getId() == id) return t;
            return null;
        }


        public boolean updateTask(int id, String newName) throws OperationException {
            Task t = findById(id);
            if (t == null) return false;
            t.setName(newName);
            save();
            return true;
        }


        public boolean deleteTask(int id) throws OperationException {
            Iterator<Task> it = tasks.iterator();
            while (it.hasNext()) {
                if (it.next().getId() == id) {
                    it.remove();
                    save();
                    return true;
                }
            }
            return false;
        }
    }


    public static void main(String[] args) throws OperationException {
        File dbFile = new File("tasks.json");
        TaskDb store = new TaskDb(dbFile);
        Scanner sc = new Scanner(System.in);
        System.out.println("\n\t\t\t\t\t\t\t\t\t\t\t===================================Welcome to Snap! your best ever TaskManager.=======================================");

        boolean running = true;
        while (running) {
            System.out.println("\n Menu:");
            System.out.println("1) [~Task-cli~] add a task:");
            System.out.println("2) [~Task-cli~] view all tasks:");
            System.out.println("3) [~Task-cli~] view a task by Id:");
            System.out.println("4) [~Task-cli~] update a task by Id:");
            System.out.println("5) [~Task-cli~] delete a task by Id:");
            System.out.println("6) [~Task-cli~] Exit");
            System.out.println("Choose your option:");


            String opt = sc.nextLine().trim();
            switch (opt) {
                case "1":
                    System.out.println("+[~Task-cli~]+ Enter task name: ");
                    String name = sc.nextLine().trim();
                    if (name.isEmpty()) {
                        System.out.println("Oops!name is empty");
                        break;
                    }
                    Task added = store.addTask(name);
                    System.out.println("Hooray! task added successfully");
                    break;

                case "2":
                    System.out.println("Here is all your tasks:");
                    List<Task> all = store.listTasks();
                    if (all.isEmpty()) {
                        System.out.println("No task waiting task");
                        break;
                    } else {
                        System.out.println("Your tasks:");
                        for (Task t : all) {
                            System.out.println(t);
                        }
                    }
                    break;

                case "3":
                    System.out.println("+[~Task-cli~]+ Enter task id");
                    int vid = readInt(sc);
                    Task vt = store.findById(vid);
                    if (vt == null) System.out.println("Task not found.");
                    else System.out.println(vt);
                    break;

                case "4":
                    System.out.println("+[~Task-cli~]+ Enter task Id: ");
                    int uid = readInt(sc);
                    Task ut = store.findById(uid);
                 if(ut == null ){
                     System.out.println("Task not found");
                     break;
                 }
                    System.out.println("Current: "+ ut);
                    System.out.println("Enter new name: ");
                    String newName = sc.nextLine().trim();
                    if (newName.isEmpty()){
                        System.out.println("Oops! name can not be empty");
                        break;
                    }
                    if(store.updateTask(uid, newName)){ System.out.println("Update complete successfully"); }
                    break;

                case "5":
                    System.out.println("+[~Task-cli~]+ Enter task Id:");
                    int did = readInt(sc);
                    Task dt = store.findById(did);
                    if (dt == null){
                        System.out.println("Task not found");
                        break;
                    }
                    if (store.deleteTask(did)){
                        System.out.println("task deleted successfully");
                    }
                    break;

                case "6":
                    running = false;
                    System.out.println("Goodbye!");
                    break;

                default:
                    System.out.println("Option does not exist");
            }


        }
sc.close();
    }
    private static int readInt(Scanner sc) {
        String s = sc.nextLine().trim();
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}


