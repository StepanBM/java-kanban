package tasks;
import taskType.TaskStatus;

import java.util.Objects;

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
                ", id=" + getId();
        if (getDescription() != null) {
            result = result + ", description.length=" + getDescription().length();
        } else {
            result = result + ", description=null";
        }

        return result + ", status=" + getStatus() + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subtask subtask = (Subtask) o;
        return Objects.equals(getName(), subtask.getName()) &&
                Objects.equals(getDescription(), subtask.getDescription()) &&
                (getId() == subtask.getId());
    }

}
