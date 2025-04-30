import manager.HistoryManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Test;
import data.TaskStatus.TaskStatus;
import tasks.Task;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    HistoryManager historyManager = Managers.getDefaultHistory();
    TaskManager taskManager = Managers.getDefault(historyManager);

    @Test
    void checkingForTaskConflictWithGivenIdAndGeneratedId() {
        Task task1 = new Task("Имя №1", "Ооооочень длинное описание № 1", TaskStatus.NEW);
        Task task2 = new Task(1,"Имя №1", "Ооооочень длинное описание № 1", TaskStatus.NEW);

        final int taskId = taskManager.createTask(task1);
        Task savedTask = taskManager.updateTask(task2);

        assertEquals(task1, savedTask, "Задачи не совпадают.");

    }

}