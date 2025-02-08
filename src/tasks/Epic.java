package tasks;
import status.TaskStatus;

import java.util.ArrayList;

public class Epic extends Task {
   private ArrayList<Subtask> listSubtask = new ArrayList<>();

    public Epic(String name, String description, TaskStatus status) {
        super(name, description, status);
    }

    public ArrayList<Subtask> getListSubtask() {
        return listSubtask;
    }

    public void setListSubtask(ArrayList<Subtask> listSubtask) {
        this.listSubtask = listSubtask;
    }

    @Override
    public String toString() {
        String result = "Epic{" +
                "name='" + getName() + '\'' +
                ", id=" + getId();
        if (getDescription() != null) {
            result = result + ", description.length=" + getDescription().length();
        } else {
            result = result + ", description=null";
        }

        return result + ", status=" + getStatus() + '}';
    }

}
