package manager;

import taskData.TaskStatus;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {

    private final HistoryManager historyManager;

    public Map<Integer, Task> tasks = new HashMap<>();
    public Map<Integer, Epic> epics = new HashMap<>();
    public Map<Integer, Subtask> subtasks = new HashMap<>();
    public int counter = 1;

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
        task.setId(counter++);
        tasks.put(task.getId(), task);
        return task.getId();
    }

    @Override
    public int createEpic(Epic epic) {
        epic.setId(counter++);
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    @Override
    public int createSubtask(int id, Subtask subtask) {
        if (!epics.containsKey(id)) {
            return 0;
        }
        int numberDelete = 0;
        Epic epic = epics.get(id);
        subtask.setId(counter++);
        subtasks.put(subtask.getId(), subtask);
        epic.getListSubtask().add(subtask);
        changeStatus(numberDelete, epic.getId());
        return subtask.getId();
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
        for (Task task : tasks.values()) {
            historyManager.removeTask(task.getId());
        }
        tasks.clear();
    }

    @Override
    public void deleteAllEpic() {
        for (Epic epic : epics.values()) {
            historyManager.removeTask(epic.getId());
            for (Subtask exp : epic.getListSubtask()) {
                historyManager.removeTask(exp.getId());
            }
        }
        epics.clear();
        subtasks.clear();

    }

    @Override
    public void deleteAllSubtask() {
        int numberDelete = 0;
        for (Subtask subtask : subtasks.values()) {
            historyManager.removeTask(subtask.getId());
        }
        subtasks.clear();
        for (Integer key : epics.keySet()) {
            Epic epic = epics.get(key);
            epic.getListSubtask().clear();
            numberDelete++;
            changeStatus(numberDelete, epic.getId());
        }
    }

    // Вывод задачи по id
    @Override
    public Task getByIdTask(int id) {
        if (!(tasks.containsKey(id))) {
            return null;
        } else {
            Task task = tasks.get(id);
            historyManager.addTask(task);
            return task;
        }
    }

    @Override
    public Epic getByIdEpic(int id) {
        if (!(epics.containsKey(id))) {
            return null;
        } else {
            Epic epic = epics.get(id);
            historyManager.addTask(epic);
            return epic;
        }
    }

    @Override
    public Subtask getByIdSubtask(int id) {
        if (!(subtasks.containsKey(id))) {
            return null;
        } else {
            Subtask subtask = subtasks.get(id);
            historyManager.addTask(subtask);
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
        int numberDelete = 0;
        if (!(subtasks.containsKey(id))) {
            return subtask;
        }
        subtask.setId(id);
        subtasks.put(subtask.getId(), subtask);
        for (Integer key : epics.keySet()) {
            Epic epic = epics.get(key);
            for (int i = 0; i < epic.getListSubtask().size(); i++) {
                if (subtask.getId() == epic.getListSubtask().get(i).getId()) {
                    epic.getListSubtask().set(i, subtask);
                    changeStatus(numberDelete, epic.getId());
                }
            }
        }
        return subtask;
    }

    // Удаление по id
    @Override
    public void deleteByIdTask(int id) {
        tasks.remove(id);
        historyManager.removeTask(id);
    }

    @Override
    public void deleteByIdEpic(int id) {
        Epic epic = epics.get(id);
        historyManager.removeTask(id);
        for (Subtask exp : epic.getListSubtask()) {
            subtasks.remove(exp.getId());
            historyManager.removeTask(exp.getId());
        }
        epics.remove(id);
    }

    @Override
    public void deleteByIdSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        historyManager.removeTask(id);
        int numberDelete = 0;
        if (subtask.getId() == id) {
            subtasks.remove(id);
            for (Integer key : epics.keySet()) {
                Epic epic = epics.get(key);
                for (int i = 0; i < epic.getListSubtask().size(); i++) {
                    if (subtask.getId() == epic.getListSubtask().get(i).getId()) {
                        epic.getListSubtask().remove(i);
                        numberDelete++;
                        changeStatus(numberDelete, epic.getId());
                    }
                }
            }
        }
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
    public void changeStatus(int numberDelete, int id) {
        Epic epic = epics.get(id);
        int statusNew = 0;
        int statusProgress = 0;
        int statusDone = numberDelete;
        for (int i = 0; i < epic.getListSubtask().size(); i++) {

            if (epic.getListSubtask().get(i).getStatus().equals(TaskStatus.NEW)) {
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
