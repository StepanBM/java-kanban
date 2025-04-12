import manager.HistoryManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Test;
import data.TaskStatus.TaskStatus;
import tasks.Epic;
import tasks.Subtask;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    HistoryManager historyManager = Managers.getDefaultHistory();
    TaskManager taskManager = Managers.getDefault(historyManager);

    Epic epic1 = new Epic("Имя №1", "Ооооочень длинное описание № 1", TaskStatus.NEW);
    final int epicId = taskManager.createEpic(epic1);

    @Test
    void addNewSubtask() {

        Subtask subtask = new Subtask("Имя подзадачи №1", "Ооооочень длинное описание № 1", TaskStatus.NEW, 1);
        Subtask subtask2 = new Subtask("Имя подзадачи №2", "Ооооочень длинное описание № 2", TaskStatus.NEW, 1);
        Subtask subtask3 = new Subtask("Имя подзадачи №3", "Ооооочень длинное описание № 3", TaskStatus.NEW, 1);
        final int subtaskId = taskManager.createSubtask(subtask);
        final int subtaskId2 = taskManager.createSubtask(subtask2);
        final int subtaskId3 = taskManager.createSubtask(subtask3);

        final Subtask savedSubtask = taskManager.getByIdSubtask(subtaskId);

        assertEquals(subtask, savedSubtask, "Задачи не совпадают.");
        assertNotNull(savedSubtask, "Задача не найдена.");

        final List<Subtask> subtasks = taskManager.outputAllSubtask();

        assertNotNull(subtasks, "Задачи не возвращаются.");
        assertEquals(3, subtasks.size(), "Неверное количество задач.");
        assertEquals(subtask, subtasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void checkingForSubtaskConflictWithGivenIdAndGeneratedId() {

        Subtask subtask1 = new Subtask("Имя №1", "Ооооочень длинное описание № 1", TaskStatus.NEW, 1);
        Subtask subtask2 = new Subtask("Имя №1", "Ооооочень длинное описание № 1", TaskStatus.NEW, 1);
        final int subtaskId = taskManager.createSubtask(subtask1);

        Subtask savedSubtask = taskManager.updateSubtask(subtaskId, subtask2);
        assertEquals(subtask1, savedSubtask, "Задачи не совпадают.");
    }

}