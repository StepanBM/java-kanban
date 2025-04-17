import manager.HistoryManager;
import manager.Managers;
import org.junit.jupiter.api.Test;
import data.TaskStatus.TaskStatus;
import manager.TaskManager;
import tasks.Task;
import java.time.Duration;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    HistoryManager historyManager = Managers.getDefaultHistory();
    TaskManager taskManager = Managers.getDefault(historyManager);

    @Test
    void removingDuplicatesFromTheHistory() {
        Task task = new Task("Имя №1", "Ооооочень длинное описание № 1", TaskStatus.NEW, Duration.ofMinutes(10), LocalDateTime.of(2025, 8, 1, 0, 9));
        Task task2 = new Task("Имя №2", "Ооооочень длинное описание № 2", TaskStatus.NEW, Duration.ofMinutes(10), LocalDateTime.of(2025, 4, 1, 0, 9));

        final int taskId1 = taskManager.createTask(task);
        final int taskId2 = taskManager.createTask(task2);

        taskManager.getByIdTask(taskId1);
        taskManager.getByIdTask(taskId2);
        taskManager.getByIdTask(taskId1);

        assertEquals(task, taskManager.getByIdTask(taskId1));
        assertEquals(task2, taskManager.getByIdTask(taskId2));
        assertEquals(2, historyManager.getHistory().size());

    }

    @Test
    void deletingTasksFromTheHistory() {
        Task task = new Task("Имя №1", "Ооооочень длинное описание № 1", TaskStatus.NEW, Duration.ofMinutes(10), LocalDateTime.of(2024, 7, 1, 0, 9));
        final int taskId = taskManager.createTask(task);

        taskManager.getByIdTask(taskId);
        taskManager.deleteByIdTask(taskId);

        assertTrue(historyManager.getHistory().isEmpty());

    }

    @Test
    void ThePreviousVersionTasksSavedInTheHistory() {
        Task task = new Task("Имя №1", "Ооооочень длинное описание № 1", TaskStatus.NEW, Duration.ofMinutes(10), LocalDateTime.of(2025, 7, 1, 0, 9));
        Task task2 = new Task("Имя №2", "Ооооочень длинное описание № 2", TaskStatus.NEW, Duration.ofMinutes(10), LocalDateTime.of(2025, 5, 1, 0, 9));

        final int taskId1 = taskManager.createTask(task);
        taskManager.getByIdTask(taskId1);
        Task savedTask = taskManager.updateTask(taskId1, task2);

        assertTrue(historyManager.getHistory()!=savedTask);

    }

}