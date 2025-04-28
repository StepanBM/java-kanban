package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    // Создание задач
    int createTask(Task task);

    int createEpic(Epic epic);

    int createSubtask(Subtask subtask);

    // Вывод всех задач
    ArrayList<Task> outputAllTask();

    ArrayList<Epic> outputAllEpic();

    ArrayList<Subtask> outputAllSubtask();

    // Удаление всех задач
    void deleteAllTask();

    void deleteAllEpic();

    void deleteAllSubtask();

    // Вывод задачи по id
    Task getByIdTask(int id);

    Epic getByIdEpic(int id);

    Subtask getByIdSubtask(int id);

    // Обновление задач
    Task updateTask(Task task);

    Epic updateEpic(Epic epic);

    Subtask updateSubtask(Subtask subtask);

    // Удаление по id
    void deleteByIdTask(int id);

    void deleteByIdEpic(int id);

    void deleteByIdSubtask(int id);

    // Получение списка всех подзадач определённого эпика
    List<Subtask> getAllEpicSubtasks(int id);

    void changeStatus(int numberDelete, int id);

    List<Task> getHistory();

    List<Task> getPrioritizedTasks();

    boolean theTaskIntersectsInTheList(Task task);

}