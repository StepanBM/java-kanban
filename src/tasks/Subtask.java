package tasks;
import data.TaskStatus.TaskStatus;
import data.TypesTasks.TypesTasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {

    private int epicID; // Тут ссылка (или id) на соответствующий Эпик, к которому принадлежит данная подзадача

    public Subtask(String name, String description, TaskStatus status, Duration duration, LocalDateTime startTime, int epicID) {
        super(name, description, status, duration, startTime);
        this.epicID = epicID;
    }

    public Subtask(String name, String description, TaskStatus status, int epicID) {
        super(name, description, status);
        this.epicID = epicID;
    }

    public Subtask(String name, String description, TaskStatus status, Duration duration, int epicID) {
        super(name, description, status, duration);
        this.epicID = epicID;
    }

    public Subtask(int id, String name, String description, TaskStatus status, Duration duration, LocalDateTime startTime, int epicID) {
        super(id, name, description, status, duration, startTime);
        this.epicID = epicID;
    }

    public Subtask(int id, String name, String description, TaskStatus status, int epicID) {
        super(id, name, description, status);
        this.epicID = epicID;
    }

    public int getepicID() {
        return epicID;
    }

    public void setepicID(int epicID) {
        this.epicID = epicID;
    }

    @Override
    public TypesTasks getType() {
        return TypesTasks.SUBTASK;
    }

    @Override
    public LocalDateTime getEndTime() {
        return super.getEndTime();
    }

    @Override
    public String toString() {
        String result = "Subtask{" +
                "name='" + getName() + '\'' +
                ", id=" + getId() + '\'' +
                ", epicID=" + getepicID();
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
