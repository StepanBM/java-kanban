import data.TaskStatus.TaskStatus;
//import exceptions.ManagerErrorSaveTaskTime;
import manager.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

//public abstract class TaskManagerTest<T extends TaskManager> {
//
//    T taskManager;
//    Task task1;
//    Task task2;
//    Task task3;
//    Task task4;
//    Epic epic1;
//    Epic epic2;
//    Subtask subtask1;
//    Subtask subtask2;
//    Subtask subtask3;
//    Subtask subtask4;
//
//    public void createTaskEpicSubtask() {
//        task1 = new Task("Имя подзадачи №1", "Ооооочень длинное описание № 1", TaskStatus.NEW, Duration.ofMinutes(10), LocalDateTime.of(2222, 1, 1, 0, 9));
//        task2 = new Task("Имя подзадачи №2", "Ооооочень длинное описание № 2", TaskStatus.NEW, Duration.ofMinutes(10), LocalDateTime.of(1999, 1, 1, 0, 12));
//        task3 = new Task("Имя подзадачи №3", "Ооооочень длинное описание № 3", TaskStatus.NEW, Duration.ofMinutes(10), LocalDateTime.of(2025, 1, 1, 0, 12));
//        task4 = new Task("Имя подзадачи №4", "Ооооочень длинное описание № 4", TaskStatus.NEW, Duration.ofMinutes(10), LocalDateTime.of(2025, 1, 1, 0, 10));
//        epic1 = new Epic("Имя №1", "Ооооочень длинное описание № 1", TaskStatus.NEW);
//        epic2 = new Epic("Имя №2", "Ооооочень длинное описание № 2", TaskStatus.NEW);
//        subtask1 = new Subtask("Имя подзадачи №1", "Ооооочень длинное описание № 1", TaskStatus.NEW, Duration.ofMinutes(10), LocalDateTime.of(2011, 7, 1, 0, 9), 1);
//        subtask2 = new Subtask("Имя подзадачи №2", "Ооооочень длинное описание № 2", TaskStatus.NEW, Duration.ofMinutes(10), LocalDateTime.of(2012, 7, 1, 0, 20), 1);
//        subtask3 = new Subtask("Имя подзадачи №3", "Ооооочень длинное описание № 3", TaskStatus.NEW, Duration.ofMinutes(10), LocalDateTime.of(2033, 7, 1, 0, 35), 1);
//        subtask4 = new Subtask("Имя подзадачи №4", "Ооооочень длинное описание № 4", TaskStatus.NEW, Duration.ofMinutes(10), LocalDateTime.of(2021, 7, 21, 0, 35), 3);
//
//    }
//
//    @Test
//    void createTask() {
//        final int taskId = taskManager.createTask(task1);
//        final Task savedTask = taskManager.getByIdTask(taskId);
//
//        assertEquals(task1, savedTask, "Задачи не совпадают.");
//        assertNotNull(savedTask, "Задача не найдена.");
//
//        final List<Task> tasks = taskManager.outputAllTask();
//
//        assertNotNull(tasks, "Задачи не возвращаются.");
//        assertEquals(1, tasks.size(), "Неверное количество задач.");
//        assertEquals(task1, tasks.get(0), "Задачи не совпадают.");
//
//    }
//
//    @Test
//    void createEpic() {
//        final int epicId = taskManager.createEpic(epic1);
//        final Epic savedEpic = taskManager.getByIdEpic(epicId);
//
//        assertEquals(epic1, savedEpic, "Задачи не совпадают.");
//        assertNotNull(savedEpic, "Задача не найдена.");
//
//        final List<Epic> epics = taskManager.outputAllEpic();
//
//        assertNotNull(epics, "Задачи не возвращаются.");
//        assertEquals(1, epics.size(), "Неверное количество задач.");
//        assertEquals(epic1, epics.get(0), "Задачи не совпадают.");
//
//    }
//
//    @Test
//    void createSubtask() {
//        final int epicId = taskManager.createEpic(epic1);
//        final int subtaskId = taskManager.createSubtask(subtask1);
//        final int subtaskId2 = taskManager.createSubtask(subtask2);
//        final Subtask savedSubtask = taskManager.getByIdSubtask(subtaskId);
//
//        assertEquals(subtask1, savedSubtask, "Задачи не совпадают.");
//        assertNotNull(savedSubtask, "Задача не найдена.");
//
//        final List<Subtask> subtasks = taskManager.outputAllSubtask();
//
//        assertNotNull(epic1.getListSubtask(), "Список подзадач не должен быть null.");
//        assertEquals(2, epic1.getListSubtask().size(), "Неверное количество подзадач.");
//        assertTrue(epic1.getListSubtask().contains(subtask1), "Список подзадач не содержит подзадачу 1.");
//        assertTrue(epic1.getListSubtask().contains(subtask2), "Список подзадач не содержит подзадачу 2.");
//        assertNotNull(subtasks, "Задачи не возвращаются.");
//        assertEquals(subtask1, subtasks.get(0), "Задачи не совпадают.");
//    }
//
//    @Test
//    void deleteByIdTask() {
//        int taskId = task1.getId();
//        taskManager.deleteByIdTask(taskId);
//
//        assertNull(taskManager.getByIdTask(taskId), "Задача не была удалена.");
//
//        List<Task> tasks = taskManager.outputAllTask();
//
//        assertEquals(0, tasks.size(), "Неверное количество задач после удаления.");
//
//    }
//
//    @Test
//    void deleteByIdEpic() {
//        final int epicId = taskManager.createEpic(epic1);
//        taskManager.deleteByIdEpic(epicId);
//
//        assertNull(taskManager.getByIdEpic(epicId), "Задача не была удалена.");
//
//        List<Epic> epics = taskManager.outputAllEpic();
//
//        assertEquals(0, epics.size(), "Неверное количество задач после удаления.");
//
//    }
//
//    @Test
//    void deleteByIdSubtask() {
//        final int epicId = taskManager.createEpic(epic1);
//        final int subtaskId = taskManager.createSubtask(subtask3);
//
//        taskManager.deleteByIdSubtask(subtaskId);
//
//        assertNull(taskManager.getByIdSubtask(subtaskId), "Задача не была удалена.");
//
//        List<Subtask> subtasks = taskManager.outputAllSubtask();
//
//        assertEquals(0, subtasks.size(), "Неверное количество задач после удаления.");
//
//    }
//
//    @Test
//    void calculatingTheIntersectionOfTaskTimeIntervals() {
//        ManagerErrorSaveTaskTime exception = assertThrows(ManagerErrorSaveTaskTime.class,
//                () -> {
//                    taskManager.createTask(task3);
//                    taskManager.createTask(task4);
//                });
//        assertEquals(
//                "Не получилось сохранить задачу, измените время начала",
//                exception.getMessage());
//
//    }
//
//}
