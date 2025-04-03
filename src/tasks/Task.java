package tasks;
import data.TaskStatus.TaskStatus;
import data.TypesTasks.TypesTasks;

import java.util.Objects;

public class Task {
   private int id;
    private String name;
    private String description;
    private TaskStatus status;


    public Task(String name, String description, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.status = status;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TypesTasks getType() {
        return TypesTasks.TASK;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Task task = (Task) obj;
        return Objects.equals(name, task.name) &&
                Objects.equals(description, task.description) &&
                (id == task.id) &&
                 status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, description, status);
    }

    @Override
    public String toString() {
        String result = "Task{" +
                "name='" + name + '\'' +
                ", id=" + id;
        if (description != null) {
            result = result + ", description.length=" + description.length();
        } else {
            result = result + ", description=null";
        }

        return result + ", status=" + status + '}';
    }

}
