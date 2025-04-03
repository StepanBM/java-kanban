import manager.*;
import org.junit.jupiter.api.Test;
import data.TaskStatus.TaskStatus;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTaskManagerTest {

    @Test
    void savingAnEmptyFile() throws IOException {

        File file = File.createTempFile("resultTaskTest", ".csv");

        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(file);

        assertEquals(0, manager.outputAllTask().size());
        assertEquals(0, manager.outputAllEpic().size());
        assertEquals(0, manager.outputAllSubtask().size());
    }

    @Test
    void savingMultipleTasks() throws IOException {
        File file = File.createTempFile("resultTaskTest", ".csv");

        FileBackedTaskManager taskManager = new FileBackedTaskManager(new InMemoryHistoryManager(), file);

        Task task1 = new Task("Имя №1", "Ооооочень длинное описание № 1", TaskStatus.NEW);
        Task task2 = new Task("Имя №2", "Ооооочень длинное описание № 2", TaskStatus.NEW);
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        Epic epic = new Epic("epic", "desription", TaskStatus.NEW);
        taskManager.createEpic(epic);

        Subtask subtask=new Subtask("sub1","descrr1", TaskStatus.NEW, epic.getId());
        taskManager.createSubtask(subtask);

        taskManager.deleteAllTask();

        FileBackedTaskManager loadedTaskManager = FileBackedTaskManager.loadFromFile(file);

        assertEquals(1, loadedTaskManager.outputAllEpic().size());
        assertEquals(1, loadedTaskManager.outputAllSubtask().size());

        assertEquals("epic", loadedTaskManager.outputAllEpic().get(0).getName());
        assertEquals("sub1", loadedTaskManager.outputAllSubtask().get(0).getName());
    }

    @Test
    void uploadingMultipleTasks() throws IOException {
        File file = File.createTempFile("resultTaskTest", ".csv");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            bw.write("id,type,name,status,description,epic\n");
            bw.write("1,TASK,Имя Task №1,NEW,Ооооочень длинное описание Task № 1,epic\n");
            bw.write("2,EPIC,Имя Epic №1,NEW,Ооооочень длинное описание Epic № 1,epic\n");
            bw.write("historyTask:\n");
        }

        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(file);

        assertEquals(1, manager.outputAllTask().size());
        assertEquals(1, manager.outputAllEpic().size());
        assertEquals("Имя Task №1", manager.outputAllTask().get(0).getName());
        assertEquals("Имя Epic №1", manager.outputAllEpic().get(0).getName());
        assertEquals("Ооооочень длинное описание Task № 1", manager.outputAllTask().get(0).getDescription());
        assertEquals("Ооооочень длинное описание Epic № 1", manager.outputAllEpic().get(0).getDescription());
    }

}
