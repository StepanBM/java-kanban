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

    int createSubtask(int id, Subtask subtask);

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
    Task updateTask(int id, Task task);

    Epic updateEpic(int id, Epic epic);

    Subtask updateSubtask(int id, Subtask subtask);

    // Удаление по id
    void deleteByIdTask(int id);

    void deleteByIdEpic(int id);

    void deleteByIdSubtask(int id);

    // Получение списка всех подзадач определённого эпика
    List<Subtask> getAllEpicSubtasks(int id);

    void changeStatus();

    List<Task> getHistory();

}