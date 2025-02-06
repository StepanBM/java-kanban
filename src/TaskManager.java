
import java.util.HashMap;

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
            System.out.println("Эпика с таким id не существует");
        }
        Epic epic = epics.get(id);
        subtask.setSubId(counterEpic++);
        subtasks.put(subtask.getSubId(), subtask);
        epic.getListSubtask().add(subtask);
    }

    // Вывод всех задач
    public void outputAllEpic() {
        for (Integer key : epics.keySet()) {
            Epic epic = epics.get(key);
            System.out.println(epic);
        }
    }

    public void outputAllTask() {
        for (Integer key : tasks.keySet()) {
            Task task = tasks.get(key);
            System.out.println(task);
        }
    }

    public void outputAllSubtask() {
        for (Integer key : subtasks.keySet()) {
            Subtask subtask = subtasks.get(key);
            System.out.println(subtask);
        }
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
        for (Integer key : epics.keySet()) {
            Epic epic = epics.get(key);
            epic.setStatus(TaskStatus.DONE);
            for (Subtask exp : epic.getListSubtask()) {
                exp.setStatus(TaskStatus.DONE);
            }
        }

    }

    // Вывод задачи по id
    public void outputByIdTask(int id) {
        if (tasks.containsKey(id)) {
            Task task = tasks.get(id);
            System.out.println(task);
        } else {
            System.out.println("Задачи с таким id не существует");
        }
    }

    public void outputByIdEpic(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            System.out.println(epic);
        } else {
            System.out.println("Эпика с таким id не существует");
        }
    }

    public void outputByIdSubtask(int id) {
        if (subtasks.containsKey(id)) {
            Subtask subtask = subtasks.get(id);
            System.out.println(subtask);
        } else {
            System.out.println("Подзадачи с таким id не существует");
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
        if (subtasks.containsKey(id)) {
            subtask.setSubId(id);
            subtasks.put(subtask.getSubId(), subtask);
            for (Integer key : epics.keySet()) {
                Epic epic = epics.get(key);
                for (int i = 0; i < epic.getListSubtask().size(); i++) {

                    if (subtask.getSubId() == epic.getListSubtask().get(i).getSubId()) {
                        epic.getListSubtask().set(i, subtask);
                        epic.setStatus(TaskStatus.IN_PROGRESS);

                    }

                }
            }
        } else {
            System.out.println("Подзадачи с таким id не существует");
        }

    }

    // Удаление по id
    public void deleteByIdTask(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else {
            System.out.println("Задачи с таким id не существует");
        }
    }

    public void deleteByIdEpic(int id) {
        if (epics.containsKey(id)) {
            epics.remove(id);
        } else {
            System.out.println("Эпика с таким id не существует");
        }
    }

    public void deleteByIdSubtask(int id) {
        if (subtasks.containsKey(id)) {
            Subtask subtask = subtasks.get(id);
            for (Integer key : epics.keySet()) {
                Epic epic = epics.get(key);
                for (Subtask exp : epic.getListSubtask()) {
                    if (subtask.getSubId() == exp.getSubId()) {
                        subtasks.remove(id);
                        subtask.setStatus(TaskStatus.DONE);
                        if (epic.getListSubtask().isEmpty()) {
                            epic.setStatus(TaskStatus.DONE);
                        } else {
                            epic.setStatus(TaskStatus.IN_PROGRESS);

                        }
                    }

                }
            }
        } else {
            System.out.println("Подзадачи с таким id не существует");
        }
    }

    // Получение списка всех подзадач определённого эпика
    public void getAllEpicSubtasks(int id) {
        Epic epic = epics.get(id);
        if (epics.containsKey(id) && !(epic.getListSubtask().isEmpty())) {
            System.out.println(epic.getListSubtask());
        } else if (epics.containsKey(id) && epic.getListSubtask().isEmpty()) {
            System.out.println("В этом эпике нет подзадач");
        } else {
            System.out.println("Эпика с таким id не существует");
        }
    }

}



