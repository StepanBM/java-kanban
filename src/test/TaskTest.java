package test;

import manager.HistoryManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Test;
import status.TaskStatus;
import tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    HistoryManager historyManager = Managers.getDefaultHistory();
    TaskManager taskManager = Managers.getDefault(historyManager);

    @Test
    void addNewTask() {

        Task task = new Task("Имя №1", "Ооооочень длинное описание № 1", TaskStatus.NEW);
        final int taskId = taskManager.createTask(task);

        final Task savedTask = taskManager.getByIdTask(taskId);

        assertEquals(task, savedTask, "Задачи не совпадают.");
        assertNotNull(savedTask, "Задача не найдена.");

        final List<Task> tasks = taskManager.outputAllTask();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void checkingForTaskConflictWithGivenIdAndGeneratedId() {

        Task task1 = new Task("Имя №1", "Ооооочень длинное описание № 1", TaskStatus.NEW);
        Task task2 = new Task("Имя №1", "Ооооочень длинное описание № 1", TaskStatus.NEW);
        final int taskId = taskManager.createTask(task1);

        Task savedTask = taskManager.updateTask(taskId, task2);
        assertEquals(task1, savedTask, "Задачи не совпадают.");
    }

}