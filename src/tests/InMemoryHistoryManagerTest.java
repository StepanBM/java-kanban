package tests;

import manager.HistoryManager;
import manager.Managers;
import org.junit.jupiter.api.Test;
import status.TaskStatus;
import manager.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    HistoryManager historyManager = Managers.getDefaultHistory();
    TaskManager taskManager = Managers.getDefault(historyManager);

    @Test
    void addHistory() {
        Task task = new Task("Имя №1", "Ооооочень длинное описание № 1", TaskStatus.NEW);
        Epic epic = new Epic("Имя №1", "Ооооочень длинное описание № 1", TaskStatus.NEW);
        Subtask subtask = new Subtask("Имя подзадачи №1", "Ооооочень длинное описание № 1", TaskStatus.NEW);
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(3, history.size(), "История не пустая.");
    }

    @Test
    void tasksHistoryManagerSavePreviousVersionAfterTheUpdate() {
        Epic epic = new Epic("Имя №1", "Ооооочень длинное описание № 1", TaskStatus.NEW);
        Subtask subtask = new Subtask("Имя подзадачи №1", "Ооооочень длинное описание № 1", TaskStatus.NEW);
        Subtask subtaskNew = new Subtask("Имя обновленное", "Ооооочень длинное описание", TaskStatus.NEW);
        historyManager.add(subtask);
        historyManager.add(epic);
        taskManager.updateSubtask(subtask.getepicID(), subtaskNew);
        historyManager.add(subtaskNew);

        final List<Task> history = historyManager.getHistory();
        assertEquals(subtask, history.get(0), "Задача отсутствует.");
        assertEquals(subtaskNew, history.get(2), "Задача отсутствует.");
    }

}