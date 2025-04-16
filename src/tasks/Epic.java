package tasks;
import data.TaskStatus.TaskStatus;
import data.TypesTasks.TypesTasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private LocalDateTime endTime;

   private List<Subtask> listSubtask = new ArrayList<>();

    public Epic(String name, String description, TaskStatus status) {
        super(name, description, status);
    }

    public Epic(String name, String description, TaskStatus status, Duration duration, LocalDateTime startTime) {
        super(name, description, status, duration, startTime);
    }

    public Epic(String name, String description, TaskStatus status, Duration duration) {
        super(name, description, status, duration);
    }

    public List<Subtask> getListSubtask() {
        return listSubtask;
    }

    public void setListSubtask(List<Subtask> listSubtask) {
        this.listSubtask = listSubtask;
    }

    @Override
    public TypesTasks getType() {
        return TypesTasks.EPIC;
    }

    @Override
    public LocalDateTime getEndTime() {
        return super.getEndTime();
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Epic epic = (Epic) o;
        return Objects.equals(getName(), epic.getName()) &&
                Objects.equals(getDescription(), epic.getDescription()) &&
                (getId() == epic.getId());
    }

}
