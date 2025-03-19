package manager;

import tasks.Task;

import java.util.List;

public interface HistoryManager {

    List<Task> getHistory();

    Task addTask(Task task);

    void removeTask(int id);

}
