import manager.HistoryManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Test;
import data.TaskStatus.TaskStatus;
import tasks.Epic;
import tasks.Subtask;
import static org.junit.jupiter.api.Assertions.*;

//class SubtaskTest {
//
//    HistoryManager historyManager = Managers.getDefaultHistory();
//    TaskManager taskManager = Managers.getDefault(historyManager);
//
//    Epic epic1 = new Epic("Имя №1", "Ооооочень длинное описание № 1", TaskStatus.NEW);
//    final int epicId = taskManager.createEpic(epic1);
//
//    @Test
//    void checkingForSubtaskConflictWithGivenIdAndGeneratedId() {
//        Subtask subtask1 = new Subtask("Имя №1", "Ооооочень длинное описание № 1", TaskStatus.NEW,1);
//        Subtask subtask2 = new Subtask("Имя №1", "Ооооочень длинное описание № 1", TaskStatus.NEW,1);
//
//        final int subtaskId = taskManager.createSubtask(subtask1);
//        Subtask savedSubtask = taskManager.updateSubtask(subtaskId, subtask2);
//
//        assertEquals(subtask1, savedSubtask, "Задачи не совпадают.");
//
//    }
//
//}