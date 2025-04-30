package tasks;

import data.TaskStatus.TaskStatus;
import data.TypesTasks.TypesTasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Objects;

public class Task {
   private int id;
    private String name;
    private String description;
    private TaskStatus status;
    private Duration duration;
    private LocalDateTime startTime;

  // private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
  public Task(int id, String name, String description, TaskStatus status, Duration duration, LocalDateTime startTime) {
      this.id = id;
      this.name = name;
      this.description = description;
      this.status = status;
      this.duration = duration;
      this.startTime = startTime;

  }

    public Task(int id, String name, String description, TaskStatus status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;

    }

    public Task(String name, String description, TaskStatus status, Duration duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;

    }

    public Task(String name, String description, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.status = status;

    }

    public Task(String name, String description, TaskStatus status, Duration duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = duration;

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

    public String getLocalDateTimeString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        String formatDateTime = startTime.format(formatter);
        return formatDateTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }


//    public static class LocalDateAdapter extends TypeAdapter<LocalDateTime> {
//        private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
//
//        @Override
//        public void write(final JsonWriter jsonWriter, final LocalDateTime localDateTime) throws IOException {
//            if (localDateTime == null) {
//                jsonWriter.nullValue();
//            } else {
//                jsonWriter.value(localDateTime.format(formatter));
//            }
//        }
//
//        @Override
//        public LocalDateTime read(final JsonReader jsonReader) throws IOException {
//           // String nullOrNot = jsonReader.nextString();
//            return "null".equals(jsonReader.nextString()) ? null : LocalDateTime.parse(jsonReader.nextString(), formatter);
//        }
//    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Task task = (Task) obj;
        return Objects.equals(name, task.name) &&
                Objects.equals(description, task.description) &&
                (id == task.id) &&
                 status == task.status &&
                duration == task.duration;
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

        return result + ", status=" + status +  '}';
    }

}
