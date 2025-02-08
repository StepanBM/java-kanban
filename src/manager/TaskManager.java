package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import status.TaskStatus;

import java.util.HashMap;
import java.util.ArrayList;

public class TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int counterEpic = 1;

    // Создание задач
    public void createTask(Task task) {
        task.setId(counterEpic++);
        tasks.put(task.getId(), task);
    }

    public void createEpic(Epic epic) {
        epic.setId(counterEpic++);
        epics.put(epic.getId(), epic);
    }

    public void createSubtask(int id, Subtask subtask) {
        if (!epics.containsKey(id)) {
            return;
        }
        Epic epic = epics.get(id);
        subtask.setepicID(counterEpic++);
        subtasks.put(subtask.getepicID(), subtask);
        epic.getListSubtask().add(subtask);
        changeStatus();
    }

    // Вывод всех задач
    public ArrayList<Task> outputAllTask() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> outputAllEpic() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> outputAllSubtask() {
        return new ArrayList<>(subtasks.values());
    }

    // Удаление всех задач
    public void deleteAllTask() {
        tasks.clear();
    }

    public void deleteAllEpic() {
        epics.clear();
        subtasks.clear();
    }

    public void deleteAllSubtask() {
        subtasks.clear();
        changeStatus();
    }

    // Вывод задачи по id
    public Task getByIdTask(int id) {
        if (!(tasks.containsKey(id))) {
           // System.out.println("Задачи с таким id не существует");
            return null;
        } else {
            Task task = tasks.get(id);
            return task;
        }
    }

    public Epic getByIdEpic(int id) {
        if (!(epics.containsKey(id))) {
            return null;
        } else {
            Epic epic = epics.get(id);
           return epic;
        }
    }

    public Subtask getByIdSubtask(int id) {
        if (!(subtasks.containsKey(id))) {
            return null;
        } else {
            Subtask subtask = subtasks.get(id);
            return subtask;
        }
    }

    // Обновление задач
    public void updateTask(int id, Task task) {
        task.setId(id);
        tasks.put(task.getId(), task);
    }

    public void updateEpic(int id, Epic epic) {
        epic.setId(id);
        epics.put(epic.getId(), epic);
    }

    public void updateSubtask(int id, Subtask subtask) {
        if (!(subtasks.containsKey(id))) {
            return;
        }
        subtask.setepicID(id);
        subtasks.put(subtask.getepicID(), subtask);
        for (Integer key : epics.keySet()) {
            Epic epic = epics.get(key);
            for (int i = 0; i < epic.getListSubtask().size(); i++) {
                if (subtask.getepicID() == epic.getListSubtask().get(i).getepicID()) {
                    epic.getListSubtask().set(i, subtask);
                    epic.setStatus(TaskStatus.IN_PROGRESS);
                }
            }
        }

    }

    // Удаление по id
    public void deleteByIdTask(int id) {
            tasks.remove(id);
    }

    public void deleteByIdEpic(int id) {
        Epic epic = epics.get(id);
        for (Subtask exp : epic.getListSubtask()) {
            subtasks.remove(exp.getepicID());
        }
        epics.remove(id);
    }

public void deleteByIdSubtask(int id) {
    Subtask subtask = subtasks.get(id);
    if (subtask.getepicID() == id) {
        subtasks.remove(id);
        subtask.setStatus(TaskStatus.DONE);
    }
    changeStatus();

}

    // Получение списка всех подзадач определённого эпика
    public ArrayList<Subtask> getAllEpicSubtasks(int id) {
        Epic epic = epics.get(id);
        if (epics.containsKey(id) && !(epic.getListSubtask().isEmpty())) {
            return epic.getListSubtask();
        } else if (epics.containsKey(id) && epic.getListSubtask().isEmpty()) {
            return null;
        } else {
            return null;
        }
    }

    public void changeStatus() {
        for (Integer key : epics.keySet()) {
            Epic epic = epics.get(key);
            int statusNew = 0;
            int statusProgress = 0;
            int statusDone = 0;
            System.out.println("Эпик " + key);
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
            if (statusDone>0 && statusNew==0 && statusProgress==0) {
                epic.setStatus(TaskStatus.DONE);
            } else if (statusDone==0 && statusProgress==0) {
                epic.setStatus(TaskStatus.NEW);
            } else {
                epic.setStatus(TaskStatus.IN_PROGRESS);
            }
        }

    }

}