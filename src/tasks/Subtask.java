package tasks;
import status.TaskStatus;

public class Subtask extends Task {

    private int epicID; // Тут ссылка (или id) на соответствующий Эпик, к которому принадлежит данная подзадача

    public Subtask(String name, String description, TaskStatus status) {
        super(name, description, status);
    }

    public int getepicID() {
        return epicID;
    }

    public void setepicID(int epicID) {
        this.epicID = epicID;
    }

    @Override
    public String toString() {
        String result = "Subtask{" +
                "name='" + getName() + '\'' +
                ", epicID=" + epicID;
        if (getDescription() != null) {
            result = result + ", description.length=" + getDescription().length();
        } else {
            result = result + ", description=null";
        }

        return result + ", status=" + getStatus() + '}';
    }

}
