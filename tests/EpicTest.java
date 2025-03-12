

import manager.HistoryManager;
import manager.Managers;
import org.junit.jupiter.api.Test;
import status.TaskStatus;
import manager.TaskManager;
import tasks.Epic;
import tasks.Subtask;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    HistoryManager historyManager = Managers.getDefaultHistory();
    TaskManager taskManager = Managers.getDefault(historyManager);

    @Test
    void addNewEpic() {

        Epic epic1 = new Epic("Имя №1", "Ооооочень длинное описание № 1", TaskStatus.NEW);
        final int epicId = taskManager.createEpic(epic1);

        final Epic savedEpic = taskManager.getByIdEpic(epicId);
        assertEquals(epic1, savedEpic, "Задачи не совпадают.");
        assertNotNull(savedEpic, "Задача не найдена.");

        final List<Epic> epics = taskManager.outputAllEpic();

        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic1, epics.get(0), "Задачи не совпадают.");
    }

    @Test
    void checkingForEpicConflictWithGivenIdAndGeneratedId() {

        Epic epic1 = new Epic("Имя №1", "Ооооочень длинное описание № 1", TaskStatus.NEW);
        Epic epic2 = new Epic("Имя №1", "Ооооочень длинное описание № 1", TaskStatus.NEW);
        final int epicId = taskManager.createEpic(epic1);

        Epic savedEpic = taskManager.updateEpic(epicId, epic2);
        assertEquals(epic1, savedEpic, "Задачи не совпадают.");
    }

    @Test
    void epicStatusWhenAllSubtasksAreCompleted() {

        Epic epic1 = new Epic("Имя №1", "Ооооочень длинное описание № 1", TaskStatus.NEW);
        final int epicId = taskManager.createEpic(epic1);

        Subtask subtask = new Subtask("Имя подзадачи №1", "Ооооочень длинное описание № 1", TaskStatus.NEW);
        Subtask subtask2 = new Subtask("Имя подзадачи №2", "Ооооочень длинное описание № 2", TaskStatus.NEW);
        Subtask subtask3 = new Subtask("Имя подзадачи №3", "Ооооочень длинное описание № 3", TaskStatus.NEW);
        taskManager.createSubtask(epicId, subtask);
        taskManager.createSubtask(epicId, subtask2);
        taskManager.createSubtask(epicId, subtask3);
        taskManager.deleteAllSubtask();
        assertEquals(TaskStatus.DONE, epic1.getStatus());

    }

    @Test
    void epicStatusWhenAllSubtasksAreNew() {

        Epic epic1 = new Epic("Имя №1", "Ооооочень длинное описание № 1", TaskStatus.NEW);
        final int epicId = taskManager.createEpic(epic1);

        Subtask subtask = new Subtask("Имя подзадачи №1", "Ооооочень длинное описание № 1", TaskStatus.NEW);
        Subtask subtask2 = new Subtask("Имя подзадачи №2", "Ооооочень длинное описание № 2", TaskStatus.NEW);
        Subtask subtask3 = new Subtask("Имя подзадачи №3", "Ооооочень длинное описание № 3", TaskStatus.NEW);
        taskManager.createSubtask(epicId, subtask);
        taskManager.createSubtask(epicId, subtask2);
        taskManager.createSubtask(epicId, subtask3);

        assertEquals(TaskStatus.NEW, epic1.getStatus());

    }

    @Test
    void epicStatusWhenSomeOfTheSubtasksAreCompletedAndSomeAreNot() {

        Epic epic1 = new Epic("Имя №1", "Ооооочень длинное описание № 1", TaskStatus.NEW);
        final int epicId = taskManager.createEpic(epic1);

        Subtask subtask = new Subtask("Имя подзадачи №1", "Ооооочень длинное описание № 1", TaskStatus.NEW);
        Subtask subtask2 = new Subtask("Имя подзадачи №2", "Ооооочень длинное описание № 2", TaskStatus.NEW);
        Subtask subtask3 = new Subtask("Имя подзадачи №3", "Ооооочень длинное описание № 3", TaskStatus.NEW);
        final int subtaskId = taskManager.createSubtask(epicId, subtask);
        final int subtaskId2 = taskManager.createSubtask(epicId, subtask2);
        final int subtaskId3 = taskManager.createSubtask(epicId, subtask3);

        taskManager.deleteByIdSubtask(subtaskId2);
        taskManager.deleteByIdSubtask(subtaskId3);

        assertEquals(TaskStatus.IN_PROGRESS, epic1.getStatus());

    }

}