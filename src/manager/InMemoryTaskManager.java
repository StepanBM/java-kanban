package manager;

import status.TaskStatus;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    HistoryManager historyManager;

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int counterEpic = 1;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    // История просмотров задач, эпиков и подзадач по id
    @Override
    public List<Task> getHistory() {
            return historyManager.getHistory();
    }

    // Создание задач
    @Override
    public int createTask(Task task) {
        task.setId(counterEpic++);
        tasks.put(task.getId(), task);
        return task.getId();
    }

    @Override
    public int createEpic(Epic epic) {
        epic.setId(counterEpic++);
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    @Override
    public int createSubtask(int id, Subtask subtask) {
        if (!epics.containsKey(id)) {
            return 0;
        }
        Epic epic = epics.get(id);
        subtask.setepicID(counterEpic++);
        subtasks.put(subtask.getepicID(), subtask);
        epic.getListSubtask().add(subtask);
        changeStatus();
        return subtask.getepicID();
    }

    // Вывод всех задач
    @Override
    public ArrayList<Task> outputAllTask() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> outputAllEpic() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> outputAllSubtask() {
        return new ArrayList<>(subtasks.values());
    }

    // Удаление всех задач
    @Override
    public void deleteAllTask() {
        tasks.clear();
    }

    @Override
    public void deleteAllEpic() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteAllSubtask() {
        subtasks.clear();
        changeStatus();
    }

    // Вывод задачи по id
    @Override
    public Task getByIdTask(int id) {
        if (!(tasks.containsKey(id))) {
            return null;
        } else {
            Task task = tasks.get(id);
            historyManager.add(task);
            return task;
        }
    }

    @Override
    public Epic getByIdEpic(int id) {
        if (!(epics.containsKey(id))) {
            return null;
        } else {
            Epic epic = epics.get(id);
            historyManager.add(epic);
            return epic;
        }
    }

    @Override
    public Subtask getByIdSubtask(int id) {
        if (!(subtasks.containsKey(id))) {
            return null;
        } else {
            Subtask subtask = subtasks.get(id);
            historyManager.add(subtask);
            return subtask;
        }
    }

    // Обновление задач
    @Override
    public Task updateTask(int id, Task task) {
        task.setId(id);
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Epic updateEpic(int id, Epic epic) {
        epic.setId(id);
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public Subtask updateSubtask(int id, Subtask subtask) {
        if (!(subtasks.containsKey(id))) {
            return subtask;
        }
        subtask.setepicID(id);
        subtasks.put(subtask.getepicID(), subtask);
        for (Integer key : epics.keySet()) {
            Epic epic = epics.get(key);
            for (int i = 0; i < epic.getListSubtask().size(); i++) {
                if (subtask.getepicID() == epic.getListSubtask().get(i).getepicID()) {
                    epic.getListSubtask().set(i, subtask);
                }
            }
        }
        changeStatus();
        return subtask;
    }

    // Удаление по id
    @Override
    public void deleteByIdTask(int id) {
        tasks.remove(id);
    }

    @Override
    public void deleteByIdEpic(int id) {
        Epic epic = epics.get(id);
        for (Subtask exp : epic.getListSubtask()) {
            subtasks.remove(exp.getepicID());
        }
        epics.remove(id);
    }

    @Override
    public void deleteByIdSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask.getepicID() == id) {
            subtasks.remove(id);
            subtask.setStatus(TaskStatus.DONE);
        }
        changeStatus();
    }

    // Получение списка всех подзадач определённого эпика
    @Override
    public List<Subtask> getAllEpicSubtasks(int id) {
        Epic epic = epics.get(id);
        if (epics.containsKey(id) && !(epic.getListSubtask().isEmpty())) {
            return epic.getListSubtask();
        } else if (epics.containsKey(id) && epic.getListSubtask().isEmpty()) {
            return null;
        } else {
            return null;
        }
    }

    @Override
    public void changeStatus() {
        for (Integer key : epics.keySet()) {
            Epic epic = epics.get(key);
            int statusNew = 0;
            int statusProgress = 0;
            int statusDone = 0;
            for (int i = 0; i < epic.getListSubtask().size(); i++) {

                if (subtasks.isEmpty()) {
                    epic.getListSubtask().get(i).setStatus(TaskStatus.DONE);
                    epic.setStatus(TaskStatus.DONE);
                }
                if (epic.getListSubtask().get(i).getStatus().equals(TaskStatus.DONE)) {
                    statusDone++;
                } else if (epic.getListSubtask().get(i).getStatus().equals(TaskStatus.NEW)) {
                    statusNew++;
                } else {
                    statusProgress++;
                }
            }
            if (statusDone > 0 && statusNew == 0 && statusProgress == 0) {
                epic.setStatus(TaskStatus.DONE);
            } else if (statusDone == 0 && statusProgress == 0) {
                epic.setStatus(TaskStatus.NEW);
            } else {
                epic.setStatus(TaskStatus.IN_PROGRESS);
            }
        }
    }


}
