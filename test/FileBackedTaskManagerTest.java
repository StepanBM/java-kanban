import manager.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.nio.charset.StandardCharsets;
import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
//
//    File file;
//    HistoryManager historyManager = Managers.getDefaultHistory();
//
//    @BeforeEach
//    void toBegin() throws IOException {
//        file = File.createTempFile("resultTaskTest", ".csv");
//        super.taskManager = new FileBackedTaskManager(historyManager, file);
//        taskManager.clearTasksTest();
//        createTaskEpicSubtask();
//
//    }
//
//    @Test
//    void savingAnEmptyFile() {
//        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(file);
//
//        assertEquals(0, manager.outputAllTask().size());
//        assertEquals(0, manager.outputAllEpic().size());
//        assertEquals(0, manager.outputAllSubtask().size());
//
//    }
//
//    @Test
//    void savingMultipleTasks() {
//
//        FileBackedTaskManager taskManager = new FileBackedTaskManager(new InMemoryHistoryManager(), file);
//
//        taskManager.createTask(task1);
//        taskManager.createTask(task2);
//        taskManager.createEpic(epic1);
//        taskManager.createSubtask(subtask4);
//
//        taskManager.deleteAllTask();
//
//        FileBackedTaskManager loadedTaskManager = FileBackedTaskManager.loadFromFile(file);
//        assertEquals(1, loadedTaskManager.outputAllEpic().size());
//        assertEquals(1, loadedTaskManager.outputAllSubtask().size());
//        assertEquals("Имя №1", loadedTaskManager.outputAllEpic().get(0).getName());
//        assertEquals("Имя подзадачи №4", loadedTaskManager.outputAllSubtask().get(0).getName());
//
//    }
//
//    @Test
//    void uploadingMultipleTasks() throws IOException {
//
//        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
//
//            bw.write("id,type,name,status,description,duration,startTime,epic\n");
//            bw.write("1,TASK,Имя Task №1,NEW,Ооооочень длинное описание Task № 1,10,01.07.2025 00:54,epic\n");
//            bw.write("2,EPIC,Имя Epic №1,NEW,Ооооочень длинное описание Epic № 1,null,null,epic\n");
//            bw.write("historyTask:\n");
//
//        }
//
//        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(file);
//        assertEquals(1, manager.outputAllTask().size());
//        assertEquals(1, manager.outputAllEpic().size());
//        assertEquals("Имя Task №1", manager.outputAllTask().get(0).getName());
//        assertEquals("Имя Epic №1", manager.outputAllEpic().get(0).getName());
//        assertEquals("Ооооочень длинное описание Task № 1", manager.outputAllTask().get(0).getDescription());
//        assertEquals("Ооооочень длинное описание Epic № 1", manager.outputAllEpic().get(0).getDescription());
//
//    }
//
//}